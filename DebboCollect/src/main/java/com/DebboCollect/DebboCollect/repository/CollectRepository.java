package com.DebboCollect.DebboCollect.repository;

import com.DebboCollect.DebboCollect.entity.Collecte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectRepository extends JpaRepository<Collecte, Long> {
}