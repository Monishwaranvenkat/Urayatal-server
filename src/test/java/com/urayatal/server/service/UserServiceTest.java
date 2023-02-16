package com.urayatal.server.service;

import com.urayatal.server.entity.ChatRoom;
import com.urayatal.server.entity.Message;
import com.urayatal.server.entity.User;
import com.urayatal.server.repository.ChatRoomRepository;
import com.urayatal.server.repository.MessageRepository;
import com.urayatal.server.repository.UserRepository;
import jakarta.annotation.Priority;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
//@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    MessageRepository messageRepository;
    @Test@Order(1)
    void addUser()
    {
        for(int i=0;i<=5;i++)
        {
            User user = User.builder().uid((long) i).userName("User"+i).dob("1998-09-08").sex("Male")
                    .email("test"+i+"@test.com")
                    .password("Pass").build();
            userRepository.save(user);
        }
        assertEquals(6,userRepository.findAll().size());
    }

    @Test@Order(2)
    void addchatRoom()
    {
        User user1 = userRepository.findByUserName("User1").get();
        User user2 = userRepository.findByUserName("User2").get();
        assertNotNull(user1,"User1 is null");
        assertNotNull(user2,"User2 is null");
        ChatRoom chatRoom = ChatRoom.builder().roomId(1L).user1(user1).user2(user2).build();
        chatRoomRepository.save(chatRoom);
        assertEquals(1,chatRoomRepository.findAll().size());
    }

    @Test @Order(3)
    void addMessages()
    {
        //User user1 = userRepository.findById(1L).get();
        ChatRoom chatRoom = chatRoomRepository.findById(1L).get();
        assertNotNull(chatRoom,"chatRoom is null");
        for (int i = 2; i < 100; i++) {
            Message message = Message.builder()
                    .msgId((long) i)
                    .messageTo("user1")
                    .chatRoom(chatRoom)
                    .status("UnRead")
                    .type("Text")
                    .data("Hi Bro..")
                    .timestamp(new Timestamp(System.currentTimeMillis()).toString())
                    .build();
            messageRepository.save(message);
        }

    }

    @Test
    void test()
    {
        Optional<ChatRoom> chatRoom =chatRoomRepository.findById(189L);
        if(chatRoom.isPresent())
            log.info("isPresent");
        if(chatRoom.isEmpty())
            log.info("isEmpty");

    }



}