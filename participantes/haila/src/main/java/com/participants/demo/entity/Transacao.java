package com.participants.demo.entity;

import com.participants.demo.enums.Tipo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.OffsetDateTime;

@Entity
@Table(name = "transacoes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer valor;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    private String descricao;

    @Column(name = "realizada_em")
    private Instant realizadaEm;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
