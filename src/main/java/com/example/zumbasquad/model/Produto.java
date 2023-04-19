package com.example.zumbasquad.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "descricao_id", referencedColumnName = "id")
    private Descricao descricao;
    private boolean favorito;
    private float estrelas;
    private float avaliacao;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "localizacao_id", referencedColumnName = "id")
    private Localizacao localizacao;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "detalhe_id", referencedColumnName = "id")
    private Detalhe detalhes;
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Imagem> imagens;
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "caracteristica_produto",
            joinColumns = {@JoinColumn(name = "produto_id")},
            inverseJoinColumns = {@JoinColumn(name = "caracteristica_id")})
    private List<Caracteristica> caracteristicas = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "cidade_id", nullable = false)
    private Cidade cidade;
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @OneToMany(mappedBy = "produto")
    @JsonIgnore
    private List<Reserva> reservas;

    public Produto(String nome, Descricao descricao, List<Imagem> imagens, List<Caracteristica> caracteristicas, Cidade cidade, Categoria categoria) {
        this.nome = nome;
        this.descricao = descricao;
        this.imagens = imagens;
        this.caracteristicas = caracteristicas;
        this.cidade = cidade;
        this.categoria = categoria;
    }

    public Produto(String nome, Descricao descricao, List<Imagem> imagens, List<Caracteristica> caracteristicas, Cidade cidade, Categoria categoria, List<Reserva> reservas) {
        this.nome = nome;
        this.descricao = descricao;
        this.imagens = imagens;
        this.caracteristicas = caracteristicas;
        this.cidade = cidade;
        this.categoria = categoria;
        this.reservas = reservas;
    }
}
