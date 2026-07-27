package com.DebboCollect.DebboCollect.repository;

import com.DebboCollect.DebboCollect.entity.LotCollect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotCollecteRepository extends JpaRepository<LotCollect, Long> {
}
