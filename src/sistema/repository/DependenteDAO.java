package sistema.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import sistema.app.menu.CustomLogger;
import sistema.model.Dependente;

public class DependenteDAO implements CriacaoTabela {

  CustomLogger customLogger = new CustomLogger();

  private final ConexaoDB conexao;

  // constructo para receber a conexao do banco
  public DependenteDAO(ConexaoDB conexao) {
    this.conexao = conexao;
  }

  @Override
  // Comando para criar tabela, informando os atributos e propriedades delas
  public void criarTabela() {

    String comandoSQL = "CREATE TABLE IF NOT EXISTS dependente ( "
        + " id_dependente SERIAL PRIMARY KEY,"
        + " nome VARCHAR(100) NOT NULL,"
        + " cpf VARCHAR(14) UNIQUE NOT NULL,"
        + " data_nascimento DATE NOT NULL,"
        + " parentesco parentesco NOT NULL,"
        + " id_funcionario INT REFERENCES funcionario(id_funcionario) NOT NULL"
        + " );";

    try (

        Connection con = conexao.conectarDB(); // tenta executar a conexao do banco
        PreparedStatement stmt = con.prepareStatement(comandoSQL);) {
      /* ^^ cria um molde do comando sql que foi passado utilizando comandoSQL */
      stmt.execute(); // < exectua o comando molde

    } catch (Exception error) { // caso ocorra um erro
      customLogger.logWarning("Erro ao inicializar tabela Dependente: ");
      throw new RuntimeException(error.getMessage(), error); // executa uma mensagem informando o erro e mostrando o
                                                             // erro, facilitando a
      // correção do erro
    }

  }

  // INSET INTO
  public void salvarDependente(Dependente dependente) {
    // comando sql para inserir valores, ? representa os valores passados pelo get
    // das outras classes / objetos
    String comandoSQL = "INSERT INTO dependente (nome, cpf, data_nascimento, parentesco, id_funcionario) VALUES (?, ?, ?, ?::parentesco, ?);";

    try (
        Connection con = conexao.conectarDB(); // cria a conexao
        PreparedStatement stmt = con.prepareStatement(comandoSQL);) { // prepara o comando
      stmt.setObject(1, dependente.getNome());
      // prepara o parametro com base no tipo de valor necessário para o sql
      // utilizando setObject que converte para o padrão de configuração
      // esperado
      stmt.setObject(2, dependente.getCpf());
      stmt.setObject(3, dependente.getDataNacimento());
      stmt.setObject(4, dependente.getParentesco() != null
          ? dependente.getParentesco().name()
          : null);// converte o enum para string utilizando o name();
      stmt.setObject(5, dependente.getFuncionario());

      stmt.executeUpdate();
      customLogger.logDependenteSucess("Dependente '" + dependente.getNome() + "' Cadastrado com sucesso!");

    } catch (Exception error) {
      customLogger.logError("Erro na inserção: ");
      throw new RuntimeException(error.getMessage());
    }
  }

  // UPDATE
  // Método para atualizar dados de um dependente
  public void atualizarDependente(Dependente dependente, int opcao, int id_dependente) {
    String parteSQL;

    // Define qual campo será atualizado com base na opção escolhida
    switch (opcao) {
      case 1 -> parteSQL = "nome = ?";
      case 2 -> parteSQL = "cpf = ?";
      case 3 -> parteSQL = "data_nascimento = ?";
      case 4 -> parteSQL = "parentesco = ?";
      case 5 -> parteSQL = "id_funcionario = ?";
      default -> throw new AssertionError("Opção inválida! -> parteSQL");
    }
    // Monta o comando SQL de UPDATE dinamicamente,
    // substituindo apenas o campo escolhido (parteSQL)
    String comandoSQL = "UPDATE dependente SET " + parteSQL + " WHERE id_dependente = ?";

    try (
         // Abre conexão com o banco
        Connection con = conexao.conectarDB();
         // Prepara o comando SQL para execução
        PreparedStatement stmt = con.prepareStatement(comandoSQL);) {

      // Define o valor do campo que será atualizado
      switch (opcao) {
        case 1 -> stmt.setObject(1, dependente.getNome());
        case 2 -> stmt.setObject(1, dependente.getCpf());
        case 3 -> stmt.setObject(1, dependente.getDataNacimento());
        case 4 -> stmt.setObject(1, dependente.getParentesco().name());
        case 5 -> stmt.setObject(1, dependente.getFuncionario());

        default -> throw new AssertionError("Opção inválida! -> stmt");
      }

      // Define o ID do dependente que será atualizado
      stmt.setObject(2, id_dependente);
      // Executa o UPDATE
      stmt.executeUpdate();

    } catch (Exception error) {
      customLogger.logError("Erro ao atualizar: ");
      throw new RuntimeException(error.getMessage());
    }
  }

