package com.participants.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class SaldoDTO {

    private Integer total;
    private Instant dataExtrato;
    private Integer limite;
}
