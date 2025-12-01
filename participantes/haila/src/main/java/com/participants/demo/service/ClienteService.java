package com.participants.demo.service;

import com.participants.demo.dto.*;
import com.participants.demo.entity.Cliente;
import com.participants.demo.entity.Transacao;
import com.participants.demo.enums.Tipo;
import com.participants.demo.repository.ClienteRepository;
import com.participants.demo.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final TransacaoRepository transacaoRepository;

    @Transactional
    public TransacaoResponseDTO criarTransacao(Long clientId, TransacaoRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente inexistente"));

        if (dto.getValor() == null || dto.getValor() <= 0 ||
            dto.getDescricao() == null || dto.getDescricao().length() < 1 ||
                dto.getDescricao().length() > 10) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Tipo tipo;
        if ("c".equalsIgnoreCase(dto.getTipo())) {
            tipo = Tipo.CREDITO;
        } else if ("d".equalsIgnoreCase(dto.getTipo())) {
            tipo = Tipo.DEBITO;
        } else {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Tipo de transação inválido.");
        }

        Integer novoSaldo = cliente.getSaldo();

        if (tipo == Tipo.CREDITO) {
            novoSaldo += dto.getValor();
        } else {
            novoSaldo -= dto.getValor();
            if (novoSaldo < -cliente.getLimite()) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }

        cliente.setSaldo(novoSaldo);
        clienteRepository.save(cliente);

        novaTransaco(cliente, dto, tipo);
        return new TransacaoResponseDTO(cliente.getLimite(), cliente.getSaldo());
    }

    public void novaTransaco(Cliente cliente, TransacaoRequestDTO dto, Tipo tipo) {
        Transacao transacao = new Transacao();
        transacao.setCliente(cliente);
        transacao.setValor(dto.getValor());
        transacao.setTipo(tipo);
        transacao.setDescricao(dto.getDescricao());
        transacao.setRealizadaEm(Instant.now());

        transacaoRepository.save(transacao);
    }

    public ExtratoDTO extrato(Long clientId) {
        Cliente cliente = clienteRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente inexistente"));

        List<Transacao> ultimas = transacaoRepository.findTop10ByClienteIdOrderByRealizadaEmDesc(clientId);

        List<TransacaoExtratoDTO> dtos =
                ultimas.stream().map(t ->
                        new TransacaoExtratoDTO(
                                t.getValor(),
                                t.getTipo().getDescricao(),
                                t.getDescricao(),
                                t.getRealizadaEm()
                        )).toList();

        return new ExtratoDTO(new SaldoDTO(cliente.getSaldo(), Instant.now(), cliente.getLimite()), dtos);
    }
}
