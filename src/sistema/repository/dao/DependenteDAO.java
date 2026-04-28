package sistema.repository.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static sistema.app.util.CustomLogger.logDependenteError;
import static sistema.app.util.CustomLogger.logError;
import static sistema.app.util.CustomLogger.logSucess;
import static sistema.app.util.CustomLogger.logWarning;
import sistema.model.Dependente;
import sistema.repository.connection.DatabaseConfig;

public class DependenteDAO extends BaseDAO implements GenericDAO<Dependente> {

  public DependenteDAO(DatabaseConfig db) {
    super(db);
  }

  @Override
  public void atualizar(Dependente dependente, int id, int opcao) {

    StringBuilder comandoSQL = new StringBuilder("UPDATE dependente SET ");

    List<Object> parametro = new ArrayList<>();

    switch (opcao) {
      case 0 -> {
        comandoSQL.append("nome = ?, cpf = ?, data_nascimento = ?, parentesco = ?, id_funcionario = ?");
        parametro.add(dependente.getNome());
        parametro.add(dependente.getCpf());
        parametro.add(dependente.getDataNascimento());
        parametro.add(dependente.getParentesco().name());
        parametro.add(dependente.getIdFuncionario());

      }
      case 1 -> {
        comandoSQL.append("nome = ?");
        parametro.add(dependente.getNome());
      }

      case 2 -> {
        comandoSQL.append("cpf = ?");
        parametro.add(dependente.getCpf());
      }

      case 3 -> {
        comandoSQL.append("data_nascimento = ?");
        parametro.add(dependente.getDataNascimento());
      }

      case 4 -> {
        comandoSQL.append("parentesco = ?");
        parametro.add(dependente.getParentesco().name());
      }

      case 5 -> {
        comandoSQL.append("parentesco = ?");
        parametro.add(dependente.getIdFuncionario());
      }

      default -> {
        logWarning("Opção inválida!");
        return;
      }
    }
    comandoSQL.append(" WHERE id_dependente = ?");
    parametro.add(id);

    try {
      executeUpdate(comandoSQL.toString(), parametro.toArray());
      logSucess("Dependente ID " + id + " atualizado!");

    } catch (Exception error) {
      logError("Erro ao Atualizar dependente: " + error.getMessage());
      throw new RuntimeException(error.getMessage(), error);
    }

  }

  @Override
  public void excluir(int id) {
    String comandoSQL = "DELETE FROM dependente WHERE id_dependente = ?";
    executeUpdate(comandoSQL, id);
    logSucess("Dependente ID" + id + "excluído!");
  }

  @Override
  public List<Dependente> listarTodos(int opcao) {
    StringBuilder comandoSQL = new StringBuilder("SELECT * FROM dependente ORDER BY");
    comandoSQL.append(" ");
    switch (opcao) {
      case 1 -> comandoSQL.append("id_dependente DESC;");
      case 2 -> comandoSQL.append("nome DESC;");
      case 3 -> comandoSQL.append("cpf DESC;");
      case 4 -> comandoSQL.append("data_nascimento DESC;");
      case 5 -> comandoSQL.append("parentesco::parentesco DESC;");
      case 6 -> comandoSQL.append("id_funcionario DESC;");
      default -> {
        logWarning("Opção inválida!");
        return new ArrayList<>(); // evita NullPointerException no chamador
      }
    }

    List<Dependente> lista = new ArrayList<>();

    try (
        Connection conexao = db.conectarDB();
        PreparedStatement stmt = conexao.prepareStatement(comandoSQL.toString());
        ResultSet rs = stmt.executeQuery();) {

      while (rs.next()) {
        lista.add(mapearDependente(rs));
      }

    } catch (Exception error) {
      logDependenteError("Erro ao listar Dependentes: " + error.getMessage());
      throw new RuntimeException(error.getMessage(), error);
    }

    return lista;
  }

  @Override
  public void salvar(Dependente dependente) {

    String comandoSQL = "INSERT INTO dependente (nome, cpf, data_nascimento, parentesco, id_funcionario) VALUES (?, ?, ?, ?::parentesco, ?)";

    executeUpdate(
        comandoSQL,
        dependente.getNome(),
        dependente.getCpf(),
        dependente.getDataNascimento(),
        dependente.getParentesco().name(),
        dependente.getIdFuncionario());

    logSucess("Dependente '" + dependente.getNome() + "' salvo com sucesso!");
  }

  private Dependente mapearDependente(ResultSet rs) throws SQLException {

    Dependente dependente = new Dependente();

    dependente.setIdDependente(rs.getInt("id_dependente"));
    dependente.setNome(rs.getString("nome"));
    dependente.setCpf(rs.getString("cpf"));
    dependente.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
    dependente.escolherParentesco(rs.getString("parentesco"));
    dependente.setIdFuncionario(rs.getInt("id_funcionario"));

    return dependente;
  }

  public Dependente buscarPorCpf(String cpf) {

    String comandoSQL = "SELECT * FROM dependente WHERE cpf = ?";

    try (
        Connection conexao = db.conectarDB();
        PreparedStatement stmt = conexao.prepareStatement((comandoSQL));) {
      stmt.setString(1, cpf);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next())
          return mapearDependente(rs);
      }

    } catch (Exception error) {
      logError("Erro ao buscar dependente por CPF: " + error.getMessage());
    }
    return null;
  }

}