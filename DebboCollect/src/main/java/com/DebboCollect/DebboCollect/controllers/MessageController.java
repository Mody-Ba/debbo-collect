package com.DebboCollect.DebboCollect.controllers;

import com.DebboCollect.DebboCollect.Model.MessageRequest;
import com.DebboCollect.DebboCollect.Model.MessageResponse;
import com.DebboCollect.DebboCollect.services.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISEUR','ENQUETEUR')")
    public MessageResponse creerMessage(@Valid @RequestBody MessageRequest request) {

        return messageService.creerMessage(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISEUR')")
    public List<MessageResponse> afficherTousLesMessages() {

        return messageService.afficherTousLesMessages();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISEUR')")
    public MessageResponse afficherMessageParId(@PathVariable Long id) {

        return messageService.afficherMessageParId(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public MessageResponse modifierMessage(@PathVariable Long id,
                                           @RequestBody MessageRequest request) {

        return messageService.modifierMessage(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void supprimerMessage(@PathVariable Long id) {

        messageService.supprimerMessage(id);
    }
}