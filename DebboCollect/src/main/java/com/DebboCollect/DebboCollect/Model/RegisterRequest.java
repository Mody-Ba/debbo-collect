package com.DebboCollect.DebboCollect.Model;

import com.DebboCollect.DebboCollect.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    private String nom;

    private String email;

    private String password;

    private Role role;
}