package sistema.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import sistema.model.Funcionario;

public class FuncionarioDAO {

  private ConexaoDB conexao;

  public FuncionarioDAO(ConexaoDB conexao) {
    this.conexao = conexao;
  }

  // INSET INTO
  public void salvarFuncionario (Funcionario funcionario) {
    String comandoSQL = "INSERT INTO funcionario (nome, cpf, data_nascimento, salario_bruto) VALUES (?, ?, ?, ?, ?)";

    try (
      Connection con = conexao.conectarDB();
      PreparedStatement stmt = con.prepareStatement(comandoSQL);
    ) {
          stmt.setString(1, funcionario.getNome());
          stmt.setString(2, funcionario.getCpf());
          stmt.setDate(3, funcionario.getDataNacimento());
          stmt.setDouble(4, funcionario.getSalarioBruto());
          stmt.executeUpdate();
          System.out.println("Funcionário cadastrado no jiraiya XD");

    } catch (Exception error) {

      System.out.println("Error de inserção: \n"+ error.getMessage());
      error.printStackTrace();

    }
  }

  // UPDATE
  public void atualizarFuncionario (Funcionario funcionario, int opcao) {
      String parteSQL;

     switch (opcao) {
        case 1 ->   parteSQL = " nome = ? "; //nome
        case 2 ->   parteSQL = " cpf = ? ";  //cpf
        case 3 ->   parteSQL = " data_nascimento = ? "; //data nascimento
        case 4 ->   parteSQL = " salario_bruto = ?";     
        default ->  throw new AssertionError("Opção inválida! -> parteSQL");
      }

      String comandoSQL = "UPDATE funcionario SET" + parteSQL + " WHERE id_funcionario = ?";

     try (
        Connection  con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL);
      ) {

        switch (opcao) {
          case 1 ->   stmt.setDouble(1, funcionario.getId_funcionario());
          case 2 ->   stmt.setString(1, funcionario.getNome());
          case 3 ->   stmt.setString(1, funcionario.getCpf());
          case 4 ->   stmt.setDate(1, funcionario.getDataNacimento());
          default ->  throw new AssertionError("Opção inválida! -> stmt");
        }

        stmt.setInt(2,funcionario.getId_funcionario());
        stmt.executeUpdate();

      } catch (SQLException error) {
        throw new RuntimeException("Erro ao atualizar: " + error.getMessage());
      }
  }

  // DELETE
  public void excluirFuncionario (int id_funcionario) {
    String comandoSQL = "DELETE FROM funcionario WHERE id_funcionario = ?";

    try (
      Connection con = conexao.conectarDB();
      PreparedStatement stmt = con.prepareStatement(comandoSQL);
    ) {

      /*
      Ver se é possível adicionar um método de confirmação.
      */
      
      stmt.setInt(1, id_funcionario);
      stmt.executeLargeUpdate();
      System.out.println("Funcionário removido!");

      } catch (Exception error) {
        throw new RuntimeException("Erro ao deleter ao usuario: " + error.getMessage());
    }
  }

  // SELECT
  public void selecionarFuncionario () {

  }

}
