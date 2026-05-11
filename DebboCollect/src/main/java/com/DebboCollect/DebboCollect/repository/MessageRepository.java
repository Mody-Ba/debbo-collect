package com.DebboCollect.DebboCollect.repository;

import com.DebboCollect.DebboCollect.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}