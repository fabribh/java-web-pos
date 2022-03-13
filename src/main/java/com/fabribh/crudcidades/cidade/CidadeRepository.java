package com.fabribh.crudcidades.cidade;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CidadeRepository extends JpaRepository<CidadeEntidade, Long> {

    Optional<CidadeEntidade> findByNomeAndEstado(String nome, String estado);
}
