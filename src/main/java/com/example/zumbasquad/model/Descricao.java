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
@Table(name = "descricao")
public class Descricao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String titulo;
    @Column(columnDefinition = "TEXT")
    private String texto;
    @OneToOne(mappedBy = "descricao")
    @JsonIgnore
    private Produto produto;

    public Descricao(Long id, String texto) {
        this.id = id;
        this.texto = texto;
    }

    public Descricao(Long id, String titulo, String texto) {
        this.id = id;
        this.titulo = titulo;
        this.texto = texto;
    }
}

