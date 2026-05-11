package com.DebboCollect.DebboCollect.repository;

import com.DebboCollect.DebboCollect.entity.Champ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChampRepository extends JpaRepository<Champ, Long> {
}