# Impactul - Controle de Ganhos e Gastos

## Descrição

Este sistema oferece funcionalidades para o gerenciamento de ganhos e gastos financeiros. Os usuários podem adicionar, visualizar, atualizar e excluir informações sobre ganhos e gastos de forma simples e eficaz.

## Como Usar

### Usuarios para logar

Nomes: usuario1, usuario2
<br>
Senhas: senha1, senha2

### Fluxo Principal

1. O usuário seleciona qual aba ele gostaria de navegar para gerenciar ganhos ou gastos.
2. O sistema exibe as operações disponíveis, incluindo adicionar, visualizar, atualizar ou excluir.
3. O usuário escolhe uma das operações.
4. Se for uma operação de adição ou atualização, o sistema solicita as informações relevantes.
5. O usuário fornece as informações necessárias.
6. O sistema executa a operação no banco de dados.
7. O sistema exibe uma mensagem de sucesso ou erro, informando o resultado da operação.

### Diagrama de classes
![Diagrama de classes](src/main/resources/ImpactulDiagrama.png)


Banco de dados : MySQL; JDBC : Mysql Connector 8.0.33; JAVA : OpenJDK 20;

## Query
```sql


CREATE DATABASE logincontrol;
USE logincontrol;

-- Criação da tabela com a coluna 'logado'
CREATE TABLE logins (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      usuario VARCHAR(255),
                      senha VARCHAR(255),
                      logado BOOLEAN DEFAULT FALSE
);

-- Inserção de registros com 'logado' definido como 'false'
INSERT INTO logins (usuario, senha, logado) VALUES
                                              ('usuario1', 'senha1', FALSE),
                                              ('usuario2', 'senha2', FALSE);


-- Tabela para a classe Ganho
CREATE TABLE ganhos (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      nome VARCHAR(255),
                      valor DOUBLE,
                      data VARCHAR(255),
                      usuario_id INT,
                      FOREIGN KEY (usuario_id) REFERENCES logins(id)
);

-- Inserindo 10 registros na tabela "ganhos" com IDs de usuário de 1 a 2
INSERT INTO ganhos (nome, valor, data, usuario_id) VALUES
                                                     ('Salário 1', 3000.00, '2023-01-05', 1),
                                                     ('Bônus 1', 500.00, '2023-02-10', 2),
                                                     ('Freelance 1', 800.00, '2023-03-15', 1),
                                                     ('Investimentos 1', 1500.00, '2023-04-20', 2),
                                                     ('Salário 2', 3500.00, '2023-05-25', 1),
                                                     ('Bônus 2', 600.00, '2023-06-30', 2),
                                                     ('Freelance 2', 1000.00, '2023-07-05', 1),
                                                     ('Investimentos 2', 2000.00, '2023-08-10', 2),
                                                     ('Salário 3', 4000.00, '2023-09-15', 1),
                                                     ('Bônus 3', 700.00, '2023-10-20', 2);


-- Tabela para a classe Gasto
CREATE TABLE gastos (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      nome VARCHAR(255),
                      valor DOUBLE,
                      data VARCHAR(255),
                      forma_pagamento VARCHAR(255),
                      usuario_id INT,
                      FOREIGN KEY (usuario_id) REFERENCES logins(id)
);

-- Inserindo 10 registros na tabela "gastos" com IDs de usuário de 1 a 2
INSERT INTO gastos (nome, valor, data, forma_pagamento, usuario_id) VALUES
                                                                      ('Aluguel 1', 1000.00, '2023-01-05', 'Cartão de Crédito', 1),
                                                                      ('Compras 1', 200.00, '2023-02-10', 'Dinheiro', 2),
                                                                      ('Restaurante 1', 150.00, '2023-03-15', 'Cartão de Débito', 1),
                                                                      ('Transporte 1', 50.00, '2023-04-20', 'Dinheiro', 2),
                                                                      ('Aluguel 2', 1100.00, '2023-05-25', 'Cartão de Crédito', 1),
                                                                      ('Compras 2', 250.00, '2023-06-30', 'Cartão de Crédito', 2),
                                                                      ('Restaurante 2', 180.00, '2023-07-05', 'Dinheiro', 1),
                                                                      ('Transporte 2', 60.00, '2023-08-10', 'Dinheiro', 2),
                                                                      ('Aluguel 3', 1200.00, '2023-09-15', 'Cartão de Crédito', 1),
                                                                      ('Compras 3', 220.00, '2023-10-20', 'Cartão de Crédito', 2);



```