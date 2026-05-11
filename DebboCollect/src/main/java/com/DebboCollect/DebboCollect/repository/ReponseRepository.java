package com.DebboCollect.DebboCollect.repository;

import com.DebboCollect.DebboCollect.entity.Reponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReponseRepository extends JpaRepository<Reponse, Long> {
}