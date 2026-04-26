package sistema.app.menu;

import sistema.app.service.ConexaoService;
import sistema.app.ui.InputHelper;
import sistema.app.ui.Terminal;
import sistema.app.util.CustomLogger;
import sistema.repository.connection.DatabaseConfig;

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
      System.out.print("Opção: ");

      opcao = InputHelper.lerInt();

      switch (opcao) {

        case 1 -> realizarConexao();
        case 2 -> menuCsv();
        case 3 -> abrirMenuEntidade("FUNCIONARIO");
        case 4 -> abrirMenuEntidade("DEPENDENTE");
        case 5 -> abrirMenuEntidade("FOLHA DE PAGAMENTO");
        case 0 -> CustomLogger.logFinal("Sistema encerrado!");
        default -> CustomLogger.logWarning("Opção inválida!");
      }

    } while (opcao != 0);
  }

  /* função para estabelecer a conexão com o DB */
  private void realizarConexao() {
  }

  /*
   * Função para abrir o submenu de uma entidade:
   * FUNCIONADIO / DEPENDENTE / FOLHA DE PAGAMENTO
   */
  private void abrirMenuEntidade(String entidade) {
  }

  /* Função do menu de Importação / Exportação de arquivos CSV */
  private void menuCsv() {

  }
}