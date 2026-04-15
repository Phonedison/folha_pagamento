package sistema.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import sistema.repository.ConexaoDB;

public class EscritorCSV {
  private final ConexaoDB conexao;

  public EscritorCSV(ConexaoDB conexao) {
    this.conexao = conexao;
  }

  public void escreverFolhaPagamentoCSV(String caminho) throws SQLException {
    String comandoSQL = """
        SELECT f.nome,
               f.cpf,
               fp.desconto_inss,
               fp.desconto_ir,
               fp.salario_liquido
        FROM folha_pagamento fp
        JOIN funcionario f
          ON f.id_funcionario = fp.id_funcionario
        ORDER BY fp.codigo DESC
        """;
    try (Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL);
        ResultSet rs = stmt.executeQuery();
        BufferedWriter bw = new BufferedWriter(new FileWriter(caminho))) {

      while (rs.next()) {
        bw.write(
            rs.getString("nome") + ";" +
                rs.getString("cpf") + ";" +
                rs.getDouble("desconto_inss") + ";" +
                rs.getDouble("desconto_ir") + ";" +
                rs.getDouble("Salario_liquido"));
        bw.newLine();
      }

    } catch (Exception e) {
      // TODO: handle exception
    }
  }

}
