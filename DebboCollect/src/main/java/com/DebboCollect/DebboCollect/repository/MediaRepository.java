package com.DebboCollect.DebboCollect.repository;

import com.DebboCollect.DebboCollect.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {

    List<Media> findByReponseId(Long reponseId);
}