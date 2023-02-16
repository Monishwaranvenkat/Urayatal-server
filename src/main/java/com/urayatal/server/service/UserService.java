package com.urayatal.server.service;
import com.urayatal.server.dto.LoginDTO;
import com.urayatal.server.dto.RegisterDto;
import com.urayatal.server.dto.TokenDto;
import com.urayatal.server.entity.User;
import com.urayatal.server.repository.UserRepository;
import com.urayatal.server.utilities.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j

public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AblyService ablyService;





    public ApiResponse login(LoginDTO loginDTO) {
        User user = getUserByUserName(loginDTO.getUserName());
        if (user!=null && user.getPassword().equals(loginDTO.getPassword()))
        {
            String token = ablyService.generateToken(loginDTO.getUserName());
            String uuid = UUID.nameUUIDFromBytes(loginDTO.getUserName().getBytes()).toString();
            String channelId = "MessageChannel_"+uuid;
            return ApiResponse.builder()
                    .status("SUCCESS")
                    .result(TokenDto.builder().userName(loginDTO.getUserName()).jwtToken(token).channelId(channelId).build())
                    .build();
        }
        return  ApiResponse.builder()
                .status("FAILED")
                .result("Login Failed")
                .build();
    }

    public ApiResponse register(RegisterDto registerDto)
    {
        User user = User.builder()
                .userName(registerDto.getUserName())
                .password(registerDto.getPassword())
                .sex(registerDto.getSex())
                .email(registerDto.getEmail())
                .dob(registerDto.getDob())
                .build();
        try {
            userRepository.save(user);
            return ApiResponse.builder()
                    .status("SUCCESS")
                    .result("Registered Successfully")
                    .build();
        }catch (Exception e)
        {
            return ApiResponse.builder()
                    .status("FAILED")
                    .result(e.getMessage())
                    .build();
        }
    }

    User getUserByUserName(String userName)
    {
        Optional<User> user = userRepository.findByUserName(userName);
        return user.orElse(null);
    }
}
