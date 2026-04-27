package sistema.repository.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

import sistema.app.util.CustomLogger;
import sistema.exception.CpfDuplicadoException;
import sistema.model.Funcionario;
import sistema.repository.connection.DatabaseConfig;

public class FuncionarioDAO extends BaseDAO implements GenericDAO<Funcionario> {

  public FuncionarioDAO(DatabaseConfig db) {
    super(db);
  }

  /* Método para adicionar um funcionário */
  @Override
  public void salvar(Funcionario funcionario) {

    String comandoSQL = "INSERT INTO funcionario (nome, cpf, data_nascimento, salario_bruto) VALUES (?, ?, ?, ?);";

    executeUpdate(
        comandoSQL,
        funcionario.getNome(),
        funcionario.getCpf(),
        funcionario.getDataNacimento(),
        funcionario.getSalarioBruto());

    CustomLogger.logSucess("Funcionário '" + funcionario.getNome() + "' salvo com sucesso!");
  }

  /* Método para buscar todos os funcionários */
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
        return new ArrayList<>();
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
      CustomLogger.logFuncionarioError("Erro ao listar Funcionários" + error.getMessage());
      throw new RuntimeException(error.getMessage(), error);
    }
    return lista;
  }

  /* Método de exclusão com base no ID e passando um parametro a ser atualizado */
  @Override
  public void atualizar(Funcionario funcionario, int id, int opcao) throws CpfDuplicadoException {
    StringBuilder sql = new StringBuilder("UPDATE funcionario SET ");
    List<Object> params = new ArrayList<>();

    switch (opcao) {
      case 0 -> {
        sql.append("nome = ?, cpf = ?, data_nascimento = ?, salario_bruto = ?");
        params.add(funcionario.getNome());
        params.add(funcionario.getCpf());
        params.add(funcionario.getDataNascimento());
        params.add(funcionario.getSalarioBruto());
      }
      case 1 -> {
        sql.append("nome = ?");
        params.add(funcionario.getNome());
      }
      case 2 -> {
        sql.append("cpf = ?");
        params.add(funcionario.getCpf());
      }
      case 3 -> {
        sql.append("data_nascimento = ?");
        params.add(funcionario.getDataNascimento());
      }
      case 4 -> {
        sql.append("salario_bruto = ?");
        params.add(funcionario.getSalarioBruto());
      }
      default -> {
        CustomLogger.logWarning("Opção de atualização inválida!");
        return;
      }
    }

    sql.append(" WHERE id_funcionario = ?");
    params.add(id);

    try {
      executeUpdate(sql.toString(), params.toArray());
      CustomLogger.logSucess("Funcionário ID " + id + " atualizado!");
    } catch (RuntimeException e) {
      // Verifica se o erro é de CPF duplicado (violação de constraint UNIQUE)
      if (e.getMessage() != null && e.getMessage().contains("cpf")) {
        throw new CpfDuplicadoException("CPF já cadastrado no sistema.", e);
      }
      throw e;
    }
  }

  /* Método de exclusão por ID */
  @Override
  public void excluir(int id) {
    String comandoSQL = "DELETE FROM funcionario WHERE id_funcionario = ?";
    executeUpdate(comandoSQL, id);

    CustomLogger.logSucess("Funcionário de ID " + id + "excluído com sucesso!");
  }

  /* Método para mapear e evitar repetição */
  private Funcionario mapearFuncionario(ResultSet rs) throws SQLException {

    Funcionario funcionario = new Funcionario();

    funcionario.setIdFuncionario(rs.getInt("id_funcionario"));
    funcionario.setNome(rs.getString("nome"));
    funcionario.setCpf(rs.getString("cpf"));
    funcionario.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
    funcionario.setSalarioBruto(rs.getDouble("salario_bruto"));

    return funcionario;
  }

  /* Método para buscar funcionário por CPF */
  public Funcionario buscarPorCpf(String cpf) {

    String comandoSQL = "SELECT * FROM funcionario WHERE cpf = ?";

    try (
        Connection conexao = db.conectarDB();
        PreparedStatement stmt = conexao.prepareStatement((comandoSQL));) {
      stmt.setString(1, cpf);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next())
          return mapearFuncionario(rs);
      }

    } catch (Exception error) {
      CustomLogger.logError("Erro ao buscar funcionário por CPF: " + error.getMessage());
    }
    return null;
  }

  /* Método para buscar funcionário por ID */
  public Funcionario buscarPorId(int id) {
    String comandoSQL = "SELECT * FROM funcionario WHERE id_funcionario = ?";
    try (
        Connection conexao = db.conectarDB();
        PreparedStatement stmt = conexao.prepareStatement(comandoSQL);) {

      stmt.setInt(1, id);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next())
          return mapearFuncionario(rs);
      }
    } catch (Exception error) {
      CustomLogger.logError("Erro ao buscar funcionário por ID: " + error.getMessage());
    }
    return null;
  }
}
