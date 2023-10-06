package com.example.application.dto;

import java.time.LocalDate;
import java.util.Objects;

public class Gasto {
    private Integer id;
    private String nome;
    private Double valor;
    private LocalDate data;
    private String formaPagamento;
    private Login usuario;

    public Gasto() {
    }

    public Login getUsuario() {
        return usuario;
    }

    public void setUsuario(Login usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gasto gasto = (Gasto) o;
        return Objects.equals(id, gasto.id) && Objects.equals(nome, gasto.nome) && Objects.equals(valor, gasto.valor) && Objects.equals(data, gasto.data) && Objects.equals(formaPagamento, gasto.formaPagamento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, valor, data, formaPagamento);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
}
