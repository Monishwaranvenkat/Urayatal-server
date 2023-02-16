package com.urayatal.server.controller;


import com.urayatal.server.dto.LoginDTO;
import com.urayatal.server.dto.RegisterDto;
import com.urayatal.server.service.UserService;
import com.urayatal.server.utilities.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@Slf4j
public class AuthenticationController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginDTO loginDTO)
    {
        return ResponseEntity.ok(userService.login(loginDTO));
    }
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterDto registerDto)
    {
        return ResponseEntity.ok(userService.register(registerDto));
    }
}
