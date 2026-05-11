package com.DebboCollect.DebboCollect.repository;

import com.DebboCollect.DebboCollect.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
}