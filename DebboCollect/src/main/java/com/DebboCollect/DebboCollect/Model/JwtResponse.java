package com.DebboCollect.DebboCollect.Model;

import lombok.Getter;

@Getter
public class JwtResponse {

    private String token;

    private String type = "Bearer";

    private String email;

    public JwtResponse(
            String token,
            String email
    ) {

        this.token = token;

        this.email = email;
    }
}