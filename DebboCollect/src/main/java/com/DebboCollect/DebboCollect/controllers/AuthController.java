package com.DebboCollect.DebboCollect.controllers;

import com.DebboCollect.DebboCollect.Model.JwtResponse;
import com.DebboCollect.DebboCollect.Model.LoginRequest;
import com.DebboCollect.DebboCollect.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(
                authService.login(request)
        );
    }
}