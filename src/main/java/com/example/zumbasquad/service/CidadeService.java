package com.example.zumbasquad.service;

import com.example.zumbasquad.model.Cidade;
import com.example.zumbasquad.repository.ICidadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CidadeService {
    private final ICidadeRepository repository;

    public CidadeService(ICidadeRepository repository) { this.repository = repository; }

    public Cidade add(Cidade cidade) { return repository.save(cidade); }

    public List<Cidade> getAll() { return  repository.findAll(); }
}
