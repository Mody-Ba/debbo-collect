package com.DebboCollect.DebboCollect;
import com.DebboCollect.DebboCollect.Model.MessageRequest;
import com.DebboCollect.DebboCollect.Model.MessageResponse;
import com.DebboCollect.DebboCollect.entity.Message;
import com.DebboCollect.DebboCollect.entity.Utilisateur;
import com.DebboCollect.DebboCollect.mappers.MessageMapper;
import com.DebboCollect.DebboCollect.repository.MessageRepository;
import com.DebboCollect.DebboCollect.repository.UtilisateurRepository;
import com.DebboCollect.DebboCollect.services.MessageServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceImpTest {
    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private MessageMapper messageMapper;

    @InjectMocks
    private MessageServiceImp messageService;

    private Message message;
    private MessageRequest messageRequest;
    private MessageResponse messageResponse;
    private Utilisateur expediteur;
    private Utilisateur destinataire;

    @BeforeEach
    void setUp() {

        expediteur = new Utilisateur();
        expediteur.setId(1L);

        destinataire = new Utilisateur();
        destinataire.setId(2L);

        message = new Message();
        message.setId(1L);
        message.setContenu("Bonjour");
        message.setDateEnvoi(new Date());
        message.setLu(false);
        message.setExpediteur(expediteur);
        message.setDestinataire(destinataire);

        messageRequest = new MessageRequest();
        messageRequest.setContenu("Bonjour");
        messageRequest.setLu(false);
        messageRequest.setExpediteurId(1L);
        messageRequest.setDestinataireId(2L);

        messageResponse = new MessageResponse();
        messageResponse.setId(1L);
        messageResponse.setContenu("Bonjour");
        messageResponse.setLu(false);
    }

    @Test
    void shouldCreateMessage() {

        when(utilisateurRepository.findById(1L))
                .thenReturn(Optional.of(expediteur));

        when(utilisateurRepository.findById(2L))
                .thenReturn(Optional.of(destinataire));

        when(messageMapper.toEntity(messageRequest, expediteur, destinataire))
                .thenReturn(message);

        when(messageRepository.save(message))
                .thenReturn(message);

        when(messageMapper.toResponse(message))
                .thenReturn(messageResponse);

        MessageResponse result =
                messageService.creerMessage(messageRequest);

        assertEquals("Bonjour", result.getContenu());
    }

    @Test
    void shouldReturnAllMessages() {

        when(messageRepository.findAll())
                .thenReturn(List.of(message));

        when(messageMapper.toResponse(message))
                .thenReturn(messageResponse);

        List<MessageResponse> result =
                messageService.afficherTousLesMessages();

        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnMessageById() {

        when(messageRepository.findById(1L))
                .thenReturn(Optional.of(message));

        when(messageMapper.toResponse(message))
                .thenReturn(messageResponse);

        MessageResponse result =
                messageService.afficherMessageParId(1L);

        assertEquals("Bonjour", result.getContenu());
    }

    @Test
    void shouldUpdateMessage() {

        when(messageRepository.findById(1L))
                .thenReturn(Optional.of(message));

        when(utilisateurRepository.findById(1L))
                .thenReturn(Optional.of(expediteur));

        when(utilisateurRepository.findById(2L))
                .thenReturn(Optional.of(destinataire));

        when(messageRepository.save(message))
                .thenReturn(message);

        when(messageMapper.toResponse(message))
                .thenReturn(messageResponse);

        MessageResponse result =
                messageService.modifierMessage(1L, messageRequest);

        assertEquals("Bonjour", result.getContenu());
    }

    @Test
    void shouldDeleteMessage() {

        doNothing().when(messageRepository)
                .deleteById(1L);

        messageService.supprimerMessage(1L);

        verify(messageRepository, times(1))
                .deleteById(1L);
    }

}
