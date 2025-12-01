package com.participants.demo.controller;

import com.participants.demo.dto.ExtratoDTO;
import com.participants.demo.dto.TransacaoRequestDTO;
import com.participants.demo.dto.TransacaoResponseDTO;
import com.participants.demo.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;

    @PostMapping("/{id}/transacoes")
    public ResponseEntity<TransacaoResponseDTO> criarTransacao(
            @PathVariable Long id,
            @RequestBody TransacaoRequestDTO dto) {
        return ResponseEntity.ok(service.criarTransacao(id, dto));
    }

    @GetMapping("/{id}/extrato")
    public ResponseEntity<ExtratoDTO> extrato(@PathVariable Long id) {
        return ResponseEntity.ok(service.extrato(id));
    }
}
