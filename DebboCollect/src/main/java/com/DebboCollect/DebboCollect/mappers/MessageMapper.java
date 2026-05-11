package com.DebboCollect.DebboCollect.mappers;

import com.DebboCollect.DebboCollect.Model.MessageModel;
import com.DebboCollect.DebboCollect.Model.MessageRequest;
import com.DebboCollect.DebboCollect.Model.MessageResponse;
import com.DebboCollect.DebboCollect.entity.Message;
import com.DebboCollect.DebboCollect.entity.Utilisateur;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MessageMapper {

    public Message toEntity(MessageRequest request,
                            Utilisateur expediteur,
                            Utilisateur destinataire) {

        return Message.builder()
                .contenu(request.getContenu())
                .lu(request.isLu())
                .dateEnvoi(new Date())
                .expediteur(expediteur)
                .destinataire(destinataire)
                .build();
    }

    public MessageResponse toResponse(Message message) {

        return MessageResponse.builder()
                .id(message.getId())
                .contenu(message.getContenu())
                .dateEnvoi(message.getDateEnvoi())
                .lu(message.isLu())
                .expediteurId(message.getExpediteur().getId())
                .destinataireId(message.getDestinataire().getId())
                .build();
    }

    public MessageModel toModel(Message message) {

        return MessageModel.builder()
                .id(message.getId())
                .contenu(message.getContenu())
                .dateEnvoi(message.getDateEnvoi())
                .lu(message.isLu())
                .expediteurId(message.getExpediteur().getId())
                .destinataireId(message.getDestinataire().getId())
                .build();
    }
}