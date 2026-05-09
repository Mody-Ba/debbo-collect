package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.Model.UtilisateurModel;
import com.DebboCollect.DebboCollect.entity.Utilisateur;
import com.DebboCollect.DebboCollect.mappers.UtilisateurMapper;
import com.DebboCollect.DebboCollect.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilisateurServiceImp implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurServiceImp(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public UtilisateurModel createUser(UtilisateurModel model) {

        Utilisateur utilisateur = UtilisateurMapper.toEntity(model);

        utilisateur = utilisateurRepository.save(utilisateur);

        return UtilisateurMapper.toModel(utilisateur);
    }

    @Override
    public List<UtilisateurModel> getAllUsers() {

        return utilisateurRepository.findAll()
                .stream()
                .map(UtilisateurMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public UtilisateurModel getUserById(Long id) {

        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        return UtilisateurMapper.toModel(utilisateur);
    }

    @Override
    public UtilisateurModel updateUser(Long id, UtilisateurModel model) {

        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        utilisateur.setNom(model.getNom());
        utilisateur.setEmail(model.getEmail());
        utilisateur.setMotDePasse(model.getMotDePasse());
        utilisateur.setRole(model.getRole());

        utilisateur = utilisateurRepository.save(utilisateur);

        return UtilisateurMapper.toModel(utilisateur);
    }

    @Override
    public void deleteUser(Long id) {

        utilisateurRepository.deleteById(id);
    }
}
