package sistema.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import sistema.model.FolhaPagamento;

public class FolhaPagamentoDAO implements CriacaoTabela {

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
      throw new RuntimeException("Erro ao inicializar tabela Folha_pagamento: " + error.getMessage(),
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
      System.out.println("Folha de pagamento registrado XD");

    } catch (Exception error) {

      throw new RuntimeException("Erro na inserção: " + error.getMessage());

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

      stmt.executeUpdate();

    } catch (SQLException error) {
      throw new RuntimeException("Erro ao atualizar: " + error.getMessage());
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
        System.out.println("--- Relatório ---");
        System.out.println(" ");
        System.out.println("CÓDIGO | DATA PAGAMENTO | DESCONTO INSS | DESCONTO IR | SALARIO LIQUIDO | ID FUNCIONARIO");
        System.out.println(" ------------------ ");
        /*
         * APENAS UM TESTE
         * Imprime os dados após a busca utilizando o select armazenado na variavel
         * resultado, enquanto for passado com valor
         */
        while (resultado.next()) {

          System.out.println(resultado.getInt("codigo") + " | "
              + resultado.getDate("data_pagamento") + " | "
              + resultado.getDouble("desconto_inss") + " | "
              + resultado.getDouble("desconto_ir") + " | "
              + resultado.getDouble("salario_liquido") + " | "
              + resultado.getInt("id_funcionario"));
        }
      }

    } catch (Exception error) {
      throw new RuntimeException("Erro ao buscar folha: " + error.getMessage());
    }
  }
}
