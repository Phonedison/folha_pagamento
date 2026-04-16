package sistema.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import sistema.model.Dependente;

public class DependenteDAO implements CriacaoTabela {

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
      throw new RuntimeException("Erro ao inicializar tabela Dependente: " + error.getMessage(),
          error); // executa uma mensagem informando o erro e mostrando o erro, facilitando a
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

    } catch (Exception error) {
      throw new RuntimeException("Erro na inserção: " + error.getMessage());
    }
  }

  // UPDATE
  public void atualizarDependente(Dependente dependente, int opcao, int id_dependente) {
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
        case 3 -> stmt.setObject(1, dependente.getDataNacimento());
        case 4 -> stmt.setObject(1, dependente.getParentesco().name());
        case 5 -> stmt.setObject(1, dependente.getFuncionario());

        default -> throw new AssertionError("Opção inválida! -> stmt");
      }

      stmt.setObject(2, id_dependente);
      stmt.executeUpdate();

    } catch (Exception error) {
      throw new RuntimeException("Erro ao atualizar: " + error.getMessage());
    }
  }

  // DELETE
  public void excluirDependente(int id_dependente) {
    String comandoSQL = "DELETE FROM dependente WHERE id_dependente = ?";

    try (
        Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL);) {

      stmt.setObject(1, id_dependente);
      int linhas = stmt.executeUpdate();

      if (linhas > 0) {
        System.out.println("Dependente removido!");
      } else {
        System.out.println("Dependente não encontrado!");
      }

    } catch (Exception error) {
      throw new RuntimeException("Erro ao deletar dependente: " + error.getMessage());
    }
  }

  // SELECT
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

      try (ResultSet resultado = stmt.executeQuery()) {
        System.out
            .println(
                "--------------------------------------------------------------------------------------------------");
        String formato = "| %-5s | %-25s | %-15s | %-12s | %-12s | %-10s |%n";
        System.out.printf(formato, "ID", "NOME", "CPF", "NASC.", "PARENTESCO", "ID FUN.");
        System.out
            .println(
                "--------------------------------------------------------------------------------------------------");

        while (resultado.next()) {

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
      }

    } catch (Exception error) {
      throw new RuntimeException("Erro ao buscar dependente: " + error.getMessage(), error);
    }
  }
}
