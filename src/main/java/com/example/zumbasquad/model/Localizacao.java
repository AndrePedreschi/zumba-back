package com.example.zumbasquad.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Long id;
    private String endereco;
    private int distancia;
    private String coordenadas;
    private String centro;
    @OneToOne(mappedBy = "localizacao")
    @JsonIgnore
    private Produto produto;

    public Localizacao(Long id, String endereco) {
        this.id = id;
        this.endereco = endereco;
    }

    public Localizacao(Long id, String endereco, int distancia, String coordenadas, String centro) {
        this.id = id;
        this.endereco = endereco;
        this.distancia = distancia;
        this.coordenadas = coordenadas;
        this.centro = centro;
    }
}
