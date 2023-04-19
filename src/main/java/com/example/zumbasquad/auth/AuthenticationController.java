package com.example.zumbasquad.auth;

import com.example.zumbasquad.model.auth.AuthenticationRequest;
import com.example.zumbasquad.model.auth.AuthenticationResponse;
import com.example.zumbasquad.model.auth.RegisterRequest;
import com.example.zumbasquad.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(service.login(request));
    }

    @GetMapping("/search")
    public Optional<Usuario> buscarPorEmail(@RequestParam String email){
        return service.getData(email);
    }
}
