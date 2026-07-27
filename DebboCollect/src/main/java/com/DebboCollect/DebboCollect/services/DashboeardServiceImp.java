package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.Model.DashboardResponse;
import com.DebboCollect.DebboCollect.entity.StatusCollect;
import com.DebboCollect.DebboCollect.repository.CollectRepository;
import com.DebboCollect.DebboCollect.repository.ProjetRepository;
import com.DebboCollect.DebboCollect.repository.ReponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboeardServiceImp implements DashboardService{

    private final ProjetRepository projetRepository;
    private final CollectRepository collecteRepository;
    private final ReponseRepository reponseRepository;

    @Override
    public DashboardResponse getDashboard() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String role = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        String email = authentication.getName();


        if (role.equals("ROLE_BAILLEUR")) {

            long nombreProjets = projetRepository.findAll()
                    .stream()
                    .filter(p -> p.getBailleur().getEmail().equals(email))
                    .count();

            long nombreCollectes = collecteRepository.findAll()
                    .stream()
                    .filter(c -> c.getProjet().getBailleur().getEmail().equals(email))
                    .filter(c -> c.getStatut() == StatusCollect.VALIDEE)
                    .count();

            long nombreReponses = reponseRepository.findAll()
                    .stream()
                    .filter(r -> r.getCollecte().getProjet().getBailleur().getEmail().equals(email))
                    .filter(r -> r.getCollecte().getStatut() == StatusCollect.VALIDEE)
                    .count();

            long validees = collecteRepository.findAll()
                    .stream()
                    .filter(c -> c.getProjet().getBailleur().getEmail().equals(email))
                    .filter(c -> c.getStatut() == StatusCollect.VALIDEE)
                    .count();

            long attente = collecteRepository.findAll()
                    .stream()
                    .filter(c -> c.getProjet().getBailleur().getEmail().equals(email))
                    .filter(c -> c.getStatut() == StatusCollect.EN_ATTENTE)
                    .count();

            long rejetees = collecteRepository.findAll()
                    .stream()
                    .filter(c -> c.getProjet().getBailleur().getEmail().equals(email))
                    .count();

            return DashboardResponse.builder()
                    .nombreProjets(nombreProjets)
                    .nombreCollectes(nombreCollectes)
                    .nombreReponses(nombreReponses)
                    .collectesValidees(validees)
                    .collectesEnAttente(attente)

                    .build();
        }



        long nombreProjets = projetRepository.findAll()
                .stream()
                .filter(p -> p.getSuperviseur().getEmail().equals(email))
                .count();

        long nombreCollectes = collecteRepository.findAll()
                .stream()
                .filter(c -> c.getProjet().getSuperviseur().getEmail().equals(email))
                .count();

        long nombreReponses = reponseRepository.findAll()
                .stream()
                .filter(r -> r.getCollecte().getProjet().getSuperviseur().getEmail().equals(email))
                .count();


        long validees = collecteRepository.findAll()
                .stream()
                .filter(c -> c.getProjet().getSuperviseur().getEmail().equals(email))
                .filter(c -> c.getStatut() == StatusCollect.VALIDEE)
                .count();

        long attente = collecteRepository.findAll()
                .stream()
                .filter(c -> c.getProjet().getSuperviseur().getEmail().equals(email))
                .filter(c -> c.getStatut() == StatusCollect.EN_ATTENTE)
                .count();

        long rejetees = collecteRepository.findAll()
                .stream()
                .filter(c -> c.getProjet().getSuperviseur().getEmail().equals(email))
                .count();

        return DashboardResponse.builder()
                .nombreProjets(nombreProjets)
                .nombreCollectes(nombreCollectes)
                .nombreReponses(nombreReponses)
                .collectesValidees(validees)
                .collectesEnAttente(attente)

                .build();
    }

}
