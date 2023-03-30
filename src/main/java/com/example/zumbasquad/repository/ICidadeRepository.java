package com.example.zumbasquad.repository;

import com.example.zumbasquad.model.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICidadeRepository extends JpaRepository<Cidade, Long> {
}
