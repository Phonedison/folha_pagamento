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

public class EscreverCSV {
  CustomLogger customLogger = new CustomLogger();

  private final ConexaoDB conexao;

  public EscreverCSV(ConexaoDB conexao) {
    this.conexao = conexao;
  }
  // Método responsável por exportar os dados da tabela Folha_de_pagamento para CSV
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
  // Método responsável por exportar os dados da tabela FUNCIONARIO para um arquivo CSV
  public void escreverFuncionarioCSV(String caminho) {
    // Cria o nome do arquivo com a data atual
    String nomeArquivo = "Lista_de_funcionario_" + LocalDate.now() + ".csv";
    // Monta o caminho completo do arquivo (pasta + nome do arquivo)
    String caminhoExportacao = caminho + File.separator + nomeArquivo;
    // Comando SQL que busca todos os funcionários ordenados pelo ID (decrescente)
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
      // Log de sucesso informando onde o arquivo foi gerado
      customLogger.logSucess("Arquivo gerado em: " + caminhoExportacao);

    } catch (Exception e) {
      customLogger.logError("Erro ao exportar FUNCIONARIO!");
      throw new RuntimeException(e.getMessage());
    }
  }
  // Método responsável por exportar os dados da tabela DEPENDENTE para CSV
  public void escreverDependenteCSV(String caminho) {
    // Nome do arquivo com data atual
    String nomeArquivo = "Lista_de_dependente_" + LocalDate.now() + ".csv";
    // Monta o caminho completo do arquivo (pasta + nome do arquivo)
    String caminhoExportacao = caminho + File.separator + nomeArquivo;
    // Comando SQL para buscar dependentes ordenados por ID (decrescente)
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
      // Log de sucesso informando onde o arquivo foi gerado
      customLogger.logSucess("Arquivo gerado em: " + caminhoExportacao);

    } catch (Exception e) {
      customLogger.logError("Erro ao exportar DEPENDENTE!");
      throw new RuntimeException(e.getMessage());
    }
  }
  // Método que exporta a quantidade de dependentes por funcionário
  public void escreverQtdDependentePorFuncionario(String caminho) {
    // Nome do arquivo com data
    String nomeArquivo = "Lista_de_qtd_dependente_por_funcionario_" + LocalDate.now() + ".csv";
    // Caminho completo do arquivo
    String caminhoExportacao = caminho + File.separator + nomeArquivo;

    // Comando SQL que:
    // Junta funcionario com dependente (LEFT JOIN)
    // Conta quantos dependentes cada funcionário tem
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
      // Log de sucesso
      customLogger.logSucess("Arquivo gerado em: " + caminhoExportacao);

    } catch (Exception e) {
      customLogger.logError("Erro ao exportar DADOS DE QTD de DEPENDENTE por FUNCIONARIO!");
      throw new RuntimeException(e.getMessage());
    }
  }
}
