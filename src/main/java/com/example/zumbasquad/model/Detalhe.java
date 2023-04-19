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
@Table(name = "detalhes")
public class Detalhe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String regrasDaCasa;
    @Column(columnDefinition = "TEXT")
    private String politicaDeCancelamento;
    @Column(columnDefinition = "TEXT")
    private String saudeESeguranca;
    @OneToOne(mappedBy = "detalhes")
    @JsonIgnore
    private Produto produto;

    public Detalhe(Long id, String regrasDaCasa, String politicaDeCancelamento, String saudeESeguranca) {
        this.id = id;
        this.regrasDaCasa = regrasDaCasa;
        this.politicaDeCancelamento = politicaDeCancelamento;
        this.saudeESeguranca = saudeESeguranca;
    }
}
