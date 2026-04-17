package sistema.service;

import sistema.repository.ConexaoDB;
import sistema.repository.DependenteDAO;
import sistema.repository.FolhaPagamentoDAO;
import sistema.repository.FuncionarioDAO;

public class CsvService {

  private final LeitorCSV leitor;
  private final EscreverCSV escrever;

  public CsvService(FuncionarioDAO funDAO, DependenteDAO denDAO, FolhaPagamentoDAO folhaDAO, ConexaoDB conexao) {

    this.leitor = new LeitorCSV(funDAO, denDAO, folhaDAO);
    this.escrever = new EscreverCSV(conexao);

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

}
