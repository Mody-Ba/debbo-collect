package com.DebboCollect.DebboCollect.mappers;

import com.DebboCollect.DebboCollect.Model.UtilisateurModel;
import com.DebboCollect.DebboCollect.Model.UtilisateurRequest;
import com.DebboCollect.DebboCollect.Model.UtilisateurResponse;
import com.DebboCollect.DebboCollect.entity.Utilisateur;

public class UtilisateurMapper {

    public static UtilisateurModel toModel(UtilisateurRequest request) {

        UtilisateurModel model = new UtilisateurModel();

        model.setNom(request.getNom());
        model.setEmail(request.getEmail());
        model.setPassword(request.getPassword());
        model.setRole(request.getRole());

        model.setCompteActif(request.getCompteActif());

        return model;
    }

    public static Utilisateur toEntity(UtilisateurModel model) {

        Utilisateur utilisateur = new Utilisateur();

        utilisateur.setId(model.getId());
        utilisateur.setNom(model.getNom());
        utilisateur.setEmail(model.getEmail());
        utilisateur.setPassword(model.getPassword());
        utilisateur.setRole(model.getRole());
        utilisateur.setCompteActif(model.getCompteActif());

        return utilisateur;
    }

    public static UtilisateurModel toModel(Utilisateur entity) {

        UtilisateurModel model = new UtilisateurModel();

        model.setId(entity.getId());
        model.setNom(entity.getNom());
        model.setEmail(entity.getEmail());
        model.setPassword(entity.getPassword());
        model.setRole(entity.getRole());
        model.setCompteActif(entity.getCompteActif());

        return model;
    }

    public static UtilisateurResponse toResponse(UtilisateurModel model) {

        UtilisateurResponse response = new UtilisateurResponse();

        response.setId(model.getId());
        response.setNom(model.getNom());
        response.setEmail(model.getEmail());
        response.setRole(model.getRole());
        response.setCompteActif(model.getCompteActif());

        return response;
    }
}
/*Ce mapper fait 4 transformations
Méthode	Transformation
toModel(request)	Request → Model
toEntity(model)	Model → Entity
toModel(entity)	Entity → Model
toResponse(model)	Model → Response

Architecture:
Request
↓
Model
↓
Entity
↓
Database

Puis retour :

Entity
↓
Model
↓
Response*/
