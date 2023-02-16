package com.urayatal.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @Column(name = "UserId",unique = true,nullable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="User_SEQ_Gen")
    @SequenceGenerator(name="User_SEQ_Gen", sequenceName="User_SEQ",allocationSize = 50)
    private Long uid;
    @Column(name = "userName",unique = true,nullable = false)
    private String userName;
    @Column(name = "email",unique = true,nullable = false)
    private String email;
    @Column(name = "password",nullable = false)
    private String password;
    @Column(name = "sex",nullable = false)
    private String sex;
    @Column(name = "DOB",nullable = false)
    private String dob;
}
