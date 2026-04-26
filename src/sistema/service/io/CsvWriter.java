package sistema.service.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import sistema.app.util.CustomLogger;
import sistema.repository.connection.DatabaseConfig;

public class CsvWriter {

  private final DatabaseConfig conexao;

  public CsvWriter(DatabaseConfig conexao) {
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

      CustomLogger.logSucess("Arquivo gerado em: " + caminhoExportacao);

    } catch (Exception e) {
      CustomLogger.logError("Erro ao exportar FOLHA DE PAGAMENTO!");
      throw new RuntimeException(e.getMessage());
    }
  }

  public void escreverFuncionarioCSV(String caminho) {
    String nomeArquivo = "Lista_de_funcionario_" + LocalDate.now() + ".csv";
    String caminhoExportacao = caminho + File.separator + nomeArquivo;

    String comandoSQL = """
        SELECT * FROM funcionario ORDER BY id_funcionario DESC;
        """;

    try (Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL);
        ResultSet rs = stmt.executeQuery();
        BufferedWriter bw = new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream(caminhoExportacao), StandardCharsets.UTF_8))) {

      while (rs.next()) {
        bw.write(
            rs.getInt("id_funcionario") + ";" +
                rs.getString("nome") + ";" +
                rs.getString("cpf") + ";" +
                rs.getDate("data_nascimento") + ";" +
                rs.getDouble("salario_bruto"));
        bw.newLine();
      }

      CustomLogger.logSucess("Arquivo gerado em: " + caminhoExportacao);

    } catch (Exception e) {
      CustomLogger.logError("Erro ao exportar FUNCIONARIO!");
      throw new RuntimeException(e.getMessage());
    }
  }

  public void escreverDependenteCSV(String caminho) {
    String nomeArquivo = "Lista_de_dependente_" + LocalDate.now() + ".csv";
    String caminhoExportacao = caminho + File.separator + nomeArquivo;

    String comandoSQL = """
        SELECT * FROM dependente ORDER BY id_dependente DESC;
        """;

    try (Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL);
        ResultSet rs = stmt.executeQuery();
        BufferedWriter bw = new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream(caminhoExportacao), StandardCharsets.UTF_8))) {

      while (rs.next()) {
        bw.write(
            rs.getInt("id_dependente") + ";" +
                rs.getString("nome") + ";" +
                rs.getString("cpf") + ";" +
                rs.getString("parentesco") + ";" +
                rs.getInt("id_funcionario"));
        bw.newLine();
      }

      CustomLogger.logSucess("Arquivo gerado em: " + caminhoExportacao);

    } catch (Exception e) {
      CustomLogger.logError("Erro ao exportar DEPENDENTE!");
      throw new RuntimeException(e.getMessage());
    }
  }

  public void escreverQtdDependentePorFuncionario(String caminho) {
    String nomeArquivo = "Lista_de_qtd_dependente_por_funcionario_" + LocalDate.now() + ".csv";
    String caminhoExportacao = caminho + File.separator + nomeArquivo;

    String comandoSQL = """
        SELECT
            f.id_funcionario,
            f.nome,
            COUNT(d.id_dependente) AS quantidade_dependentes
        FROM funcionario f
        LEFT JOIN dependente d
            ON f.id_funcionario = d.id_funcionario
        GROUP BY f.id_funcionario, f.nome
        ORDER BY f.id_funcionario;
            """;

    try (Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL);
        ResultSet rs = stmt.executeQuery();
        BufferedWriter bw = new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream(caminhoExportacao), StandardCharsets.UTF_8))) {

      while (rs.next()) {
        bw.write(
            rs.getInt("id_funcionario") + ";" +
                rs.getString("nome") + ";" +
                rs.getInt("quantidade_dependentes") + ";");
        bw.newLine();
      }

      CustomLogger.logSucess("Arquivo gerado em: " + caminhoExportacao);

    } catch (Exception e) {
      CustomLogger.logError("Erro ao exportar DADOS DE QTD de DEPENDENTE por FUNCIONARIO!");
      throw new RuntimeException(e.getMessage());
    }
  }
}
