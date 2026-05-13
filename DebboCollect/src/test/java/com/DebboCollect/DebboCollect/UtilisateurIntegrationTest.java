package com.DebboCollect.DebboCollect;
import com.DebboCollect.DebboCollect.Model.UtilisateurModel;
import com.DebboCollect.DebboCollect.entity.Role;
import com.DebboCollect.DebboCollect.repository.UtilisateurRepository;
import com.DebboCollect.DebboCollect.services.UtilisateurServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UtilisateurIntegrationTest {
    @Autowired
    private UtilisateurServiceImp utilisateurService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    private UtilisateurModel utilisateurModel;

    @BeforeEach
    void setUp() {

        utilisateurRepository.deleteAll();

        utilisateurModel = new UtilisateurModel();

        utilisateurModel.setNom("Mody");
        utilisateurModel.setEmail("mody@gmail.com");
        utilisateurModel.setMotDePasse("1234");
        utilisateurModel.setRole(Role.ADMIN);
    }

    @Test
    void shouldCreateUser() {

        UtilisateurModel savedUser =
                utilisateurService.createUser(utilisateurModel);

        assertNotNull(savedUser);

        assertEquals("Mody", savedUser.getNom());

        assertEquals(1, utilisateurRepository.count());
    }

    @Test
    void shouldReturnAllUsers() {

        utilisateurService.createUser(utilisateurModel);

        List<UtilisateurModel> users =
                utilisateurService.getAllUsers();

        assertEquals(1, users.size());
    }

    @Test
    void shouldReturnUserById() {

        UtilisateurModel savedUser =
                utilisateurService.createUser(utilisateurModel);

        UtilisateurModel foundUser =
                utilisateurService.getUserById(savedUser.getId());

        assertNotNull(foundUser);

        assertEquals("Mody", foundUser.getNom());
    }

    @Test
    void shouldUpdateUser() {

        UtilisateurModel savedUser =
                utilisateurService.createUser(utilisateurModel);

        utilisateurModel.setNom("Ba Mody");

        UtilisateurModel updatedUser =
                utilisateurService.updateUser(
                        savedUser.getId(),
                        utilisateurModel
                );

        assertEquals("Ba Mody", updatedUser.getNom());
    }

    @Test
    void shouldDeleteUser() {

        UtilisateurModel savedUser =
                utilisateurService.createUser(utilisateurModel);

        utilisateurService.deleteUser(savedUser.getId());

        assertEquals(0, utilisateurRepository.count());
    }
}
