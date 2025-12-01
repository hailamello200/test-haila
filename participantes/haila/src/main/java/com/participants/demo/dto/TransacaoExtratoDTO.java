package com.participants.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class TransacaoExtratoDTO {

    private Integer valor;
    private String tipo;
    private String descricao;
    private Instant realizadaEm;
}
