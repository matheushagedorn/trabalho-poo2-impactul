package com.example.application.dao;

import com.example.application.database.connection.ConexaoBanco;
import com.example.application.dto.Ganho;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GanhoDao {
    private static final Connection connection = ConexaoBanco.ConexaoBanco();

    public static void adicionarGanho(Ganho ganho){
        String sql = "INSERT INTO ganhos (nome, valor, data, usuario_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, ganho.getNome());
            preparedStatement.setDouble(2, ganho.getValor());
            preparedStatement.setDate(3, Date.valueOf(ganho.getData()));
            preparedStatement.setInt(4, ganho.getUsuario().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao adicionar ganho");
        }
    }

    public static void atualizarGanho(Ganho ganho){
        String sql = "UPDATE ganhos SET nome = ?, valor = ?, data = ?, usuario_id = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, ganho.getNome());
            preparedStatement.setDouble(2, ganho.getValor());
            preparedStatement.setDate(3, Date.valueOf(ganho.getData()));
            preparedStatement.setInt(4, ganho.getUsuario().getId());
            preparedStatement.setInt(5, ganho.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar ganho");
        }
    }

    public static Ganho buscarGanhoPorId(int id) {
        String sql = "SELECT g.* FROM ganhos g " +
                "JOIN logins l ON g.usuario_id = l.id " +
                "WHERE g.id = ? AND l.logado = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setBoolean(2, true);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return criarGanhoAPartirDoResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar ganho por id");
        }
        return null;
    }


    public static List<Ganho> buscarTodosGanhos() {
        List<Ganho> ganhos = new ArrayList<>();
        String sql = "SELECT g.* FROM ganhos g " +
                "JOIN logins l ON g.usuario_id = l.id " +
                "WHERE l.logado = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBoolean(1, true);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    ganhos.add(criarGanhoAPartirDoResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar todos os ganhos");
        }
        return ganhos;
    }


    public static void deletarGanho(int id){
        String sql = "DELETE FROM ganhos WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao deletar ganho");
        }
    }

    private static Ganho criarGanhoAPartirDoResultSet(ResultSet resultSet){
        Ganho ganho = new Ganho();
        try {
            ganho.setId(resultSet.getInt("id"));
            ganho.setNome(resultSet.getString("nome"));
            ganho.setValor(resultSet.getDouble("valor"));
            ganho.setData(resultSet.getDate("data").toLocalDate());
            ganho.setUsuario(LoginDao.buscarLoginPorId(resultSet.getInt("usuario_id")));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar ganho a partir do ResultSet");
        }

        return ganho;
    }
}
