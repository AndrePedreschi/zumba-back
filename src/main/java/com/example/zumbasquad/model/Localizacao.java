package com.example.zumbasquad.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "localizacao")
public class Localizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String endereco;
    private int distancia;
    private float[][] coordenadas;
    private float[][] centro;
    @OneToOne(mappedBy = "localizacao")
    private Produto produto;
}
