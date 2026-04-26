package sistema.repository.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import sistema.app.util.CustomLogger;
import sistema.exception.CpfDuplicado;
import sistema.model.Funcionario;
import sistema.repository.CriacaoTabela;
import sistema.repository.connection.DatabaseConfig;

public class FuncionarioDAO implements CriacaoTabela {
  private final DatabaseConfig conexao;

  public FuncionarioDAO(DatabaseConfig conexao) {
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
      CustomLogger.logError("Erro ao inicializar tabela Funcionario:");
      throw new RuntimeException(error.getMessage(), error);
    }
  }

  // INSET INTO
  public void salvarFuncionario(Funcionario funcionario) {

    String comandoSQL = "INSERT INTO funcionario (nome, cpf, data_nascimento, salario_bruto) VALUES (?, ?, ?, ?);";

    try (
        Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL, Statement.RETURN_GENERATED_KEYS);) {
      stmt.setObject(1, funcionario.getNome());
      stmt.setObject(2, funcionario.getCpf());
      stmt.setObject(3, funcionario.getDataNacimento());
      stmt.setObject(4, funcionario.getSalarioBruto());
      stmt.executeUpdate();

      CustomLogger.logSucess("Funcionário '" + funcionario.getNome() + "' Cadastrado com sucesso!");

      try (ResultSet rs = stmt.getGeneratedKeys()) {
        if (rs.next()) {
          funcionario.setId_funcionario((rs.getInt(1))); // setar para 0 com o intuito
        }
      }

    } catch (Exception error) {
      CustomLogger.logError("Erro ao inserir: " + funcionario.getNome());
      throw new RuntimeException("Erro: " + error.getMessage());
    }
  }

  // UPDATE
  public void atualizarFuncionario(Funcionario funcionario, int opcao, int id_funcionario) throws CpfDuplicado {
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

      stmt.setObject(2, id_funcionario);
      stmt.executeUpdate();

    } catch (SQLException error) {
      if (error.getMessage().contains("cpf")) {
        throw new CpfDuplicado("CPF já cadastrado! Tente novamente com outro CPF.");
      }
      throw new RuntimeException("Erro ao atualizar: " + error.getMessage());
    }
  }

  // DELETE
  public void excluirFuncionario(int id_funcionario) {
    String comandoSQL = "DELETE FROM funcionario WHERE id_funcionario = ?;";

    try (
        Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL);) {

      stmt.setObject(1, id_funcionario);
      int linhas = stmt.executeUpdate();

      if (linhas > 0) {
        CustomLogger.logWarning("Funcionário de ID '" + id_funcionario + "' removido!");
      } else {
        CustomLogger.logError("Funcionário não encontrado!");
      }

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
      comandoSQL = "SELECT * FROM funcionario WHERE" + parametroSQL + " " + condicao
          + " ? ORDER BY id_funcionario DESC;";
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
        if (resultado.next()) {
          System.out
              .println("------------------------------------------------------------------------------------------");
          String formato = "| %-5s | %-30s | %-15s | %-12s | %-13s |%n";
          System.out.printf(formato, "COD", "NOME", "CPF", "NASC.", "SALARIO");
          System.out
              .println("------------------------------------------------------------------------------------------");

          /*
           * Imprime os dados após a busca utilizando o select armazenado na variavel
           * resultado.
           */
          while (resultado.next()) {

            System.out.printf("| %-5d | %-30s | %-15s | %-12s | R$ %-10.2f |%n",
                resultado.getInt("id_funcionario"),
                resultado.getString("nome"),
                resultado.getString("cpf"),
                resultado.getString("data_nascimento"),
                resultado.getDouble("salario_bruto"));
          }

          System.out
              .println("------------------------------------------------------------------------------------------");
        } else {
          CustomLogger.logWarning("Tabela FUNCIONARIO vazio!");
        }
      }

      // stmt.executeUpdate(); // -> Comando apenas para deleted, update e insert;

    } catch (SQLException error) {
      CustomLogger.logError("Erro ao buscar dados dos funcionários!");
      throw new RuntimeException(error.getMessage());
    }

  }

  public Funcionario buscarPorCpf(String cpf) {

    String comandoSQL = "SELECT * FROM funcionario WHERE cpf = ?";
    Funcionario funcionario = null;

    try (Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL)) {

      stmt.setString(1, cpf);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          funcionario = new Funcionario();
          funcionario.setId_funcionario(rs.getInt("id_funcionario"));
          funcionario.setNome(rs.getString("nome"));
          funcionario.setCpf(rs.getString("cpf"));
          funcionario.setDataNacimento(rs.getDate("data_nascimento").toLocalDate());
          funcionario.setSalarioBruto(rs.getDouble("salario_bruto"));
        }
      }
    } catch (SQLException error) {
      CustomLogger.logWarning("Erro ao Encontrar Funcionário" + error.getMessage());

    }
    return funcionario;
  }

  public void selececionarQtdDependentePorFUncionario() {
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

    try (
        Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL);) {

      try (ResultSet resultado = stmt.executeQuery()) {
        System.out.println("------------------------------------------------");
        String formato = "| %-5s | %-30s | %-3s |%n";
        System.out.printf(formato, "COD", "NOME", "QTD");
        System.out.println("------------------------------------------------");

        /*
         * Imprime os dados após a busca utilizando o select armazenado na variavel
         * resultado.
         */
        while (resultado.next()) {

          System.out.printf("| %-5d | %-30s | %-3s |%n",
              resultado.getInt("id_funcionario"),
              resultado.getString("nome"),
              resultado.getInt("quantidade_dependentes"));
        }
        System.out.println("--------------------------------------------------");
      }

    } catch (SQLException error) {
      CustomLogger.logError("Erro ao buscar dados dos funcionários!");
      throw new RuntimeException(error.getMessage());
    }

  }

}
