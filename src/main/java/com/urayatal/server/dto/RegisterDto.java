package com.urayatal.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto implements Serializable {
    private String userName;
    private String email;
    private String password;
    private String sex;
    private String dob;
}
