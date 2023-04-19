package com.example.zumbasquad.auth;

import com.example.zumbasquad.enums.EnumPapel;
import com.example.zumbasquad.model.Usuario;
import com.example.zumbasquad.model.auth.AuthenticationRequest;
import com.example.zumbasquad.model.auth.RegisterRequest;
import com.example.zumbasquad.repository.IUsuarioRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private IUsuarioRepository mockRepository;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @InjectMocks
    private JwtService mockJwtService;

    @Mock
    private AuthenticationManager mockAuthenticationManager;

    @Test
    void testRegister(){
        RegisterRequest request = new RegisterRequest("nome", "sobrenome", "email", "senha");

        var usuario = Usuario.builder()
                .nome(request.getNome())
                .sobrenome(request.getSobrenome())
                .email(request.getEmail())
                .senha(mockPasswordEncoder.encode(request.getSenha()))
                .papel(EnumPapel.ADMIN)
                .build();

        mockRepository.save(usuario);

        var jwtToken = mockJwtService.generateToken(usuario);

        var service = new AuthenticationService(mockRepository, mockPasswordEncoder, mockJwtService, mockAuthenticationManager);
        var result = service.register(request);

        assertEquals(result.getToken(), jwtToken);
    }

    @Test
    @Disabled
    void testLogin(){
        var usuario = Usuario.builder()
                .nome("nome")
                .sobrenome("sobrenome")
                .email("email")
                .senha(mockPasswordEncoder.encode("senha"))
                .papel(EnumPapel.ADMIN)
                .build();

        mockRepository.save(usuario);

        var request = new AuthenticationRequest(usuario.getEmail(), usuario.getSenha());

        mockAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getSenha()
                )
        );

        var usuarioEncontrado = mockRepository.findByEmail(request.getEmail()).get();

        var jwtToken = mockJwtService.generateToken(usuarioEncontrado);

        var service = new AuthenticationService(mockRepository, mockPasswordEncoder, mockJwtService, mockAuthenticationManager);
        var result = service.login(request);

        assertEquals(result.getToken(), jwtToken);
    }

    @Test
    void testLoginThrowsExceptionWhenUserNotFound() {
        // Arrange
        var request = new AuthenticationRequest("john.doe@example.com", "password");
        when(mockRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        var service = new AuthenticationService(mockRepository, mockPasswordEncoder, mockJwtService, mockAuthenticationManager);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> service.login(request));
    }

    @Test
    void testGetData() {
        // Arrange
        var email = "john.doe@example.com";
        var expectedUser = Usuario.builder()
                .nome("John")
                .sobrenome("Doe")
                .email(email)
                .senha("encodedPassword")
                .papel(EnumPapel.USER)
                .build();
        when(mockRepository.findByEmail(email)).thenReturn(Optional.of(expectedUser));

        var service = new AuthenticationService(mockRepository, mockPasswordEncoder, mockJwtService, mockAuthenticationManager);

        // Act
        var result = service.getData(email);

        // Assert
        assertEquals(result, Optional.of(expectedUser));
    }
}

