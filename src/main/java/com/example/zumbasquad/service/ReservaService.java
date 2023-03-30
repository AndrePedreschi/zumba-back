package com.example.zumbasquad.service;

import com.example.zumbasquad.model.Produto;
import com.example.zumbasquad.model.Reserva;
import com.example.zumbasquad.repository.IReservaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaService {
    private final IReservaRepository repository;

    public ReservaService(IReservaRepository repository) { this.repository = repository; }

    public Reserva add(Reserva reserva) { return repository.save(reserva); }

    public List<Reserva> getReservaByProdutoId(Long id) { return repository.findByProdutoId(id); }
}
