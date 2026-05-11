package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.Model.MessageRequest;
import com.DebboCollect.DebboCollect.Model.MessageResponse;
import com.DebboCollect.DebboCollect.entity.Message;
import com.DebboCollect.DebboCollect.entity.Utilisateur;
import com.DebboCollect.DebboCollect.mappers.MessageMapper;
import com.DebboCollect.DebboCollect.repository.MessageRepository;
import com.DebboCollect.DebboCollect.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImp implements MessageService {

    private final MessageRepository messageRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final MessageMapper messageMapper;

    @Override
    public MessageResponse creerMessage(MessageRequest request) {

        Utilisateur expediteur = utilisateurRepository.findById(request.getExpediteurId())
                .orElseThrow(() -> new RuntimeException("Expéditeur non trouvé"));

        Utilisateur destinataire = utilisateurRepository.findById(request.getDestinataireId())
                .orElseThrow(() -> new RuntimeException("Destinataire non trouvé"));

        Message message = messageMapper.toEntity(request, expediteur, destinataire);

        Message savedMessage = messageRepository.save(message);

        return messageMapper.toResponse(savedMessage);
    }

    @Override
    public List<MessageResponse> afficherTousLesMessages() {

        return messageRepository.findAll()
                .stream()
                .map(messageMapper::toResponse)
                .toList();
    }

    @Override
    public MessageResponse afficherMessageParId(Long id) {

        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message non trouvé"));

        return messageMapper.toResponse(message);
    }

    @Override
    public MessageResponse modifierMessage(Long id, MessageRequest request) {

        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message non trouvé"));

        Utilisateur expediteur = utilisateurRepository.findById(request.getExpediteurId())
                .orElseThrow(() -> new RuntimeException("Expéditeur non trouvé"));

        Utilisateur destinataire = utilisateurRepository.findById(request.getDestinataireId())
                .orElseThrow(() -> new RuntimeException("Destinataire non trouvé"));

        message.setContenu(request.getContenu());
        message.setLu(request.isLu());
        message.setExpediteur(expediteur);
        message.setDestinataire(destinataire);

        Message updatedMessage = messageRepository.save(message);

        return messageMapper.toResponse(updatedMessage);
    }

    @Override
    public void supprimerMessage(Long id) {

        messageRepository.deleteById(id);
    }
}