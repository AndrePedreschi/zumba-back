package com.example.zumbasquad.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cidades")
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;
    private String pais;
    @OneToMany(mappedBy = "cidade")
    private Set<Produto> produtos;

    public Cidade(String nome, String pais) {
        this.nome = nome;
        this.pais = pais;
    }
}
