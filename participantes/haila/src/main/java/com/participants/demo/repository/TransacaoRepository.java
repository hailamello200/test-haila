package com.participants.demo.repository;

import com.participants.demo.entity.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findTop10ByClienteIdOrderByRealizadaEmDesc(Long clienteId);
}
