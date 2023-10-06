package com.example.application.dto;

import java.util.Objects;

public class Login {
    private Integer id;
    private String usuario;
    private String senha;

    public Login() {
    }

    public Login(Integer id, String usuario, String senha) {
        this.id = id;
        this.usuario = usuario;
        this.senha = senha;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Login login = (Login) o;
        return Objects.equals(id, login.id) && Objects.equals(usuario, login.usuario) && Objects.equals(senha, login.senha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, usuario, senha);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
