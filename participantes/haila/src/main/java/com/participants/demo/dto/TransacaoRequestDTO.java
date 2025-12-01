package com.participants.demo.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class TransacaoRequestDTO {

    private Integer valor;
    private String tipo;
    private String descricao;
    private Instant realizadaEm;
}
