package com.example.zumbasquad.repository;

import com.example.zumbasquad.model.Produto;
import com.example.zumbasquad.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByProdutoId(final Long id);
}
