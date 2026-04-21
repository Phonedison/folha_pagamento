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
  //Método para pegar o caminho do arquivo .CSV de funcionário e dependentes
  public void importar(String caminho) {
    leitor.lerArquivo(caminho);
  }
  //Método para exportar o arquivo .CSV da folha de pagamento
  public void exportarFolha(String caminho) {
    escrever.escreverFolhaPagamentoCSV(caminho);
  }
  //Método para exportar arquivo .CSV de funcionários
  public void exportarFuncionario(String caminho) {
    escrever.escreverFuncionarioCSV(caminho);
  }
  //Método para exportar arquivo .CSV de dependentes
  public void exportarDependente(String caminho) {
    escrever.escreverDependenteCSV(caminho);
  }
  //Método para exportar número de dependentes por funcionário
  public void exportarQtdDependenteFuncionario(String caminho) {
    escrever.escreverQtdDependentePorFuncionario(caminho);
  }

}
