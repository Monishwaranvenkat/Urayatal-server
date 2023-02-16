package com.urayatal.server.repository;

import com.urayatal.server.entity.ChatRoom;
import com.urayatal.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {

    List<ChatRoom> findAllByUser(User user);
    Optional<ChatRoom> findChatRoomByUserName(String user1,String user2);
}