  // DELETE
  // Método para excluir um dependente pelo ID
  public void excluirDependente(int id_dependente) {
    // Comando SQL de exclusão
    String comandoSQL = "DELETE FROM dependente WHERE id_dependente = ?";

    try (
        // Conexão com banco
        Connection con = conexao.conectarDB();
        // Prepara SQL
        PreparedStatement stmt = con.prepareStatement(comandoSQL);) {

      // Define o ID que será deletado
      stmt.setObject(1, id_dependente);
      // Executa o DELETE e retorna quantidade de linhas afetadas
      int linhas = stmt.executeUpdate();

      // Se deletou pelo menos 1 linha → sucesso
      if (linhas > 0) {
        customLogger.logSucess("Dependente removido!");
      } else {
        customLogger.logWarning("Dependente não encontrado!");
      }

    } catch (Exception error) {
      customLogger.logError("Erro ao deletar dependente:");
      throw new RuntimeException(error.getMessage());
    }
  }

  // SELECT
  // Método para buscar dependentes com ou sem filtro
  public void selecionarDependente(Dependente dependente, int opcao, String condicao) {

    String comandoSQL;

    // SELECT padrão (sem filtro)
    if (opcao == 0) {
      comandoSQL = """
              SELECT
                  id_dependente,
                  nome,
                  cpf,
                  data_nascimento,
                  parentesco::text AS parentesco,
                  id_funcionario
              FROM dependente
              ORDER BY id_dependente DESC
          """;
    } else {

      String parametroSQL;

      // Define qual campo será usado no filtro
      switch (opcao) {
        case 1 -> parametroSQL = "nome";
        case 2 -> parametroSQL = "cpf";
        case 3 -> parametroSQL = "parentesco";
        case 4 -> parametroSQL = "id_funcionario";
        default -> throw new AssertionError("Opção inválida! -> opcaoSQL");
      }

      comandoSQL = """
          SELECT
              id_dependente,
              nome,
              cpf,
              data_nascimento,
              parentesco::text AS parentesco,
              id_funcionario
          FROM dependente
          WHERE """ + parametroSQL + " " + condicao +
          (opcao == 3 ? " ?::parentesco " : " ? ") +
          "ORDER BY id_dependente DESC";
    }

    try (
        Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL);) {

      // SET dos parâmetros
      if (opcao != 0) {
        switch (opcao) {
          case 1 -> stmt.setString(1, dependente.getNome());
          case 2 -> stmt.setString(1, dependente.getCpf());
          case 3 -> stmt.setString(1, dependente.getParentesco().name());
          case 4 -> stmt.setInt(1, dependente.getFuncionario());
        }
      }
      // Executa a consulta
      try (ResultSet resultado = stmt.executeQuery()) {
        // Se resultado não for nulo
        if (resultado != null) {
          // Cabeçalho da tabela no console
          System.out
              .println(
                  "--------------------------------------------------------------------------------------------------");
          String formato = "| %-5s | %-25s | %-15s | %-12s | %-12s | %-10s |%n";
          System.out.printf(formato, "ID", "NOME", "CPF", "NASC.", "PARENTESCO", "ID FUN.");
          System.out
              .println(
                  "--------------------------------------------------------------------------------------------------");

          while (resultado.next()) {
            // Imprime os dados formatados
            System.out.printf(formato,
                resultado.getInt("id_dependente"),
                resultado.getString("nome"),
                resultado.getString("cpf"),
                resultado.getDate("data_nascimento"),
                resultado.getString("parentesco"),
                resultado.getInt("id_funcionario"));
          }

          System.out
              .println(
                  "--------------------------------------------------------------------------------------------------");
        } else {
          // Caso não haja dados
          customLogger.logWarning("Tabela DEPENDENTE vazio!");
        }
      }

    } catch (Exception error) {
      customLogger.logError("Erro ao buscar dependente: ");
      throw new RuntimeException(error.getMessage(), error);
    }
  }
}
