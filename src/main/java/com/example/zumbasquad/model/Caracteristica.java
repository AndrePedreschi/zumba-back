package com.example.zumbasquad.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "caracteristicas")
public class Caracteristica {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;
    private String icone;
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "caracteristica_produto",
            joinColumns = {@JoinColumn(name = "caracteristica_id")},
            inverseJoinColumns = {@JoinColumn(name = "produto_id")})
    private Set<Produto> produtos = new HashSet<>();

    public Caracteristica(String nome, String icone) {
        this.nome = nome;
        this.icone = icone;
    }
}
