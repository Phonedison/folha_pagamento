package sistema.app.menu;

import java.time.LocalDate;
import java.util.Scanner;
import sistema.model.Dependente;
import sistema.model.FolhaPagamento;
import sistema.model.Funcionario;
import sistema.repository.ConexaoDB;
import sistema.repository.DependenteDAO;
import sistema.repository.FolhaPagamentoDAO;
import sistema.repository.FuncionarioDAO;

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
        }
        // case 2 -> Importar / Exportar arquivo .CSV;
        case 3 -> menu_modelDAO("FUNCIONARIO");
        case 4 -> menu_modelDAO("DEPENDENTE");
        case 5 -> menu_modelDAO("FOLHA DE PAGAMENTO");
        case 0 -> System.out.println("Finalizando atendimento...");
        default -> System.out.println("Número inválido!");
      }
    } while (opcao != 0);
  }

  private void menu_modelDAO(String entidade) {

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
      System.out.print("Escolha: ");

      opcao = lerOpcao();

      switch (opcao) {
        case 1 -> cadastrar(entidade);
        case 2 -> listar(entidade);
        // case 3 -> atualizar(entidade);
        case 4 -> excluir(entidade);
        case 0 -> System.out.println("Voltando...");
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

  private void executarDAO(String entidade) {
    switch (entidade) {
      case "FUNCIONARIO" -> {
        FuncionarioDAO funDAO = new FuncionarioDAO(conexao);
        funDAO.selecionarFuncionario(new Funcionario(), 0, "");
      }
      case "DEPENDENTE" -> {
        DependenteDAO depDAO = new DependenteDAO(conexao);
        depDAO.selecionarDependente(new Dependente(), 0, "");
      }
      case "FOLHA DE PAGAMENTO" -> {
        FolhaPagamentoDAO folhaDAO = new FolhaPagamentoDAO(conexao);
        folhaDAO.selecionarFolha(new FolhaPagamento(), 0, "");
      }
      default -> System.out.println("Entidade inválida!");
    }
  }

  private void cadastrar(String entidade) {
    switch (entidade) {

      case "FUNCIONARIO" -> {

        FuncionarioDAO funDAO = new FuncionarioDAO(conexao);
        Funcionario f = new Funcionario();

        titulo("Cadastrar Funcionário");

        System.out.print("Nome: ");
        f.setNome(sc.nextLine());

        System.out.print("CPF: ");
        f.setCpf(sc.nextLine());

        System.out.print("Data de Nascimento: (AAAA-MM-DD) ");
        f.setDataNacimento(LocalDate.parse(sc.nextLine()));

        System.out.print("Salário Bruto: ");
        f.setSalarioBruto(Double.parseDouble(sc.nextLine()));

        try {
          funDAO.salvarFuncionario(f);
          System.out.println("Funcionário cadastrado com sucesso!");

        } catch (Exception e) {
          System.out.println("Erro ao cadastrar funcionário: " + e.getMessage());
        }
      }
      case "DEPENDENTE" -> {

        DependenteDAO depDAO = new DependenteDAO(conexao);
        Dependente d = new Dependente();

        titulo("Cadastrar Dependente");

        System.out.print("Nome: ");
        d.setNome(sc.nextLine());

        System.out.print("CPF: ");
        d.setCpf(sc.nextLine());

        System.out.print("Data de Nascimento: ");
        d.setDataNacimento(LocalDate.parse(sc.nextLine()));

        System.out.println("Escolha o parentesco: ");
        System.out.println("1 - Filho(a)");
        System.err.println("2 - Sobrinho(a)");
        System.out.println("3 - Outros");
        System.out.print("Opção: ");
        int opcaoParentesco = lerOpcao();

        d.escolherParentesco(opcaoParentesco);

        try {
          depDAO.salvarDependente(d);
          System.out.println("Dependente cadastrado com sucesso!");

        } catch (Exception error) {
          System.out.println("Erro ao cadastrar dependente: " + error.getMessage());
        }

      }
      default -> System.out.println("Entidade inválida para cadastro!");
    }
  }

  private void listar(String entidade) {
    switch (entidade) {
      case "FUNCIONARIO" -> {

        FuncionarioDAO funDAO = new FuncionarioDAO(conexao);
        funDAO.selecionarFuncionario(new Funcionario(), 0, "");

      }
      case "DEPENDENTE" -> {
        DependenteDAO depDAO = new DependenteDAO(conexao);
        depDAO.selecionarDependente(new Dependente(), 0, "");
      }
      case "FOLHA DE PAGAMENTO" -> {
        FolhaPagamentoDAO folhaDAO = new FolhaPagamentoDAO(conexao);
        folhaDAO.selecionarFolha(new FolhaPagamento(), 0, "");
      }
      default -> System.out.println("Entidade inválida para listagem!");
    }
  }

  private void excluir(String entidade) {

    System.out.println("Digite o ID para excluir:");
    int id = lerOpcao();

    switch (entidade) {

      case "FUNCIONARIO" -> {
        FuncionarioDAO funDAO = new FuncionarioDAO(conexao);
        funDAO.excluirFuncionario(id);
      }

      case "DEPENDENTE" -> {
        DependenteDAO depDAO = new DependenteDAO(conexao);
        depDAO.excluirDependente(id);
      }
    }
  }
}
