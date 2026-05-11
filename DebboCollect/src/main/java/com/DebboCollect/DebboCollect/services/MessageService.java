package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.Model.MessageRequest;
import com.DebboCollect.DebboCollect.Model.MessageResponse;

import java.util.List;

public interface MessageService {

    MessageResponse creerMessage(MessageRequest request);

    List<MessageResponse> afficherTousLesMessages();

    MessageResponse afficherMessageParId(Long id);

    MessageResponse modifierMessage(Long id, MessageRequest request);

    void supprimerMessage(Long id);
}