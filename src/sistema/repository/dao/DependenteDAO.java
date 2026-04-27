package sistema.repository.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sistema.app.util.CustomLogger;
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
        parametro.add(dependente.getDataNacimento());
        parametro.add(dependente.getParentesco().name());
        parametro.add(dependente.getFuncionario());

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
        parametro.add(dependente.getDataNacimento());
      }

      case 4 -> {
        comandoSQL.append("parentesco = ?");
        parametro.add(dependente.getParentesco().name());
      }

      case 5 -> {
        comandoSQL.append("parentesco = ?");
        parametro.add(dependente.getFuncionario());
      }

      default -> {
        CustomLogger.logWarning("Opção inválida!");
        return;
      }
    }
    comandoSQL.append(" WHERE id_dependente = ?");
    parametro.add(id);

    try {
      executeUpdate(comandoSQL.toString(), parametro.toArray());
      CustomLogger
          .logSucess("Dependente " + dependente.getIdDependente() + " - " + dependente.getNome() + "Atualizado!");

    } catch (Exception e) {
      CustomLogger.logError("Erro ao Atualizar dependente: ");
      throw new RuntimeException(e.getMessage(), e);
    }

  }

  @Override
  public void excluir(int id) {
    String comandoSQL = "DELETE FROM funcionario WHERE id_dependente = ?";
    executeUpdate(comandoSQL, id);
  }

  @Override
  public List<Dependente> listarTodos(int opcao) {
    StringBuilder comandoSQL = new StringBuilder("SELECT * FROM dependente ORDER BY");

    switch (opcao) {
      case 1 -> comandoSQL.append("id_dependente DESC;");
      case 2 -> comandoSQL.append("nome DESC;");
      case 3 -> comandoSQL.append("cpf DESC;");
      case 4 -> comandoSQL.append("data_nascimento DESC;");
      case 5 -> comandoSQL.append("parentesco::parentesco DESC;");
      case 6 -> comandoSQL.append("id_funcionario DESC;");
      default -> {
        CustomLogger.logWarning("Opção inválida!");
        return null;
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

    } catch (Exception e) {
      CustomLogger.logDependenteError("Erro ao listar Dependentes");
      throw new RuntimeException(e.getMessage(), e);
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
        dependente.getDataNacimento(),
        dependente.getParentesco().name(),
        dependente.getFuncionario());
  }

  private Dependente mapearDependente(ResultSet rs) throws SQLException {

    Dependente dependente = new Dependente();

    dependente.setIdDependente(rs.getInt("id_dependente"));
    dependente.setNome(rs.getString("nome"));
    dependente.setCpf(rs.getString("cpf"));
    dependente.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
    dependente.escolherParentesco(rs.getString("parentesco::parentesco"));
    dependente.setFuncionario(rs.getInt("id_funcionario"));

    return dependente;
  }

}