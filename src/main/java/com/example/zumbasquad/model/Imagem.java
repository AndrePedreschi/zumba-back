package com.example.zumbasquad.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "imagens")
public class Imagem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String titulo;
    private String url;
    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    public Imagem(String titulo, String url) {
        this.titulo = titulo;
        this.url = url;
    }
}
