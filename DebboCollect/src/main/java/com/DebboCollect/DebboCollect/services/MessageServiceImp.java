package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.Model.ConversationResponse;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MessageServiceImp implements MessageService {

    private final MessageRepository messageRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final MessageMapper messageMapper;

    @Override
    public MessageResponse creerMessage(MessageRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Utilisateur expediteur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

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

        if (roleExpediteur.equals("SUPERVISEUR")
                && roleDestinataire.equals("ENQUETEUR")) {

            if (destinataire.getSuperviseur() == null
                    || !destinataire.getSuperviseur().getId().equals(expediteur.getId())) {

                throw new RuntimeException(
                        "Vous pouvez parler uniquement à vos enquêteurs"
                );
            }
        }

        if (roleExpediteur.equals("ENQUETEUR")
                && roleDestinataire.equals("SUPERVISEUR")) {

            if (expediteur.getSuperviseur() == null
                    || !expediteur.getSuperviseur().getId().equals(destinataire.getId())) {

                throw new RuntimeException(
                        "Vous pouvez parler uniquement à votre superviseur"
                );
            }
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

    @Override
    public List<MessageResponse> afficherConversation(Long utilisateurId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Utilisateur utilisateurConnecte = utilisateurRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur non trouvé"));

        List<Message> messages = messageRepository.trouverConversation(
                utilisateurConnecte.getId(),
                utilisateurId
        );

        for (Message message : messages) {

            if (!message.isLu()
                    && message.getDestinataire().getId().equals(utilisateurConnecte.getId())) {

                message.setLu(true);
                messageRepository.save(message);

                Message verif = messageRepository.findById(message.getId()).orElseThrow();

                System.out.println(
                        "Après save : " + verif.isLu()
                );

            }
        }

        return messages.stream()
                .map(messageMapper::toResponse)
                .toList();
    }

    @Override
    public List<ConversationResponse> afficherConversations() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Utilisateur utilisateurConnecte = utilisateurRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur non trouvé"));

        List<Message> messages =
                messageRepository.findByExpediteurIdOrDestinataireIdOrderByDateEnvoiDesc(
                        utilisateurConnecte.getId(),
                        utilisateurConnecte.getId()
                );

        List<ConversationResponse> conversations = new ArrayList<>();

        Set<Long> dejaAjoutes = new HashSet<>();

        for (Message message : messages) {

            Utilisateur autreUtilisateur;

            if (message.getExpediteur().getId().equals(utilisateurConnecte.getId())) {

                autreUtilisateur = message.getDestinataire();

            } else {

                autreUtilisateur = message.getExpediteur();

            }

            if (!dejaAjoutes.contains(autreUtilisateur.getId())) {

                conversations.add(
                        ConversationResponse.builder()
                                .utilisateurId(autreUtilisateur.getId())
                                .nom(autreUtilisateur.getNom())
                                .role(autreUtilisateur.getRole().name())
                                .dernierMessage(message.getContenu())
                                .build()
                );

                dejaAjoutes.add(autreUtilisateur.getId());

            }

        }

        return conversations;
    }

    @Override
    public Long compterMessagesNonLus() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Utilisateur utilisateur = utilisateurRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur non trouvé"));

        return messageRepository.compterMessagesNonLus(
                utilisateur.getId()
        );
    }
    @Override
    public MessageResponse modifierMessage(Long id, MessageRequest request) {

        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message non trouvé"));

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        Utilisateur expediteur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Expéditeur non trouvé"));

        Utilisateur destinataire = utilisateurRepository.findById(request.getDestinataireId())
                .orElseThrow(() -> new RuntimeException("Destinataire non trouvé"));

        message.setContenu(request.getContenu());
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