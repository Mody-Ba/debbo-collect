package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.Model.MessageRequest;
import com.DebboCollect.DebboCollect.Model.MessageResponse;
import com.DebboCollect.DebboCollect.entity.Message;
import com.DebboCollect.DebboCollect.entity.Utilisateur;
import com.DebboCollect.DebboCollect.mappers.MessageMapper;
import com.DebboCollect.DebboCollect.repository.MessageRepository;
import com.DebboCollect.DebboCollect.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

        String roleExpediteur = expediteur.getRole().name();

        String roleDestinataire = destinataire.getRole().name();

        if (roleExpediteur.equals("ADMIN")
                && !roleDestinataire.equals("SUPERVISEUR")) {

            throw new RuntimeException("Admin peut parler seulement au superviseur");
        }

        if (roleExpediteur.equals("BAILLEUR")
                && !roleDestinataire.equals("SUPERVISEUR")) {

            throw new RuntimeException("Bailleur peut parler seulement au superviseur");
        }

        if (roleExpediteur.equals("ENQUETEUR")
                && !roleDestinataire.equals("SUPERVISEUR")) {

            throw new RuntimeException("Enqueteur peut parler seulement au superviseur");
        }

        Message message = messageMapper.toEntity(request, expediteur, destinataire);

        Message savedMessage = messageRepository.save(message);

        return messageMapper.toResponse(savedMessage);
    }

    @Override
    public List<MessageResponse> afficherTousLesMessages() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        return messageRepository.findAll()
                .stream()
                .filter(message ->
                        message.getExpediteur().getEmail().equals(email)
                                ||
                                message.getDestinataire().getEmail().equals(email)
                )
                .map(messageMapper::toResponse)
                .toList();
    }
    @Override
    public MessageResponse afficherMessageParId(Long id) {

        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message non trouvé"));

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        if (!message.getExpediteur().getEmail().equals(email)
                &&
                !message.getDestinataire().getEmail().equals(email)) {

            throw new RuntimeException("Accès interdit");
        }

        return messageMapper.toResponse(message);
    }

}