package com.DebboCollect.DebboCollect;
import com.DebboCollect.DebboCollect.entity.Role;
import com.DebboCollect.DebboCollect.entity.Utilisateur;
import com.DebboCollect.DebboCollect.Model.UtilisateurModel;
import com.DebboCollect.DebboCollect.repository.UtilisateurRepository;
import com.DebboCollect.DebboCollect.services.UtilisateurServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)

public class UtilisateurServiceImpTest {
    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private UtilisateurServiceImp utilisateurService;

    private Utilisateur utilisateur;
    private UtilisateurModel utilisateurModel;

    @BeforeEach
    void setUp() {

        utilisateur = new Utilisateur();

        utilisateur.setId(1L);
        utilisateur.setNom("Mody");
        utilisateur.setEmail("mody@gmail.com");
        utilisateur.setMotDePasse("1234");

        utilisateurModel = new UtilisateurModel();

        utilisateurModel.setId(1L);
        utilisateurModel.setNom("Mody");
        utilisateurModel.setEmail("mody@gmail.com");
        utilisateurModel.setMotDePasse("1234");
    }

    @Test
    void shouldReturnAllUsers() {

        when(utilisateurRepository.findAll())
                .thenReturn(List.of(utilisateur));

        List<UtilisateurModel> result =
                utilisateurService.getAllUsers();

        assertEquals(1, result.size());

        assertEquals("Mody",
                result.get(0).getNom());
    }

    @Test
    void shouldReturnUserById() {

        when(utilisateurRepository.findById(1L))
                .thenReturn(Optional.of(utilisateur));

        UtilisateurModel result =
                utilisateurService.getUserById(1L);

        assertEquals("Mody", result.getNom());
    }

    @Test
    void shouldCreateUser() {

        when(utilisateurRepository.save(any(Utilisateur.class)))
                .thenReturn(utilisateur);

        UtilisateurModel result =
                utilisateurService.createUser(utilisateurModel);

        assertEquals("Mody", result.getNom());
    }

    @Test
    void shouldUpdateUser() {

        when(utilisateurRepository.findById(1L))
                .thenReturn(Optional.of(utilisateur));

        when(utilisateurRepository.save(any(Utilisateur.class)))
                .thenReturn(utilisateur);

        utilisateurModel.setNom("Ali");

        UtilisateurModel result =
                utilisateurService.updateUser(1L, utilisateurModel);

        assertEquals("Ali", result.getNom());
    }

    @Test
    void shouldDeleteUser() {

        doNothing().when(utilisateurRepository)
                .deleteById(1L);

        utilisateurService.deleteUser(1L);

        verify(utilisateurRepository, times(1))
                .deleteById(1L);
    }
}
