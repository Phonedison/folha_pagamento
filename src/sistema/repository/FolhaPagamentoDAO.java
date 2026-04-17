package sistema.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import sistema.app.menu.CustomLogger;
import sistema.model.FolhaPagamento;

public class FolhaPagamentoDAO implements CriacaoTabela {
  CustomLogger customLogger = new CustomLogger();

  private final ConexaoDB conexao;

  public FolhaPagamentoDAO(ConexaoDB conexao) {
    this.conexao = conexao;
  }

  @Override
  public final void criarTabela() { // -> Comando para executar a criação da tabela

    String comandoSQL = "CREATE TABLE IF NOT EXISTS folha_pagamento ( "
        + " codigo SERIAL PRIMARY KEY,"
        + " data_pagamento DATE NOT NULL,"
        + " desconto_inss NUMERIC(15,2),"
        + " desconto_ir NUMERIC(15,2),"
        + " salario_liquido NUMERIC(15,2),"
        + " id_funcionario INT REFERENCES funcionario(id_funcionario) NOT NULL"
        + " );";

    try (
        Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL);) {
      stmt.execute();

    } catch (Exception error) {
      customLogger.logError("Erro ao inicializar tabela Folha_pagamento: ");
      throw new RuntimeException(error.getMessage(),
          error);
    }
  }

  // INSET INTO
  public void salvarFolha(FolhaPagamento folhaPagamento) {
    String comandoSQL = "INSERT INTO folha_pagamento (data_pagamento, desconto_inss, desconto_ir, salario_liquido, id_funcionario) VALUES (?, ?, ?, ?, ?);";

    try (
        Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL);) {
      stmt.setObject(1, folhaPagamento.getDataPagamento());
      // -> checar com o professor se o melhor é setObject ou setDate
      stmt.setObject(2, folhaPagamento.getDescontoInss());
      stmt.setObject(3, folhaPagamento.getDescontoIR());
      stmt.setObject(4, folhaPagamento.getSalarioLiquido());
      stmt.setObject(5, folhaPagamento.getFuncionario().getId_funcionario());

      stmt.executeUpdate();
      customLogger
          .logFolhaSucess(
              "Folha de pagamento do(a) funcionário(a) '" + folhaPagamento.getFuncionario().getId_funcionario()
                  + " " + folhaPagamento.getFuncionario().getNome() + "' Registrado!");

    } catch (Exception error) {
      customLogger.logError("Erro na inserção da folha de pagamento do(a) funcionário(a) '"
          + folhaPagamento.getFuncionario().getId_funcionario() + " " + folhaPagamento.getFuncionario().getNome()
          + "' !");
      throw new RuntimeException("Erro: " + error.getMessage());

    }
  }

  // UPDATE
  public void atualizarFolha(FolhaPagamento folhaPagamento, int opcao) {
    String parteSQL;

    switch (opcao) {
      case 1 -> parteSQL = " data_pagamento = ? ";
      case 2 -> parteSQL = " desconto_inss = ? ";
      case 3 -> parteSQL = " desconto_ir = ? ";
      case 4 -> parteSQL = " salario_liquido = ?";
      case 5 -> parteSQL = " id_funcionario = ?";

      default -> throw new AssertionError("Opção inválida! -> parteSQL");
    }

    String comandoSQL = "UPDATE folha_pagamento SET" + parteSQL + " WHERE codigo = ?";

    try (
        Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL);) {

      switch (opcao) {
        case 1 -> stmt.setObject(1, folhaPagamento.getDataPagamento());
        case 2 -> stmt.setObject(1, folhaPagamento.getDescontoInss());
        case 3 -> stmt.setObject(1, folhaPagamento.getDescontoIR());
        case 4 -> stmt.setObject(1, folhaPagamento.getSalarioLiquido());
        case 5 -> stmt.setObject(1, folhaPagamento.getFuncionario().getId_funcionario());

        default -> throw new AssertionError("Opção inválida! -> stmt");
      }

      customLogger.logFolhaSucess(
          "Folha de Pagamento do(a) funcionário(a) '" + folhaPagamento.getFuncionario().getId_funcionario() + " "
              + folhaPagamento.getFuncionario().getNome() + "' atualizado!");
      stmt.executeUpdate();

    } catch (SQLException error) {
      customLogger.logError("Erro ao atualizar:");
      throw new RuntimeException(error.getMessage());
    }
  }

  // SELECT
  public void selecionarFolha(FolhaPagamento folhaPagamento, int opcao, String condicao) {

    String comandoSQL;
    if (opcao == 0) {
      comandoSQL = "SELECT * FROM folha_pagamento ORDER BY codigo DESC";
    } else {
      String parametroSQL;
      switch (opcao) {
        case 0 -> parametroSQL = "";
        case 1 -> parametroSQL = "codigo";
        case 2 -> parametroSQL = "data_pagamento";
        case 3 -> parametroSQL = "id_funcionario";
        default -> throw new AssertionError("Opção inválida! -> opcaoSQL");
      }
      comandoSQL = "SELECT * FROM folha_pagamento WHERE " + parametroSQL + " " + condicao + " ? ORDER BY codigo DESC;";
      /* selecionaFolha ( folhaPagamento, 1, "") */
    }

    try (
        Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL);) {

      if (opcao != 0) {
        switch (opcao) {
          case 1 -> stmt.setObject(1, folhaPagamento.getCodigo());
          case 2 -> stmt.setObject(1, folhaPagamento.getDataPagamento());
          case 3 -> stmt.setObject(1, folhaPagamento.getFuncionario().getId_funcionario());
        }
      }
      try (ResultSet resultado = stmt.executeQuery()) {
        System.out
            .println("---------------------------------------------------------------------------------------------");
        String formato = "| %-7s | %-14s | %-13s | %-11s | %-15s | %-14s |%n";
        System.out.printf(formato, "CÓDIGO", "DATA PAGAMENTO", "DESC. INSS", "DESC. IR", "SAL. LÍQUIDO", "ID FUNC.");

        System.out
            .println("---------------------------------------------------------------------------------------------");

        while (resultado.next()) {

          System.out.printf("| %-7d | %-14s | R$ %-10.2f | R$ %-8.2f | R$ %-12.2f | %-14d |%n",
              resultado.getInt("codigo"),
              resultado.getDate("data_pagamento"),
              resultado.getDouble("desconto_inss"),
              resultado.getDouble("desconto_ir"),
              resultado.getDouble("salario_liquido"),
              resultado.getInt("id_funcionario"));
        }
      }

      System.out
          .println("---------------------------------------------------------------------------------------------");

    } catch (Exception error) {
      customLogger.logError("Erro ao buscar dados da Folha de pagamento!");
      throw new RuntimeException(error.getMessage());
    }
  }
}
