package com.example.application.dao;

import com.example.application.database.connection.ConexaoBanco;
import com.example.application.dto.Login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoginDao {
    private static final Connection connection = ConexaoBanco.ConexaoBanco();

    public static Integer verificarUsuarioSenha(String usuario, String senha) {
        boolean usuarioEncontrado = false;
        Integer idUser = 0;

        String sql = "SELECT * FROM logins WHERE usuario = ? AND senha = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, usuario);
            preparedStatement.setString(2, senha);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    usuarioEncontrado = count > 0;
                    if (usuarioEncontrado){
                        idUser = resultSet.getInt("id");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao verificar usuário e senha");
        }

        return idUser;
    }

    public static List<Login> buscarTodosLogins() {
        List<Login> logins = new ArrayList<>();
        String sql = "SELECT * FROM logins";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    logins.add(criarLoginAPartirDoResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar todos os logins");
        }
        return logins;
    }

    public static Login buscarLoginPorId(int id) {
        Login login = null;

        String sql = "SELECT * FROM logins WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    login = criarLoginAPartirDoResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar login por ID");
        }

        return login;
    }

    private static Login criarLoginAPartirDoResultSet(ResultSet resultSet) throws SQLException {
        Login login = new Login();
        login.setId(resultSet.getInt("id"));
        login.setUsuario(resultSet.getString("usuario"));
        login.setSenha(resultSet.getString("senha"));
        return login;
    }

    public static Login validarLoginLogado() {
        Login login = null;

        String sql = "SELECT * FROM logins WHERE logado = ? LIMIT 1";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBoolean(1, true);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    login = criarLoginAPartirDoResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao validar o login logado");
        }

        return login;
    }

    public static void atualizarLoginParaLogado(int id) {
        String sql = "UPDATE logins SET logado = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBoolean(1, true);
            preparedStatement.setInt(2, id);

            int rowCount = preparedStatement.executeUpdate();
            if (rowCount == 0) {
                throw new RuntimeException("Nenhum registro de login foi atualizado. ID não encontrado.");
            } else {
                System.out.println(rowCount + " registro de login foi atualizado para logado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar login para logado");
        }
    }


    public static void desativarTodosLogados() {
        String sql = "UPDATE logins SET logado = ? WHERE logado = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBoolean(1, false);
            preparedStatement.setBoolean(2, true);

            int rowCount = preparedStatement.executeUpdate();
            System.out.println(rowCount + " registros foram desativados.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao desativar logins");
        }
    }

    public static List<Login> buscarLoginsLogados() {
        List<Login> loginsLogados = new ArrayList<>();

        String sql = "SELECT * FROM logins WHERE logado = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBoolean(1, true);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    loginsLogados.add(criarLoginAPartirDoResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar logins logados");
        }

        return loginsLogados;
    }



}
