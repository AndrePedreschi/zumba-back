package com.example.zumbasquad.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    @ManyToMany(mappedBy = "caracteristicas")
    @JsonIgnore
    private List<Produto> produtos;

    public Caracteristica(String nome, String icone) {
        this.nome = nome;
        this.icone = icone;
    }

    public Caracteristica(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}
