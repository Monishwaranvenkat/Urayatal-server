package com.urayatal.server.controller;

import com.urayatal.server.dto.MessageDto;
import com.urayatal.server.service.ChatService;
import com.urayatal.server.utilities.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin

@RestController
@Slf4j
public class ChatController {


    @Autowired
    ChatService chatService;



    @GetMapping("/test")
    public ResponseEntity<ApiResponse> testMethod(@RequestParam int pageNo) {
        return ResponseEntity.ok(ApiResponse.builder().status("Retrieved").result(null).build());
    }

    @GetMapping("/getChats")
    public ResponseEntity<ApiResponse> getChats(@RequestParam String userName) {
        return ResponseEntity.ok(chatService.getUserChats(userName));
    }

    @PostMapping("/sendMessage")
    public ResponseEntity<ApiResponse> sendMessage(@RequestBody MessageDto message) {
        return ResponseEntity.ok(chatService.sendMessage(message));
    }

//    @GetMapping("/getAllUsers")
//    public ResponseEntity<ApiResponse> getAllUsers()
//    {
//        return ResponseEntity.ok(chatService.getAllUsers());
//    }



}
