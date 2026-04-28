package sistema.app.menu;

import java.sql.Connection;
import java.sql.SQLException;
import sistema.app.service.ConexaoService;
import sistema.app.ui.InputHelper;
import sistema.app.ui.Terminal;
import static sistema.app.util.CustomLogger.logConectionError;
import static sistema.app.util.CustomLogger.logConectionSucess;
import static sistema.app.util.CustomLogger.logError;
import static sistema.app.util.CustomLogger.logFinal;
import static sistema.app.util.CustomLogger.logWarning;
import sistema.repository.connection.DatabaseConfig;
import sistema.repository.dao.DependenteDAO;
import sistema.repository.dao.FolhaPagamentoDAO;
import sistema.repository.dao.FuncionarioDAO;
import sistema.service.CsvService;

/* Menu principal do sistema. */
public class MenuPrincipal {
  private DatabaseConfig conexao;
  private final ConexaoService conexaoService = new ConexaoService();

  public void executar() {
    exibirMenu();
  }

  public void exibirMenu() {
    int opcao;
    do {

      Terminal.titulo("Menu Principal");
      if (conexao == null) {
        System.out.println("1 - Conectar ao Banco de Dados");
      } else {
        System.out.println("1 - [Conectado] Reconectar ao Banco de Dados");
      }

      System.out.println("2 - Importar / Exportar arquivo .CSV");

      System.out.println("3 - Gerenciar Funcionário");
      System.out.println("4 - Gerenciar Dependente");
      System.out.println("5 - Folha de Pagamento");
      System.out.println("0 - Sair");

      opcao = InputHelper.lerInt("Opção: ");

      switch (opcao) {

        case 1 -> realizarConexao();
        case 2 -> menuCsv();
        case 3 -> abrirMenuEntidade("FUNCIONARIO");
        case 4 -> abrirMenuEntidade("DEPENDENTE");
        case 5 -> abrirMenuEntidade("FOLHA DE PAGAMENTO");
        case 0 -> logFinal("Sistema encerrado!");
        default -> logWarning("Opção inválida!");
      }

    } while (opcao != 0);
  }

  /* função para estabelecer a conexão com o DB */
  private void realizarConexao() {
    boolean conectado = false;

    while (!conectado) {
      try {
        DatabaseConfig tentativa = conexaoService.solicitarDadosConexao();

        // Testa se a conexão realmente funciona antes de aceitar
        try (Connection con = tentativa.conectarDB()) {
          logConectionSucess("Conexão estabelecida com sucesso!");
          this.conexao = tentativa;
          conectado = true;
        }

      } catch (SQLException erro) {
        Terminal.titulo("Erro de Conexão");
        logConectionError("Falha: " + erro.getMessage());

        if (!InputHelper.confirmar("Deseja tentar novamente? ")) {
          conectado = true; // encerra o loop sem conexão
        }

      } catch (Exception e) {
        logError("Erro inesperado: " + e.getMessage());
        conectado = true;
      }
    }
  }

  /*
   * Função para abrir o submenu de uma entidade:
   * FUNCIONADIO / DEPENDENTE / FOLHA DE PAGAMENTO
   */
  private void abrirMenuEntidade(String entidade) {
    if (conexao == null) {
      Terminal.titulo("Erro");
      logError("Conexão com o Banco de Dados não estabelecida!");
      logWarning("Escolha a opção 1 para conectar antes de continuar.");
      return;
    } else {
      MenuEntidades menu = new MenuEntidades(conexao);
      menu.exibir(entidade);
    }
  }

  /* Função do menu de Importação / Exportação de arquivos CSV */
  private void menuCsv() {

    if (conexao == null) {
      logConectionError("Conexão não estabelecida. Acesse a opção 1 primeiro.");
      return;
    }

    Terminal.titulo("Menu CSV");

    System.out.println("1 - Importar CSV (Funcionários + Dependentes)");
    System.out.println("2 - Exportar Folha de Pagamento");
    System.out.println("3 - Exportar Lista de Funcionários");
    System.out.println("4 - Exportar Lista de Dependentes");
    System.out.println("5 - Exportar Quantidade de Dependentes por Funcionário");

    int opcao = InputHelper.lerInt("Opção: ");
    String caminho = InputHelper.lerTexto("Informe o caminho do arquivo/pasta: ");

    FuncionarioDAO funcionarioDAO = new FuncionarioDAO(conexao);
    DependenteDAO dependenteDAO = new DependenteDAO(conexao);
    FolhaPagamentoDAO folhaDAO = new FolhaPagamentoDAO(conexao);

    CsvService csvService = new CsvService(funcionarioDAO, dependenteDAO, folhaDAO, conexao);

    switch (opcao) {
      case 1 -> csvService.importar(caminho);
      case 2 -> csvService.exportarFolha(caminho);
      case 3 -> csvService.exportarFuncionario(caminho);
      case 4 -> csvService.exportarDependente(caminho);
      case 5 -> csvService.exportarQtdDependenteFuncionario(caminho);
      default -> logWarning("Opção inválida!");
    }
  }
}