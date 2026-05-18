package com.DebboCollect.DebboCollect.controller;

import com.DebboCollect.DebboCollect.Model.JwtResponse;
import com.DebboCollect.DebboCollect.Model.LoginRequest;
import com.DebboCollect.DebboCollect.Model.RegisterRequest;
import com.DebboCollect.DebboCollect.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(

            @RequestBody
            RegisterRequest request
    ) {

        return ResponseEntity.ok(

                authService.register(request)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(

            @RequestBody
            LoginRequest request
    ) {

        return ResponseEntity.ok(

                authService.login(request)
        );
    }
}