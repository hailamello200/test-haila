package com.participants.demo.service;

import com.participants.demo.dto.ExtratoDTO;
import com.participants.demo.dto.TransacaoRequestDTO;
import com.participants.demo.dto.TransacaoResponseDTO;
import com.participants.demo.entity.Cliente;
import com.participants.demo.entity.Transacao;
import com.participants.demo.enums.Tipo;
import com.participants.demo.repository.ClienteRepository;
import com.participants.demo.repository.TransacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    ClienteRepository clienteRepository;

    @Mock
    TransacaoRepository transacaoRepository;

    @InjectMocks
    ClienteService clienteService;

    Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setLimite(1000);
        cliente.setSaldo(0);
    }

    @Test
    void criarTransacaoClienteNaoEncontrado() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
                clienteService.criarTransacao(1L, new TransacaoRequestDTO()));
    }

    @Test
    void criarTransacaoCamposInvalidos() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        TransacaoRequestDTO dto = new TransacaoRequestDTO();
        dto.setValor(-10);
        dto.setDescricao("");
        dto.setTipo(Tipo.CREDITO.getCodigo());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                clienteService.criarTransacao(1L, dto));

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getStatusCode());
    }

    @Test
    void criarTransacaoCreditoComsucesso() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        TransacaoRequestDTO dto = new TransacaoRequestDTO();
        dto.setValor(500);
        dto.setDescricao("Descrição");
        dto.setTipo(Tipo.CREDITO.getCodigo());

        TransacaoResponseDTO response = clienteService.criarTransacao(1L, dto);

        assertEquals(500, response.getSaldo());
        assertEquals(1000, response.getLimite());

        verify(clienteRepository).save(cliente);
        verify(transacaoRepository).save(any());
    }

    @Test
    void criarTransacaoDebitoEstouraLimite() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        TransacaoRequestDTO dto = new TransacaoRequestDTO();
        dto.setValor(2000);
        dto.setDescricao("Descrição");
        dto.setTipo(Tipo.DEBITO.getCodigo());

        assertThrows(ResponseStatusException.class, () ->
                clienteService.criarTransacao(1L, dto));
    }

    @Test
    void criarTransacaoDebitoComsucesso() {
        cliente.setSaldo(500);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        TransacaoRequestDTO dto = new TransacaoRequestDTO();
        dto.setValor(300);
        dto.setDescricao("Descrição");
        dto.setTipo(Tipo.DEBITO.getCodigo());

        TransacaoResponseDTO response = clienteService.criarTransacao(1L, dto);

        assertEquals(200, response.getSaldo());
        verify(clienteRepository).save(cliente);
        verify(transacaoRepository).save(any());
    }

    @Test
    void extratoRetornaLista() {
        Transacao t = new Transacao();
        t.setValor(100);
        t.setTipo(Tipo.CREDITO);
        t.setDescricao("abc");
        t.setRealizadaEm(Instant.now());

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(transacaoRepository.findTop10ByClienteIdOrderByRealizadaEmDesc(1L))
                .thenReturn(List.of(t));

        ExtratoDTO extratoDTO = clienteService.extrato(1L);

        assertEquals(0, extratoDTO.getSaldo().getTotal()); // depende da implementação
        assertEquals(1, extratoDTO.getUltimasTransacoes().size());
    }
}

