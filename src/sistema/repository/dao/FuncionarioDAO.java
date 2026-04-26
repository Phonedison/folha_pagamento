package sistema.repository.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sistema.app.util.CustomLogger;
import sistema.model.Funcionario;
import sistema.repository.connection.DatabaseConfig;

public class FuncionarioDAO extends BaseDAO implements GenericDAO<Funcionario> {

  public FuncionarioDAO(DatabaseConfig db) {
    super(db);
  }

  @Override
  public void salvar(Funcionario funcionario) {

    String comandoSQL = "INSERT INTO funcionario (nome, cpf, data_nascimento, salario_bruto) VALUES (?, ?, ?, ?);";

    executeUpdate(
        comandoSQL,
        funcionario.getNome(),
        funcionario.getCpf(),
        funcionario.getDataNacimento(),
        funcionario.getSalarioBruto());
  }

  @Override
  public List<Funcionario> listarTodos(int opcao) {
    StringBuilder comandoSQL = new StringBuilder("SELECT * FROM funcionario ORDER BY ");

    switch (opcao) {
      case 1 -> comandoSQL.append("id_funcionario DESC;");
      case 2 -> comandoSQL.append("nome DESC;");
      case 3 -> comandoSQL.append("cpf DESC;");
      case 4 -> comandoSQL.append("data_nascimento DESC;");
      case 5 -> comandoSQL.append("salario_bruto DESC;");
      default -> {
        CustomLogger.logWarning("Opção inválida!");
        return null;
      }
    }

    List<Funcionario> lista = new ArrayList<>();

    try (
        Connection conexao = db.conectarDB();
        PreparedStatement stmt = conexao.prepareStatement(comandoSQL.toString());
        ResultSet rs = stmt.executeQuery();) {

      while (rs.next()) {
        lista.add(mapearFuncionario(rs));
      }

    } catch (Exception error) {
      CustomLogger.logFuncionarioError("Erro ao listar Funcionários");
      throw new RuntimeException(error.getMessage(), error);
    }
    return lista;
  }

  @Override // Método de exclusão com base no ID e passando um parametro a ser atualizado
  public void atualizar(Funcionario funcionario, int id, int opcao) {

    StringBuilder comandoSQL = new StringBuilder("UPDATE funcionario SET ");

    List<Object> parametro = new ArrayList<>();

    switch (opcao) {
      case 0 -> {
        comandoSQL.append("nome = ?, cpf = ?, data_nascimento = ?, salario_bruto = ?");
        parametro.add(funcionario.getNome());
        parametro.add(funcionario.getCpf());
        parametro.add(funcionario.getDataNacimento());
        parametro.add(funcionario.getSalarioBruto());
      }

      case 1 -> {
        comandoSQL.append("nome = ?");
        parametro.add(funcionario.getNome());
      }

      case 2 -> {
        comandoSQL.append("cpf = ?");
        parametro.add(funcionario.getCpf());
      }

      case 3 -> {
        comandoSQL.append("data_nascimento = ?");
        parametro.add(funcionario.getDataNacimento());
      }

      case 4 -> {
        comandoSQL.append("salario_bruto = ?");
        parametro.add(funcionario.getSalarioBruto());
      }

      default -> {
        CustomLogger.logWarning("Opção inválida!");
        return;
      }
    }
    comandoSQL.append(" WHERE id_funcionario = ?");
    parametro.add(id);

    try {
      executeUpdate(comandoSQL.toString(), parametro.toArray());
      CustomLogger
          .logSucess("Funcionario " + funcionario.getId_funcionario() + " - " + funcionario.getNome() + " Atualizado!");

    } catch (Exception e) {

      CustomLogger.logError("Erro ao Atualizar funcionario: ");
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  @Override // Método de exclusão por ID
  public void excluir(int id) {
    String comandoSQL = "DELETE FROM funcionario WHERE id_funcionario = ?";
    executeUpdate(comandoSQL, id);
  }

  // Método para mapear e evitar repetição
  private Funcionario mapearFuncionario(ResultSet rs) throws SQLException {

    Funcionario funcionario = new Funcionario();

    funcionario.setId_funcionario(rs.getInt("id_funcionario"));
    funcionario.setNome(rs.getString("nome"));
    funcionario.setCpf(rs.getString("cpf"));
    funcionario.setDataNacimento(rs.getDate("data_nascimento").toLocalDate());
    funcionario.setSalarioBruto(rs.getDouble("salario_bruto"));

    return funcionario;
  }

}
