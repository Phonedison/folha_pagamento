package sistema.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import sistema.model.FolhaPagamento;

public class FolhaPagamentoDAO {

  private ConexaoDB conexao;

  public FolhaPagamentoDAO(ConexaoDB conexao) {
    this.conexao = conexao;
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
      stmt.setObject(5, (folhaPagamento.getFuncionario()).getDependentes());
      stmt.executeUpdate();
      System.out.println("Folha de pagamento registrado XD");

    } catch (Exception error) {

      System.out.println("Error de inserção: \n" + error.getMessage());
      error.printStackTrace();

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

    String comandoSQL = "UPDATE folha_pagamento SET" + parteSQL + " WHERE codigo = ?;";

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

  // DELETE

}
