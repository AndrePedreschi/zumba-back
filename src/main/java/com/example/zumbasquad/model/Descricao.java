package com.example.zumbasquad.model;

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
    private String id;
    private String titulo;
    private String texto;
    @OneToOne(mappedBy = "descricao")
    private Produto produto;
}

