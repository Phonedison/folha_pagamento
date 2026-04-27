package sistema.service.io;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import sistema.model.Funcionario;
import sistema.repository.dao.DependenteDAO;
import sistema.repository.dao.FolhaPagamentoDAO;
import sistema.repository.dao.FuncionarioDAO;

public class CsvReader {

  private static final DateTimeFormatter frtmData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  private final FuncionarioDAO funcionarioDAO;
  private final DependenteDAO dependenteDAO;
  private final FolhaPagamentoDAO folhaPagamentoDAO;

  public CsvReader(FuncionarioDAO funcionarioDAO, DependenteDAO dependenteDAO, FolhaPagamentoDAO folhaPagamentoDAO) {
    this.funcionarioDAO = funcionarioDAO;
    this.dependenteDAO = dependenteDAO;
    this.folhaPagamentoDAO = folhaPagamentoDAO;
  }

  public List<Funcionario> lerArquivo(String caminho) {

    List<Funcionario> listaFuncionarios = new ArrayList<>();

    try (Buffer) {

    } catch (Exception e) {
    }

  }
}