package sistema.app.menu;

import java.util.Scanner;
import sistema.repository.ConexaoDB;

public class Menus {
  private Scanner sc = new Scanner(System.in);
  private ConexaoDB conexao;

  public void execute() {
    menu_opcoes();
  }

  public ConexaoDB conexao_db() {

    String nomeDB;
    String usuario;
    String senha;
    Integer porta;

    titulo("Conexão com o DB");

    do {
      System.out.println("Informe os dados para conexão com o Banco de Dados: ");
      System.out.print("Nº da Porta: ");
      porta = sc.nextInt();

      System.out.print("Nome do DB: ");
      nomeDB = sc.next();

      System.out.print("Usuario / Login: ");
      usuario = sc.next();

      System.out.print("Senha : ");
      senha = sc.next();

      if (nomeDB.isEmpty() && usuario.isEmpty() && senha.isEmpty() && porta == null) {
        System.out.println("Valores inválidos!");

      } else {
        if (nomeDB.isEmpty()) {
          System.out.println("Nome inválido!");
        }

        if (usuario.isEmpty()) {
          System.out.println("Usuário inválido!");
        }

        if (senha.isEmpty()) {
          System.out.println("Senha inválida!");
        }

        if (porta == null) {
          System.out.println("Porta inválida!");
        }
      }

    } while (nomeDB.isEmpty() && usuario.isEmpty() && senha.isEmpty() && porta == null);

    ConexaoDB conexao = new ConexaoDB(porta, nomeDB, usuario, senha);

    return conexao;
  }

  public void menu_opcoes() {
    int opcao;
    do {
      titulo("Menu Principal");
      System.out.println("1 - Conectar ao Banco de Dados");
      System.out.println("2 - Importa / Exportar arquivo .CSV");
      System.out.println("3 - Gerenciar Funcionário");
      System.out.println("4 - Gerenciar Dependente");
      System.out.println("5 - Folha de Pagamento");
      System.out.println("0 - Sair");
      System.out.print("Digite a opção desejada: ");
      opcao = lerOpcao();

      switch (opcao) {
        case 1 -> {
          conexao = conexao_db();
          System.out.println("Conexão realizada com sucesso!");
          break;
        }
        // case 2 -> "menuConexao()"; --> método de Importação / Exportação de arquivos
        // .CSV
        // case 3 -> "menuConexao()"; --> Repasse para o menu de Gerenciamento de
        // Funcionário
        // case 4 -> "menuConexao()"; --> Repasse para o menu de Gerenciamento de
        // Funcionário
        // case 5 -> "menuConexao()"; --> Método de Folha de Pagamento
        case 0 -> {
          System.out.println("Finalizando atendimento...");
          break;
        }
        default -> System.out.println("Número inválido!");
      }
    } while (opcao != 0);
  }

  private void menu_model(String entidade) {

    if (conexao == null) { // verifica se foi feito a conexão com o banco de dados
      titulo("E R R O");
      System.out.println("Conexão com o Banco de Dados não estabelecida!");
      System.out.println("Você precisa se conetar ao banco (Opção 1) para acessar os recursos de " + entidade + ".");
      return;
    }

    int opcao;

    do {
      titulo("GESTÃO DE " + entidade.toUpperCase());
      System.out.println("1 - Cadastrar " + entidade);
      System.out.println("2 - Listar " + entidade);
      System.out.println("3 - Atualizar " + entidade);
      System.out.println("4 - Excluir " + entidade);
      System.out.println("0 - Voltar ao Menu Principal");
      System.out.print("Digite a opção desejada: ");

      opcao = lerOpcao();

      switch (opcao) {
        case 1 -> System.out.println("Opção de cadastro de " + entidade + " selecionada.");
        case 2 -> System.out.println("Opção de listagem de " + entidade + " selecionada.");
        case 3 -> System.out.println("Opção de atualização de " + entidade + " selecionada.");
        case 4 -> System.out.println("Opção de exclusão de " + entidade + " selecionada.");
        case 0 -> System.out.println("Voltando ao Menu Principal...");
        default -> System.out.println("Número inválido!");
      }

    } while (opcao != 0);
  }

  private void titulo(String titulo) {
    System.out.println("\n==========================================");
    System.out.printf("   %s%n", titulo);
    System.out.println("==========================================");
  }

  private int lerOpcao() {
    try {
      int valor = Integer.parseInt(sc.nextLine());
      return valor;
    } catch (NumberFormatException error) {
      System.out.println("Valor inválido! Digite um número inteiro.");
      return 0;
    }
  }
}
