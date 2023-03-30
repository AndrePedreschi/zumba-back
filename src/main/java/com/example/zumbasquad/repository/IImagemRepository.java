package com.example.zumbasquad.repository;

import com.example.zumbasquad.model.Imagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IImagemRepository extends JpaRepository<Imagem, Long> {
}
