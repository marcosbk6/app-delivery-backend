package com.app_delivery_test1.backend.repository;

import com.app_delivery_test1.backend.controller.Prato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PratoRepository extends JpaRepository<Prato, Long> {
    // Método para encontrar um prato pelo nome
    Optional<Prato> findByNome(String nome);
}
