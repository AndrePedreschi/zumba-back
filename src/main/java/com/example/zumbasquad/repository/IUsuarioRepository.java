package com.example.zumbasquad.repository;

import com.example.zumbasquad.model.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

   Optional<Usuario> findByEmail(String email);
}
