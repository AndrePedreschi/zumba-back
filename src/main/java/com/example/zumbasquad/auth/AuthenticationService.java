package com.example.zumbasquad.auth;

import com.example.zumbasquad.config.JwtService;
import com.example.zumbasquad.enums.EnumPapel;
import com.example.zumbasquad.model.Usuario;
import com.example.zumbasquad.repository.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final IUsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
//        var papel = new Papel(1L, EnumPapel.USER);

       var user = Usuario.builder()
               .nome(request.getNome())
               .sobrenome(request.getSobrenome())
               .email(request.getEmail())
               .senha(passwordEncoder.encode(request.getSenha()))
               .papel(EnumPapel.USER)
               .build();
       repository.save(user);
       var jwtToken = jwtService.generateToken(user);
       return AuthenticationResponse.builder()
               .token(jwtToken)
               .build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getSenha()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public Optional<Usuario> getData(String email){
        return repository.findByEmail(email);
    }
}
