package com.example.zumbasquad.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "detalhes")
public class Detalhe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String regrasDaCasa;
    private String politicaDeCancelamento;
    private String saudeESeguranca;
    @OneToOne(mappedBy = "detalhes")
    private Produto produto;
}
