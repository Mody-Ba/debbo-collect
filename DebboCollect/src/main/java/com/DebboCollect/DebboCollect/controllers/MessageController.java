package com.DebboCollect.DebboCollect.controllers;

import com.DebboCollect.DebboCollect.Model.MessageRequest;
import com.DebboCollect.DebboCollect.Model.MessageResponse;
import com.DebboCollect.DebboCollect.services.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public MessageResponse creerMessage(@Valid @RequestBody MessageRequest request) {

        return messageService.creerMessage(request);
    }

    @GetMapping
    public List<MessageResponse> afficherTousLesMessages() {

        return messageService.afficherTousLesMessages();
    }

    @GetMapping("/{id}")
    public MessageResponse afficherMessageParId(@PathVariable Long id) {

        return messageService.afficherMessageParId(id);
    }

    @PutMapping("/{id}")
    public MessageResponse modifierMessage(@PathVariable Long id,
                                           @RequestBody MessageRequest request) {

        return messageService.modifierMessage(id, request);
    }

    @DeleteMapping("/{id}")
    public void supprimerMessage(@PathVariable Long id) {

        messageService.supprimerMessage(id);
    }
}