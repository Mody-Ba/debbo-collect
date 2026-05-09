package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.Model.UtilisateurModel;

import java.util.List;

public interface UtilisateurService {

    UtilisateurModel createUser(UtilisateurModel model);

    List<UtilisateurModel> getAllUsers();

    UtilisateurModel getUserById(Long id);

    UtilisateurModel updateUser(Long id, UtilisateurModel model);

    void deleteUser(Long id);

}
