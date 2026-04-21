package sistema.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import sistema.app.menu.CustomLogger;
import sistema.exception.CpfDuplicado;
import sistema.model.Funcionario;

public class FuncionarioDAO implements CriacaoTabela {

  CustomLogger customLogger = new CustomLogger();
  private final ConexaoDB conexao;

  public FuncionarioDAO(ConexaoDB conexao) {
    this.conexao = conexao;
  }

  @Override
  public void criarTabela() {
    // String com comando SQL para criar a tabela "funcionario" caso ela não exista
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

      // Executa o comando (criação da tabela)
      stmt.execute();

    } catch (Exception error) {
      customLogger.logError("Erro ao inicializar tabela Funcionario:");
      throw new RuntimeException(error.getMessage(), error);
    }
  }

  // INSET INTO
  // Método para inserir um funcionário no banco
  public void salvarFuncionario(Funcionario funcionario) {
    //Comando SQL de inserção com parâmetros
    String comandoSQL = "INSERT INTO funcionario (nome, cpf, data_nascimento, salario_bruto) VALUES (?, ?, ?, ?);";

    try (
        Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL, Statement.RETURN_GENERATED_KEYS);) {
      stmt.setObject(1, funcionario.getNome());
      stmt.setObject(2, funcionario.getCpf());
      stmt.setObject(3, funcionario.getDataNacimento());
      stmt.setObject(4, funcionario.getSalarioBruto());
      stmt.executeUpdate();

      customLogger.logSucess("Funcionário '" + funcionario.getNome() + "' Cadastrado com sucesso!");

      try (ResultSet rs = stmt.getGeneratedKeys()) {
        if (rs.next()) {
          funcionario.setId_funcionario((rs.getInt(1))); // setar para 0 com o intuito
        }
      }

    } catch (Exception error) {
      customLogger.logError("Erro ao inserir: " + funcionario.getNome());
      throw new RuntimeException("Erro: " + error.getMessage());
    }
  }

  // UPDATE
  // Método para atualizar dados de um funcionário
  public void atualizarFuncionario(Funcionario funcionario, int opcao, int id_funcionario) throws CpfDuplicado {
    String parteSQL;

    // Define qual campo será atualizado com base na opção
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

      // Define valor do campo baseado na opção
      switch (opcao) {

        case 1 -> stmt.setObject(1, funcionario.getNome());
        case 2 -> stmt.setObject(1, funcionario.getCpf());
        case 3 -> stmt.setObject(1, funcionario.getDataNacimento());
        case 4 -> stmt.setDouble(1, funcionario.getSalarioBruto());

        default -> throw new AssertionError("Opção inválida! -> stmt");
      }
      // Define ID do funcionário
      stmt.setObject(2, id_funcionario);
      // Executa UPDATE
      stmt.executeUpdate();

    } catch (SQLException error) {
      if (error.getMessage().contains("cpf")) {
        throw new CpfDuplicado("CPF já cadastrado! Tente novamente com outro CPF.");
      }
      throw new RuntimeException("Erro ao atualizar: " + error.getMessage());
    }
  }

  // DELETE
  // Método para excluir funcionário pelo ID
  public void excluirFuncionario(int id_funcionario) {
    String comandoSQL = "DELETE FROM funcionario WHERE id_funcionario = ?;";

    try (
        Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL);) {

      // Define ID
      stmt.setObject(1, id_funcionario);
      // Executa DELETE e retorna quantas linhas foram afetadas
      int linhas = stmt.executeUpdate();

      if (linhas > 0) {
        customLogger.logWarning("Funcionário de ID '" + id_funcionario + "' removido!");
      } else {
        customLogger.logError("Funcionário não encontrado!");
      }

    } catch (Exception error) {
      throw new RuntimeException("Erro ao deleter ao usuario: " + error.getMessage());
    }
  }

  // SELECT
  // Método para selecionar funcionários
  public void selecionarFuncionario(Funcionario funcionario, int opcao, String condicao) {
    String comandoSQL;

    // Se opção for 0 → busca todos
    if (opcao == 0) {
      comandoSQL = "SELECT * FROM funcionario ORDER BY id_funcionario DESC";
    } else {
      String parametroSQL;
      // Define qual campo será usado no filtro
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
        if (resultado != null) {
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
          customLogger.logWarning("Tabela FUNCIONARIO vazio!");
        }
      }

      // stmt.executeUpdate(); // -> Comando apenas para deleted, update e insert;

    } catch (SQLException error) {
      customLogger.logError("Erro ao buscar dados dos funcionários!");
      throw new RuntimeException(error.getMessage());
    }

  }
  // Busca um funcionário pelo CPF
  public Funcionario buscarPorCpf(String cpf) {

    String comandoSQL = "SELECT * FROM funcionario WHERE cpf = ?";
    Funcionario funcionario = null;

    try (Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL)) {

      stmt.setString(1, cpf);

      try (ResultSet rs = stmt.executeQuery()) {
        // Se encontrou resultado
        if (rs.next()) {

          // Cria objeto
          funcionario = new Funcionario();

          // Preenche os dados
          funcionario.setId_funcionario(rs.getInt("id_funcionario"));
          funcionario.setNome(rs.getString("nome"));
          funcionario.setCpf(rs.getString("cpf"));
          funcionario.setDataNacimento(rs.getDate("data_nascimento").toLocalDate());
          funcionario.setSalarioBruto(rs.getDouble("salario_bruto"));
        }
      }
    } catch (SQLException error) {
      customLogger.logWarning("Erro ao Encontrar Funcionário" + error.getMessage());

    }
    return funcionario;
  }

  // Método que mostra quantidade de dependentes por funcionário
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
      customLogger.logError("Erro ao buscar dados dos funcionários!");
      throw new RuntimeException(error.getMessage());
    }

  }

}
