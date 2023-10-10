package com.example.application.dao;

import com.example.application.database.connection.ConexaoBanco;
import com.example.application.dto.Gasto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GastoDao {
    private static final Connection connection = ConexaoBanco.ConexaoBanco();
    public static void adicionarGasto(Gasto gasto) {
        String sql = "INSERT INTO gastos (nome, valor, data, forma_pagamento, usuario_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, gasto.getNome());
            preparedStatement.setDouble(2, gasto.getValor());
            preparedStatement.setDate(3, Date.valueOf(gasto.getData()));
            preparedStatement.setString(4, gasto.getFormaPagamento());
            preparedStatement.setInt(5, gasto.getUsuario().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao adicionar gasto");
        }
    }

    public static void atualizarGasto(Gasto gasto) {
        String sql = "UPDATE gastos SET nome = ?, valor = ?, data = ?, forma_pagamento = ?, usuario_id = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, gasto.getNome());
            preparedStatement.setDouble(2, gasto.getValor());
            preparedStatement.setDate(3, Date.valueOf(gasto.getData()));
            preparedStatement.setString(4, gasto.getFormaPagamento());
            preparedStatement.setInt(5, gasto.getUsuario().getId());
            preparedStatement.setInt(6, gasto.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar gasto");
        }
    }

    public static Gasto buscarGastoPorId(int id) {
        String sql = "SELECT g.* FROM gastos g " +
                "JOIN logins l ON g.usuario_id = l.id " +
                "WHERE g.id = ? AND l.logado = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setBoolean(2, true);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return criarGastoAPartirDoResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar gasto por id");
        }
        return null;
    }


    public static List<Gasto> buscarTodosGastos() {
        List<Gasto> gastos = new ArrayList<>();
        String sql = "SELECT g.* FROM gastos g " +
                "JOIN logins l ON g.usuario_id = l.id " +
                "WHERE l.logado = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBoolean(1, true);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    gastos.add(criarGastoAPartirDoResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar todos os gastos");
        }
        return gastos;
    }

    public static void deletarGasto(int id) {
        String sql = "DELETE FROM gastos WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao deletar gasto");
        }
    }

    private static Gasto criarGastoAPartirDoResultSet(ResultSet resultSet){
        Gasto gasto = new Gasto();
        try {
            gasto.setId(resultSet.getInt("id"));
            gasto.setNome(resultSet.getString("nome"));
            gasto.setValor(resultSet.getDouble("valor"));
            gasto.setData(resultSet.getDate("data").toLocalDate());
            gasto.setFormaPagamento(resultSet.getString("forma_pagamento"));
            gasto.setUsuario(LoginDao.buscarLoginPorId(resultSet.getInt("usuario_id")));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar gasto a partir do ResultSet");
        }

        return gasto;
    }
}
