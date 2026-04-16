package sistema.service;

import sistema.repository.ConexaoDB;
import sistema.repository.DependenteDAO;
import sistema.repository.FolhaPagamentoDAO;
import sistema.repository.FuncionarioDAO;

public class CsvService {

  private final LeitorCSV leitor;
  private final EscritorCSV escritor;

  public CsvService(FuncionarioDAO funDAO, DependenteDAO denDAO, FolhaPagamentoDAO folhaDAO, ConexaoDB conexao) {

    this.leitor = new LeitorCSV(funDAO, denDAO, folhaDAO);
    this.escritor = new EscritorCSV(conexao);

  }

  public void importar(String caminho) {
    leitor.lerArquivo(caminho);
  }

  public void exportarFolha(String caminho) {
    escritor.escreverFolhaPagamentoCSV(caminho);
  }

}
