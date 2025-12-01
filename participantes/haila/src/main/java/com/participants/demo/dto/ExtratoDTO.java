package com.participants.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ExtratoDTO {

    private SaldoDTO saldo;
    private List<TransacaoExtratoDTO> ultimasTransacoes;
}
