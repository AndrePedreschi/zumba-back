package com.example.zumbasquad.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String nome;
    private String sobrenome;
    private String email;
    private String senha;
//    private boolean enabled = true;
}
