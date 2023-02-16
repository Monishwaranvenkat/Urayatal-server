package com.urayatal.server.repository;

import com.urayatal.server.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByChatRoom(String chatRoom, Pageable pageable);
}