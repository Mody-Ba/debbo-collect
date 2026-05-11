package com.DebboCollect.DebboCollect.repository;

import com.DebboCollect.DebboCollect.entity.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjetRepository extends JpaRepository<Projet, Long> {
}
