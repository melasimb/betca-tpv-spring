package es.upm.miw.betca_tpv_spring.api_rest_controllers;

import es.upm.miw.betca_tpv_spring.business_controllers.MessagesController;
import es.upm.miw.betca_tpv_spring.dtos.MessagesCreationDto;
import es.upm.miw.betca_tpv_spring.dtos.MessagesDto;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(MessagesResource.MESSAGES)
public class MessagesResource {

    public static final String MESSAGES = "/messages";
    public static final String MESSAGES_ID = "/{id}";

    private MessagesController messagesController;

    @Autowired
    public MessagesResource(MessagesController messagesController) {
        this.messagesController = messagesController;
    }

    @PostMapping
    public Mono<MessagesDto> createMessage(@Valid @RequestBody MessagesCreationDto messagesCreationDto) {
        return this.messagesController.createMessage(messagesCreationDto)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @GetMapping
    public Flux<MessagesDto> readAll() {
        return this.messagesController.readAll()
                .doOnEach(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @GetMapping(value = MESSAGES_ID)
    public Mono<MessagesDto> readById(@PathVariable String id) {
        return this.messagesController.readById(id)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @PutMapping(value = MESSAGES_ID)
    public Mono<MessagesDto> markMessageAsRead(@PathVariable String id, @RequestParam String readDate) {
        LocalDateTime ldtReadDate = LocalDateTime.parse(readDate, DateTimeFormatter.ISO_DATE_TIME);
        return this.messagesController.markMessageAsRead(id, ldtReadDate)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }
}
