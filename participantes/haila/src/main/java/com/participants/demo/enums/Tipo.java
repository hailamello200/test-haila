package com.participants.demo.enums;

public enum Tipo {

    CREDITO("c", "Crédito"),
    DEBITO("d", "Débito");

    private final String codigo;
    private final String descricao;

    Tipo(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }



}
