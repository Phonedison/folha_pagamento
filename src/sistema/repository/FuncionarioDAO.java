package sistema.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import sistema.model.Funcionario;

public class FuncionarioDAO implements CriacaoTabela {

  private final ConexaoDB conexao;

  public FuncionarioDAO(ConexaoDB conexao) {
    this.conexao = conexao;
  }

  @Override
  public void criarTabela() {
    String comandoSQL = "CREATE TABLE IF NOT EXISTS funcionario ("
        + " id_funcionario SERIAL PRIMARY KEY,"
        + " nome VARCHAR(100) NOT NULL,"
        + " cpf VARCHAR(14) UNIQUE NOT NULL,"
        + " data_nascimento DATE NOT NULL,"
        + " salario_bruto NUMERIC(15,2) NOT NULL" // Corrigido para NUMERIC
        + " );";

    try (
        Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL);) {

      stmt.execute();

    } catch (Exception error) {
      throw new RuntimeException("Erro ao inicializar tabela Funcionario: " + error.getMessage(),
          error);
    }

  }

  // INSET INTO
  public void salvarFuncionario(Funcionario funcionario) {
    String comandoSQL = "INSERT INTO funcionario (nome, cpf, data_nascimento, salario_bruto) VALUES (?, ?, ?, ?);";

    try (
        Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL);) {
      stmt.setObject(1, funcionario.getNome());
      stmt.setObject(2, funcionario.getCpf());
      stmt.setObject(3, funcionario.getDataNacimento());
      stmt.setObject(4, funcionario.getSalarioBruto());
      stmt.executeUpdate();
      System.out.println("Funcionário cadastrado no jiraiya XD");

    } catch (Exception error) {

      throw new RuntimeException("Erro ao atualizar: " + error.getMessage());

    }
  }

  // UPDATE
  public void atualizarFuncionario(Funcionario funcionario, int opcao) {
    String parteSQL;

    switch (opcao) {
      case 1 -> parteSQL = "nome = ?"; // nome
      case 2 -> parteSQL = "cpf = ?"; // cpf
      case 3 -> parteSQL = "data_nascimento = ?"; // data nascimento
      case 4 -> parteSQL = "salario_bruto = ?";
      default -> throw new AssertionError("Opção inválida! -> parteSQL");
    }

    String comandoSQL = "UPDATE funcionario SET " + parteSQL + " WHERE id_funcionario = ?; ";

    try (
        Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL);) {

      switch (opcao) {

        case 1 -> stmt.setObject(1, funcionario.getNome());
        case 2 -> stmt.setObject(1, funcionario.getCpf());
        case 3 -> stmt.setObject(1, funcionario.getDataNacimento());
        case 4 -> stmt.setDouble(1, funcionario.getSalarioBruto());

        default -> throw new AssertionError("Opção inválida! -> stmt");
      }
      stmt.setInt(2, funcionario.getId_funcionario());
      stmt.executeUpdate();

    } catch (SQLException error) {
      throw new RuntimeException("Erro ao atualizar: " + error.getMessage());
    }
  }

  // DELETE
  public void excluirFuncionario(int id_funcionario) {
    String comandoSQL = "DELETE FROM funcionario WHERE id_funcionario = ?;";

    try (
        Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL);) {

      /*
       * Ver se é possível adicionar um método de confirmação.
       */

      stmt.setObject(1, id_funcionario);
      stmt.executeLargeUpdate();
      System.out.println("Funcionário removido!");

    } catch (Exception error) {
      throw new RuntimeException("Erro ao deleter ao usuario: " + error.getMessage());
    }
  }

  // SELECT
  public void selecionarFuncionario(Funcionario funcionario, int opcao, String condicao) {
    String comandoSQL;

    if (opcao == 0) {
      comandoSQL = "SELECT * FROM funcionario ORDER BY id_funcionario DESC";
    } else {
      String parametroSQL;
      switch (opcao) {
        case 0 -> parametroSQL = ""; // tudo
        case 1 -> parametroSQL = "nome"; // nome
        case 2 -> parametroSQL = "cpf"; // cpf
        case 3 -> parametroSQL = "data_nascimento"; // data nascimento
        case 4 -> parametroSQL = "salario_bruto"; // data nascimento
        default -> throw new AssertionError("Opção inválida! -> opcaoSQL");
      }
      comandoSQL = "SELECT * FROM funcionario " + parametroSQL + " " + condicao + " ? ORDER BY id_funcionario DESC;";
      /* selecionarFuncionario ( funcionario, 2, "") */
    }

    try (
        Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL);) {

      if (opcao != 0) {
        switch (opcao) {

          case 1 -> stmt.setObject(1, funcionario.getNome());
          case 2 -> stmt.setObject(1, funcionario.getCpf());
          case 3 -> stmt.setObject(1, funcionario.getDataNacimento());
          case 4 -> stmt.setObject(1, funcionario.getSalarioBruto());

        }
      }

      try (ResultSet resultado = stmt.executeQuery()) {
        System.out.println("--- Relatório ---");
        System.out.println(" ");
        System.out.println("CÓDIGO | NOME | CPF | NASCIMENTO | SALARIO");
        System.out.println(" ------------------ ");

        /*
         * Imprime os dados após a busca utilizando o select armazenado na variavel
         * resultado.
         */
        while (resultado.next()) {

          System.out.printf("| %-5d | %-25s | %-15s | %-12s | R$ %-10.2f |%n",
              resultado.getInt("id_funcionario"),
              resultado.getString("nome"),
              resultado.getString("cpf"),
              resultado.getString("data_nascimento"),
              resultado.getDouble("salario_bruto"));
        }
      }

      // stmt.executeUpdate(); // -> Comando apenas para deleted, update e insert;

    } catch (SQLException error) {
      throw new RuntimeException("Erro ao buscar funcionário: " + error.getMessage());
    }

  }
}
