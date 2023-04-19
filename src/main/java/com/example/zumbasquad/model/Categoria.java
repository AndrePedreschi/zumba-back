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
@Table(name = "categorias")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String qualificacao;
    private String descricao;
    private String urlImagem;
    @OneToMany(mappedBy = "categoria")
    @JsonIgnore
    private List<Produto> produtos;

    public Categoria(String qualificacao, String descricao, String urlImagem) {
        this.qualificacao = qualificacao;
        this.descricao = descricao;
        this.urlImagem = urlImagem;
    }
}
