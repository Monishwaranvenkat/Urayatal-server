package com.urayatal.server.service;

import com.urayatal.server.dto.ChatRoomDto;
import com.urayatal.server.dto.MessageDto;
import com.urayatal.server.entity.ChatRoom;
import com.urayatal.server.entity.Message;
import com.urayatal.server.entity.User;
import com.urayatal.server.repository.ChatRoomRepository;
import com.urayatal.server.repository.MessageRepository;
import com.urayatal.server.utilities.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChatService {

    @Autowired
    UserService userService;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    ChatRoomRepository chatRoomRepository;
    @Autowired
    AblyService ablyService;

    public void saveMessage(Message message) {
        if (message.getChatRoom() == null) {
            User user1 = userService.getUserByUserName(message.getMessageTo());
            User user2 = userService.getUserByUserName(message.getMessageTo());
            ChatRoom newChatRoom = ChatRoom.builder().roomId(1L).user1(user1).user2(user2).build();
            message.setChatRoom(newChatRoom);
        }
        messageRepository.save(message);
    }


    public ApiResponse getUserChats(String userName) {
        User user = userService.getUserByUserName(userName);
        if (user != null) {
            return ApiResponse.builder()
                    .status("SUCCESS")
                    .result(getChatRoomsOfUser(user))
                    .build();
        }

        return ApiResponse.builder()
                .status("Failed")
                .result("User not found")
                .build();
    }

    private List<ChatRoomDto> getChatRoomsOfUser(User user) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByUser(user);
        return chatRooms.stream().map(this::chatRoomDtoConvertor).collect(Collectors.toList());
    }

    public ApiResponse sendMessage(MessageDto messageDto) {
        log.info("Entered ChatService:sendMessage");
        try {
            if (messageDto.getChatRoomRoomId() != null) {
                log.info("ChatroomId  present in message DTO");
                Optional<ChatRoom> chatRoom = chatRoomRepository.findById(messageDto.getChatRoomRoomId());
                if (chatRoom.isPresent()) {
                    log.info("Chatroom is  present in table");
                    Message message = messageEntityConvertor(messageDto, chatRoom.get());
                    Long msgID = messageRepository.save(message).getMsgId();
                    messageDto.setMsgId(msgID);
                } else {
                    return ApiResponse.builder().status("Failed").result("ChatroomId is Invalid").build();
                }
            }else {
                log.info("Chatroom is notpresent in table");
                Optional<ChatRoom> chatRoom = chatRoomRepository.findChatRoomByUserName(messageDto.getMessageFrom(), messageDto.getMessageTo());
                if (chatRoom.isPresent()) {
                    log.info("Chatroom is present in table while search using uname");
                    Message message = messageEntityConvertor(messageDto, chatRoom.get());
                    Long msgID = messageRepository.save(message).getMsgId();
                    messageDto.setChatRoomRoomId(chatRoom.get().getRoomId());
                    messageDto.setMsgId(msgID);
                } else {
                    log.info("Chatroom is not present in table while search using uname");
                    ChatRoom newChatRoom = chatRoomRepository.save(createNewChatRoom(messageDto.getMessageFrom(), messageDto.getMessageTo()));
                    Message message = messageEntityConvertor(messageDto, newChatRoom);
                    Long msgID = messageRepository.save(message).getMsgId();
                    messageDto.setMsgId(msgID);
                    messageDto.setChatRoomRoomId(newChatRoom.getRoomId());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResponse.builder().status("Failed").result("Something went wrong").build();
        }
        if (ablyService.getPresence(messageDto.getMessageTo())) {
            ablyService.publishMessageToUser(messageDto);
        }
        log.info("Leaving ChatService:sendMessage");
        return ApiResponse.builder().status("SUCCESS").result(messageDto).build();
    }

    private ChatRoom createNewChatRoom(String userName1, String userName2) throws DuplicateKeyException,NullPointerException {
        log.info("IN ChatService:createNewChatRoom");
        if(userName1.equals(userName2))
            throw new DuplicateKeyException("Both userNames are same");
        User user1 = userService.getUserByUserName(userName1);
        User user2 = userService.getUserByUserName(userName2);
        return ChatRoom.builder()
                .user1(user1)
                .user2(user2)
                .build();
    }

    private Message messageEntityConvertor(MessageDto messageDto, ChatRoom chatRoom) {
        log.info("IN ChatService:messageEntityConvertor");
        if (messageDto == null || chatRoom == null)
            throw new NullPointerException("Object is null");

        return Message.builder()
                .msgId(messageDto.getMsgId())
                .type(messageDto.getType())
                .data(messageDto.getData())
                .timestamp(messageDto.getTimestamp())
                .messageFrom(messageDto.getMessageFrom())
                .messageTo(messageDto.getMessageTo())
                .status(messageDto.getStatus())
                .chatRoom(chatRoom)
                .build();
    }


    private MessageDto messageDtoConvertor(Message message) throws NullPointerException {
        log.info("IN ChatService:messageDtoConvertor");
        if (message == null)
            throw new NullPointerException("Message Object is null");
        return MessageDto.builder()
                .msgId(message.getMsgId())
                .data(message.getData())
                .type(message.getType())
                .chatRoomRoomId(message.getChatRoom().getRoomId())
                .messageFrom(message.getMessageFrom())
                .messageTo(message.getMessageTo())
                .status(message.getStatus())
                .timestamp(message.getTimestamp())
                .build();
    }

    private ChatRoomDto chatRoomDtoConvertor(ChatRoom chatRoom) throws NullPointerException {
        log.info("IN ChatService:messageDtoConvertor");
        if (chatRoom == null) {
            throw new NullPointerException("ChatRoom Object is null");
        }

        return ChatRoomDto.builder()
                .roomId(chatRoom.getRoomId())
                .messages(chatRoom.getMessages().stream().map(this::messageDtoConvertor).collect(Collectors.toList()))
                .user1UserName(chatRoom.getUser1().getUserName())
                .user2UserName(chatRoom.getUser2().getUserName())
                .unreadMessage(chatRoom.getUnreadMessage())
                .build();

    }

}
