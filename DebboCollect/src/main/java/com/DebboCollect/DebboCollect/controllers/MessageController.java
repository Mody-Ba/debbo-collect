package com.DebboCollect.DebboCollect.controllers;

import com.DebboCollect.DebboCollect.Model.ConversationResponse;
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
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISEUR','ENQUETEUR','BAILLEUR')")
    public MessageResponse creerMessage(@Valid @RequestBody MessageRequest request) {

        return messageService.creerMessage(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISEUR','ENQUETEUR','BAILLEUR')")
    public List<MessageResponse> afficherTousLesMessages() {

        return messageService.afficherTousLesMessages();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISEUR','ENQUETEUR','BAILLEUR')")
    public MessageResponse afficherMessageParId(@PathVariable Long id) {

        return messageService.afficherMessageParId(id);
    }


    @GetMapping("/conversation/{utilisateurId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISEUR','ENQUETEUR','BAILLEUR')")
    public List<MessageResponse> afficherConversation(
            @PathVariable Long utilisateurId
    ) {

        return messageService.afficherConversation(utilisateurId);

    }


    @GetMapping("/conversations")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISEUR','ENQUETEUR','BAILLEUR')")
    public List<ConversationResponse> afficherConversations() {

        return messageService.afficherConversations();

    }

    @GetMapping("/non-lus")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISEUR','ENQUETEUR','BAILLEUR')")
    public Long compterMessagesNonLus() {

        return messageService.compterMessagesNonLus();

    }


}