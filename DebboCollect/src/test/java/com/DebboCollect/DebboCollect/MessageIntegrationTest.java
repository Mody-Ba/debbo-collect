package com.DebboCollect.DebboCollect;
import com.DebboCollect.DebboCollect.Model.MessageRequest;
import com.DebboCollect.DebboCollect.Model.MessageResponse;
import com.DebboCollect.DebboCollect.entity.Role;
import com.DebboCollect.DebboCollect.entity.Utilisateur;
import com.DebboCollect.DebboCollect.repository.MessageRepository;
import com.DebboCollect.DebboCollect.repository.UtilisateurRepository;
import com.DebboCollect.DebboCollect.services.MessageServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class MessageIntegrationTest {
    @Autowired
    private MessageServiceImp messageService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    private MessageRequest messageRequest;

    private Utilisateur expediteur;

    private Utilisateur destinataire;

    @BeforeEach
    void setUp() {

        messageRepository.deleteAll();
        utilisateurRepository.deleteAll();

        expediteur = new Utilisateur();

        expediteur.setNom("Mody");
        expediteur.setEmail("mody@gmail.com");
        expediteur.setMotDePasse("1234");
        expediteur.setRole(Role.ADMIN);

        expediteur = utilisateurRepository.save(expediteur);

        destinataire = new Utilisateur();

        destinataire.setNom("Ba");
        destinataire.setEmail("ba@gmail.com");
        destinataire.setMotDePasse("1234");
        destinataire.setRole(Role.ADMIN);

        destinataire = utilisateurRepository.save(destinataire);

        messageRequest = new MessageRequest();

        messageRequest.setContenu("Bonjour");
        messageRequest.setLu(false);
        messageRequest.setExpediteurId(expediteur.getId());
        messageRequest.setDestinataireId(destinataire.getId());
    }

    @Test
    void shouldCreateMessage() {

        MessageResponse savedMessage =
                messageService.creerMessage(messageRequest);

        assertNotNull(savedMessage);

        assertEquals(
                "Bonjour",
                savedMessage.getContenu()
        );

        assertEquals(1, messageRepository.count());
    }

    @Test
    void shouldReturnAllMessages() {

        messageService.creerMessage(messageRequest);

        List<MessageResponse> messages =
                messageService.afficherTousLesMessages();

        assertEquals(1, messages.size());
    }

    @Test
    void shouldReturnMessageById() {

        MessageResponse savedMessage =
                messageService.creerMessage(messageRequest);

        MessageResponse foundMessage =
                messageService.afficherMessageParId(savedMessage.getId());

        assertNotNull(foundMessage);

        assertEquals(
                "Bonjour",
                foundMessage.getContenu()
        );
    }

    @Test
    void shouldUpdateMessage() {

        MessageResponse savedMessage =
                messageService.creerMessage(messageRequest);

        messageRequest.setContenu("Bonsoir");

        MessageResponse updatedMessage =
                messageService.modifierMessage(
                        savedMessage.getId(),
                        messageRequest
                );

        assertEquals(
                "Bonsoir",
                updatedMessage.getContenu()
        );
    }

    @Test
    void shouldDeleteMessage() {

        MessageResponse savedMessage =
                messageService.creerMessage(messageRequest);

        messageService.supprimerMessage(savedMessage.getId());

        assertEquals(0, messageRepository.count());
    }
}
