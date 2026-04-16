package sistema.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import sistema.app.menu.CustomLogger;
import sistema.repository.ConexaoDB;

public class EscritorCSV {
  CustomLogger customLogger = new CustomLogger();

  private final ConexaoDB conexao;

  public EscritorCSV(ConexaoDB conexao) {
    this.conexao = conexao;
  }

  public void escreverFolhaPagamentoCSV(String caminho) {
    String nomeArquivo = "folha_de_pagamento_" + LocalDate.now() + ".csv";
    String caminhoExportacao = caminho + File.separator + nomeArquivo;

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
        BufferedWriter bw = new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream(caminhoExportacao), StandardCharsets.UTF_8))) {

      while (rs.next()) {
        bw.write(
            rs.getString("nome") + ";" +
                rs.getString("cpf") + ";" +
                rs.getDouble("desconto_inss") + ";" +
                rs.getDouble("desconto_ir") + ";" +
                rs.getDouble("salario_liquido"));
        bw.newLine();
      }

      customLogger.logSucess("Arquivo gerado em: " + caminhoExportacao);

    } catch (Exception e) {
      customLogger.logError("Erro ao exportar FOLHA DE PAGAMENTO!");
      throw new RuntimeException(e.getMessage());
    }
  }

}
