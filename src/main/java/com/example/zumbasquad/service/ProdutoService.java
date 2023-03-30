package com.example.zumbasquad.service;

import com.example.zumbasquad.model.Cidade;
import com.example.zumbasquad.model.Produto;
import com.example.zumbasquad.model.Reserva;
import com.example.zumbasquad.repository.IProdutoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProdutoService {
    private final IProdutoRepository repository;

    public ProdutoService(IProdutoRepository repository) { this.repository = repository; }

    public Produto add(Produto produto) { return repository.save(produto); }

    public List<Produto> getAll() { return repository.findAll(); }

    public Produto getById(Long id) { return repository.findById(id).get(); }

    public List<Produto> getAllProductsByCityId(Long id) { return repository.findByCidadeId(id); }

    public List<Produto> getAllProductsByCategoryId(Long id) { return repository.findByCategoriaId(id); }

    public List<Produto> getAllProductsByCityName(String nome){
        return repository.findByCidadeNome(nome);
    }

    public List<Produto> getAllProductsByCategoryQualification(String qualificacao){
        return repository.findByCategoriaQualificacao(qualificacao);
    }

    public List<Produto> getAllProductsByDates(LocalDate dataInicial, LocalDate dataFinal){
        return repository.findByReservasDataInicialAndReservasDataFinal(dataInicial, dataFinal);
    }

    public List<Produto> getAllProductsByDatesOrCity(LocalDate dataInicial, LocalDate dataFinal, String cidade){
        return repository.findByReservasDataInicialAndReservasDataFinalOrCidadeNome(dataInicial, dataFinal, cidade);
    }
}
