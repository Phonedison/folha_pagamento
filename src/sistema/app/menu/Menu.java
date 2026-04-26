package sistema.app.menu;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Scanner;

import sistema.app.ui.PrintSystem;
import sistema.app.util.CustomLogger;
import sistema.repository.*;
import sistema.repository.connection.DatabaseConfig;
import sistema.repository.dao.DependenteDAO;
import sistema.repository.dao.FolhaPagamentoDAO;
import sistema.repository.dao.FuncionarioDAO;
import sistema.service.CsvService;

public class Menu {
  private final Scanner sc = new Scanner(System.in);
  private DatabaseConfig conexao;

  public void execute() {
    menu_opcoes();
  }

  public DatabaseConfig conexao_db() {

    String nomeDB;
    String usuario;
    String senha;
    int porta;

    PrintSystem.titulo("Conexão com o DB");

    do {

      System.out.println("Informe os dados para conexão com o Banco de Dados: ");
      System.out.print("Nº da Porta: ");
      porta = sc.nextInt();
      sc.nextLine();

      System.out.print("Nome do DB: ");
      nomeDB = sc.nextLine();

      System.out.print("Usuario / Login: ");
      usuario = sc.nextLine();

      System.out.print("Senha : ");
      senha = sc.nextLine();

      if (nomeDB.isBlank() && usuario.isBlank() && senha.isBlank() && porta == 0) {
        System.out.println("Valores inválidos!");

      } else {
        if (nomeDB.isBlank()) {
          System.out.println("Nome inválido!");
        }

        if (usuario.isBlank()) {
          System.out.println("Usuário inválido!");
        }

        if (senha.isBlank()) {
          System.out.println("Senha inválida!");
        }

        if (porta == 0) {
          System.out.println("Porta inválida!");
        }
      }

    } while (nomeDB.isBlank() || usuario.isBlank() || senha.isBlank() || porta == 0);

    DatabaseConfig conexao = new DatabaseConfig(porta, nomeDB, usuario, senha);

    return conexao;
  }

  public void menu_opcoes() {
    int opcao;

    do {
      PrintSystem.titulo("Menu Principal");
      System.out
          .println((conexao == null) ? "1 - Conectar ao Banco de Dados" : "Conexão Com banco de dados estabelecida!");
      System.out.println("2 - Importa / Exportar arquivo .CSV");
      System.out.println("3 - Gerenciar Funcionário");
      System.out.println("4 - Gerenciar Dependente");
      System.out.println("5 - Folha de Pagamento");
      System.out.println("0 - Sair");
      System.out.print("Digite a opção desejada: ");
      opcao = lerOpcao();

      switch (opcao) {
        case 1 -> {
          boolean conectado = false;
          while (!conectado) {
            try {
              conexao = conexao_db();

              try (Connection con = conexao.conectarDB()) {
                CustomLogger.logConectionSucess("Conexão realizada com sucesso!");
                conectado = true;
              }

            } catch (SQLException error) {
              PrintSystem.titulo("ERRO DE CONEXÃO");
              CustomLogger.logConectionError("Falha: " + error.getMessage());

              System.out.print("Deseja tentar novamente? (S/N): ");
              String resposta = sc.nextLine().toUpperCase();
              if (resposta.equals("N")) {
                conectado = true;
                conexao = null;
              }
            } catch (Exception e) {
              CustomLogger.logError("Erro inesperado: " + e.getMessage());
              break;
            }
          }
        }
        case 2 -> menu_csv();
        case 3 -> menu_modelDAO("FUNCIONARIO"); // acessa o menu de funcionário, passando o nome da entidade como
                                                // parâmetro para
                                                // identificar qual menu acessar
        case 4 -> menu_modelDAO("DEPENDENTE");// acessa o menu de dependente, passando o nome da entidade como parâmetro
                                              // para
                                              // identificar qual menu acessar
        case 5 -> menu_modelDAO("FOLHA DE PAGAMENTO");
        case 0 -> CustomLogger.logFinal("SERVIÇO FINALIZADO!");

        default -> System.out.println("Número inválido!");
      }
    } while (opcao != 0);
  }

  // método para acessar os menus de cada entidade
  private void menu_modelDAO(String entidade) {

    if (conexao == null) { // verifica se foi feito a conexão com o banco de dados
      titulo("E R R O");
      CustomLogger.logError("Conexão com o Banco de Dados não estabelecida!");
      CustomLogger.logWarning("Você precisa se conetar ao banco (Opção 1) para acessar os recursos de " + ".");
      return;
    }

    int opcao;
    // loop para manter o menu da entidade ativo até que o usuário escolha voltar
    // para o menu principal
    do {
      PrintSystem.titulo("GESTÃO DE " + entidade.toUpperCase());
      System.out.println("1 - Cadastrar ");
      System.out.println("2 - Listar ");
      System.out.println("3 - Atualizar ");
      System.out.println("4 - Excluir ");
      System.out.println("0 - Voltar ao Menu Principal");
      System.out.print("Escolha: ");

      opcao = lerOpcao();

      switch (opcao) {
        case 1 -> cadastrar(entidade);
        case 2 -> listar(entidade);
        case 3 -> atualizar(entidade);
        case 4 -> excluir(entidade);
        case 0 -> CustomLogger.logWarning("Voltando...");
        default -> CustomLogger.logWarning("Número inválido!");
      }

    } while (opcao != 0);
  }

  private int lerOpcao() {

    try {
      int valor = Integer.parseInt(sc.next());
      sc.nextLine();
      return valor;

    } catch (NumberFormatException error) {
      sc.nextLine();
      CustomLogger.logWarning("Valor inválido! Digite um número inteiro.");
      return 0;
    }

  }

  // menu para leitura / exportação do arquivo CSV
  private void menu_csv() {
    PrintSystem.titulo("MENU CSV");
    System.out.println("1 - Importar CSV (Funcionario + Dependentes)");
    System.out.println("2 - Exportar Folha de Pagamento");
    System.out.println("3 - Exportar Apenas Funcionário");
    System.out.println("4 - Exportar Apenas Dependente");
    System.out.println("5 - Exportar Qtd Dependente por Funcionário");
    System.out.print("Opção: ");
    int opcao = lerOpcao();

    System.out.print("Informe o caminho do arquivo: ");
    String caminho = sc.nextLine();

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
      default -> CustomLogger.logWarning("Opção invalida!");

    }

  }

  // método para converter a data de nascimento e data de aniversário, utilizando
  // o padrão dd/MM/yyyy, para evitar erros de formatação

}
