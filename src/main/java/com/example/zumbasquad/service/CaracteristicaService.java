package com.example.zumbasquad.service;

import com.example.zumbasquad.model.Caracteristica;
import com.example.zumbasquad.repository.ICaracteristicaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaracteristicaService {
    private final ICaracteristicaRepository repository;

    public CaracteristicaService(ICaracteristicaRepository repository) { this.repository = repository; }

    public Caracteristica add(Caracteristica caracteristica) { return repository.save(caracteristica); }

    public List<Caracteristica> getAll() { return repository.findAll(); }

    public Caracteristica getById(Long id) { return repository.findById(id).get(); }
}