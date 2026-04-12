package sistema.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import sistema.model.Dependente;

public class DependenteDAO {

  private ConexaoDB conexao;

  public DependenteDAO(ConexaoDB conexao) {
    this.conexao = conexao;
  }

  // INSET INTO
  public void salvarDependente(Dependente dependente) {
    String comandoSQL = "INSERT INTO dependente (nome, cpf, data_nascimento, parentesco, id_funcionario) VALUES (?, ?, ?, ?, ?);";

    try (
        Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL);) {
      stmt.setObject(1, dependente.getNome());
      stmt.setObject(2, dependente.getCpf());
      stmt.setObject(3, dependente.getDataNacimento());
      stmt.setObject(4, dependente.getParentesco());
      stmt.setObject(5, dependente.getFuncionario());

    } catch (Exception e) {
      // TODO: handle exception
    }
  }

  // UPDATE
  public void atualizarDependente(Dependente dependente, int opcao) {
    String parteSQL;

    switch (opcao) {
      case 1 -> parteSQL = "nome = ?";
      case 2 -> parteSQL = "cpf = ?";
      case 3 -> parteSQL = "data_nascimento = ?";
      case 4 -> parteSQL = "parentesco = ?";
      case 5 -> parteSQL = "id_funcionario = ?";
      default -> throw new AssertionError("Opção inválida! -> parteSQL");
    }

    String comandoSQL = "UPDATE dependente SET " + parteSQL + " WHERE id_dependente = ?";

    try (
        Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL);) {

      switch (opcao) {
        case 1 -> stmt.setObject(1, dependente.getNome());
        case 2 -> stmt.setObject(1, dependente.getCpf());
        case 3 -> stmt.setObject(1, dependente.getParentesco());
        case 4 -> stmt.setObject(1, dependente.getFuncionario());

        default -> throw new AssertionError("Opção inválida! -> stmt");
      }

      stmt.executeUpdate();

    } catch (Exception error) {
      throw new RuntimeException("Erro ao atualizar: " + error.getMessage());
    }
  }

  // DELETE
  public void excluirDependente(int id_dependente, int opcao) {
    String comandoSQL = "DELETE FROM dependente WHERE id_dependente = ?";

    try (
        Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL);) {

      stmt.setObject(1, id_dependente);
      stmt.executeLargeUpdate();

    } catch (Exception error) {
      throw new RuntimeException("Erro ao deletar dependente: " + error.getMessage());
    }
  }

  // SELECT
  public void selecionarDependente(Dependente dependente, int opcao, String condicao) {
    String comandoSQL;

    if (opcao == 0) {
      comandoSQL = "SELECT * FROM dependente ORDER BY id_dependente DESC";
    } else {
      String parametroSQL;
      switch (opcao) {
        case 0 -> parametroSQL = "";
        case 1 -> parametroSQL = "nome";
        case 2 -> parametroSQL = "cpf";
        case 3 -> parametroSQL = "parentesco";
        case 4 -> parametroSQL = "id_funcionario";

        default -> throw new AssertionError("Opção inválida! -> opcaoSQL");
      }
      comandoSQL = "SELECT * FROM dependente " + parametroSQL + " " + condicao + " ? ORDER BY id_dependente DESC;";
    }

    try (
        Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL);) {

      if (opcao != 0) {
        switch (opcao) {

          case 1 -> stmt.setObject(1, dependente.getNome());
          case 2 -> stmt.setObject(1, dependente.getCpf());
          case 3 -> stmt.setObject(1, dependente.getParentesco());
          case 4 -> stmt.setObject(1, dependente.getFuncionario());
        }
      }
      try (ResultSet resultado = stmt.executeQuery()) {
        System.out.println(" --- Relatório --- ");
        System.out.println(" ");
        System.out.println("CÓDIGO | NOME DEPENDENTE | CPF | DATA NASCIMENTO | PARENTESCO | ID FUNCIONARIO");
        System.out.println(" ------------------ ");

        while (resultado.next()) {
          System.out.println(resultado.getInt("id_dependente") + " | "
              + resultado.getNString("nome") + " | "
              + resultado.getNString("cpf") + " | "
              + resultado.getDate("data_nascimento") + " | "
              + resultado.getNString("parentesco") + " | "
              + resultado.getInt("id_funcionario"));
        }
      }

    } catch (Exception error) {
      throw new RuntimeException("Erro ao buscar dempendente: " + error.getMessage());
    }
  }
}
