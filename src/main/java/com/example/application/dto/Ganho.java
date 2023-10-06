package com.example.application.dto;

import java.time.LocalDate;
import java.util.Objects;

public class Ganho {
    private Integer id;
    private String nome;
    private Double valor;
    private LocalDate data;
    private Login usuario;

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
        Ganho ganho = (Ganho) o;
        return Objects.equals(id, ganho.id) && Objects.equals(nome, ganho.nome) && Objects.equals(valor, ganho.valor) && Objects.equals(data, ganho.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, valor, data);
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
}
