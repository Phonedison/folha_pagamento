package sistema.service;

import java.time.LocalDate;
import sistema.model.FolhaPagamento;
import sistema.model.Funcionario;
import sistema.repository.connection.DatabaseConfig;
import sistema.repository.dao.DependenteDAO;
import sistema.repository.dao.FolhaPagamentoDAO;
import sistema.repository.dao.FuncionarioDAO;
import sistema.service.io.CsvReader;
import sistema.service.io.CsvWriter;

public class CsvService {

  private final CsvReader leitor;
  private final CsvWriter escrever;
  private final DatabaseConfig conexao;

  public CsvService(FuncionarioDAO funDAO, DependenteDAO denDAO, FolhaPagamentoDAO folhaDAO, DatabaseConfig conexao) {

    this.leitor = new CsvReader(funDAO, denDAO, folhaDAO);
    this.escrever = new CsvWriter(conexao);
    this.conexao = conexao;

  }

  public void importar(String caminho) {
    leitor.lerArquivo(caminho);
  }

  public void exportarFolha(String caminho) {
    escrever.escreverFolhaPagamentoCSV(caminho);
  }

  public void exportarFuncionario(String caminho) {
    escrever.escreverFuncionarioCSV(caminho);
  }

  public void exportarDependente(String caminho) {
    escrever.escreverDependenteCSV(caminho);
  }

  public void exportarQtdDependenteFuncionario(String caminho) {
    escrever.escreverQtdDependentePorFuncionario(caminho);
  }

  public void registrarFolha(Funcionario funcionario) {
    FolhaPagamento folha = new FolhaPagamento();
    folha.setFuncionario(funcionario);
    folha.setDataPagamento(LocalDate.now());

    FolhaService calcularFolha = new FolhaService();
    calcularFolha.processarFolhaCompleta(folha);

    FolhaPagamentoDAO fd = new FolhaPagamentoDAO(conexao);
    fd.salvar(folha);
  }

}
