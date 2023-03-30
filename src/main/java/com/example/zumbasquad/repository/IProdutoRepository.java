package com.example.zumbasquad.repository;

import com.example.zumbasquad.model.Categoria;
import com.example.zumbasquad.model.Cidade;
import com.example.zumbasquad.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByCidadeId(final Long id);

    List<Produto> findByCategoriaId(final Long id);

    List<Produto> findByCidadeNome(String nome);

    List<Produto> findByCategoriaQualificacao(String qualificacao);

    List<Produto> findByReservasDataInicialAndReservasDataFinal(LocalDate dataInicial, LocalDate dataFinal);

    List<Produto> findByReservasDataInicialAndReservasDataFinalOrCidadeNome(LocalDate dataInicial, LocalDate dataFinal, String cidade);
}
