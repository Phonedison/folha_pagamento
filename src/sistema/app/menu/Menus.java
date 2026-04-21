package sistema.app.menu;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import sistema.exception.CpfDuplicado;
import sistema.exception.DependenteException;
import sistema.model.*;
import sistema.repository.*;
import sistema.service.CsvService;

/* Parte respnsável pela interação com usuário*/
public class Menus {

  /* Serve para customizar as mensagens exibidas pro usuário*/
  CustomLogger customLogger = new CustomLogger();

  private final Scanner sc = new Scanner(System.in);
  // Objeto de conexão com o banco de dados
  private ConexaoDB conexao;

  /*Chama o método menu opções*/
  public void execute() {
    menu_opcoes();
  }

  /* Método para fazer conexão com o banco de dados com as informações providas pelo usuário*/
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
      sc.nextLine();

      System.out.print("Nome do DB: ");
      nomeDB = sc.nextLine();

      System.out.print("Usuario / Login: ");
      usuario = sc.nextLine();

      System.out.print("Senha : ");
      senha = sc.nextLine();

      /* Verificação de dados vazios*/
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

    } while (nomeDB.isEmpty() || usuario.isEmpty() || senha.isEmpty() || porta == null);

    /* Instância pra conexão com o Banco*/
    ConexaoDB conexao = new ConexaoDB(porta, nomeDB, usuario, senha);

    return conexao;
  }
  /* Método que dá opções de ações para o usuário após a conexão com o Banco de Dados*/
  public void menu_opcoes() {

    int opcao;
    do {
      titulo("Menu Principal");
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
        /* Verifica se está conectado com o banco e envia mensagem de confirmação de conexão ou erro de conexão*/
        case 1 -> {
          boolean conectado = false;
          while (!conectado) {
            try {
              conexao = conexao_db();

              try (Connection con = conexao.conectarDB()) {
                customLogger.logConectionSucess("Conexão realizada com sucesso!");
                conectado = true;
              }

            } catch (SQLException error) {
              titulo("ERRO DE CONEXÃO");
              customLogger.logConectionError("Falha: " + error.getMessage());

              System.out.print("Deseja tentar novamente? (S/N): ");
              String resposta = sc.nextLine().toUpperCase();
              if (resposta.equals("N")) {
                conectado = true;
                conexao = null;
              }
            } catch (Exception e) {
              customLogger.logError("Erro inesperado: " + e.getMessage());
              break;
            }
          }
        }
        /* Chama o método de opções do que fazer com arquivo .CSV*/
        case 2 -> menu_csv();

        case 3 -> menu_modelDAO("FUNCIONARIO"); // acessa o menu de funcionário, passando o nome da entidade como
                                                // parâmetro para
                                                // identificar qual menu acessar
        case 4 -> menu_modelDAO("DEPENDENTE");// acessa o menu de dependente, passando o nome da entidade como parâmetro
                                              // para
                                              // identificar qual menu acessar
        /* Acessa o menu de folha de pagamento*/
        case 5 -> menu_modelDAO("FOLHA DE PAGAMENTO");
        /* Finaliza a leitura de dados do usuário*/
        case 0 -> customLogger.logFinal("SERVIÇO FINALIZADO!");

        default -> System.out.println("Número inválido!");
      }
    } while (opcao != 0);
  }

  // método para acessar os menus de cada entidade
  private void menu_modelDAO(String entidade) {

    if (conexao == null) { // verifica se foi feito a conexão com o banco de dados
      titulo("E R R O");
      customLogger.logError("Conexão com o Banco de Dados não estabelecida!");
      customLogger.logWarning("Você precisa se conetar ao banco (Opção 1) para acessar os recursos de " + ".");
      return;
    }

    int opcao;
    // loop para manter o menu da entidade ativo até que o usuário escolha voltar
    // para o menu principal
    do {
      titulo("GESTÃO DE " + entidade.toUpperCase());
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
        case 0 -> customLogger.logWarning("Voltando...");
        default -> customLogger.logWarning("Número inválido!");
      }

    } while (opcao != 0);
  }

  //Método para ler opção digitada e tratar excessões

  private int lerOpcao() {

    try {
      int valor = Integer.parseInt(sc.next());
      sc.nextLine();
      return valor;

    } catch (NumberFormatException error) {
      sc.nextLine();
      customLogger.logWarning("Valor inválido! Digite um número inteiro.");
      return 0;
    }

  }
 // Método para cadastrar Funcionario ou dependente com dados fornecidos pelo usuário e para acessar a folha de pagamento
  private void cadastrar(String entidade) {
    switch (entidade) {

      //Opção de cadastrar funcionario
      case "FUNCIONARIO" -> {

        // criando o objeto do tipo FuncionarioDAO para acessar os métodos de CRUD do
        // funcionário
        FuncionarioDAO funDAO = new FuncionarioDAO(conexao);
        // criando o objeto do tipo Funcionario para acessar os atributos e métodos da
        // classe Funcionario, e posteriormente passar como parâmetro para os métodos de
        // CRUD
        Funcionario f = new Funcionario();

        // Inserção de valores referente a funcionário
        titulo("Cadastrar Funcionário");

        System.out.print("Nome: ");
        f.setNome(sc.nextLine());

        System.out.print("CPF: ");
        f.setCpf(sc.nextLine());

        System.out.print("Data de Nascimento: ");
        f.setDataNacimento(converterData(sc.nextLine()));

        System.out.print("Salário Bruto: ");
        f.setSalarioBruto(Double.parseDouble(sc.nextLine()));

        try {
          // validando os dados do funcionário utilizando o método validarFuncionario da
          // classe Funcionario, caso haja algum erro de validação, será lançado uma
          // exceção com a mensagem de erro correspondente
          funDAO.salvarFuncionario(f);
          // System.out.println("Funcionário cadastrado com sucesso!");

        } catch (Exception e) {
          // caso ocorra um erro durante a validação ou inserção do funcionário, será
          // capturada a exceção e exibida uma mensagem de erro informando o motivo do
          // erro, facilitando a correção do mesmo
          customLogger.logError("Erro ao cadastrar funcionário: " + e.getMessage());
        }
      }
      //Opção de cadastrar dependente
      case "DEPENDENTE" -> {

        DependenteDAO depDAO = new DependenteDAO(conexao);
        Dependente d = new Dependente();

        titulo("Cadastrar Dependente");

        System.out.print("Nome: ");
        d.setNome(sc.nextLine());

        System.out.print("CPF: ");
        d.setCpf(sc.nextLine());

        System.out.print("Data de Nascimento: ");
        d.setDataNacimento(converterData(sc.nextLine()));

        System.out.println("ID do(a) funcionário(a) responsável: ");
        int idFuncionario = lerOpcao();
        d.setFuncionario(idFuncionario); // Integer

        System.out.println("Escolha o parentesco: ");
        System.out.println("1 - Filho(a)");
        System.out.println("2 - Sobrinho(a)");
        System.out.println("3 - Outros");
        System.out.print("Opção: ");
        int opcaoParentesco = lerOpcao();

        d.escolherParentesco(opcaoParentesco);

        //Tratamento de excessões no cadastro de dependentes
        try {
          d.validarDependente();
          depDAO.atualizarDependente(d, opcaoParentesco, idFuncionario);
          depDAO.salvarDependente(d);
          customLogger.logSucess("Dependente cadastrado com sucesso!");

          // caso ocorra um erro durante a validação ou inserção do dependente, será
          // capturada a exceção e exibida uma mensagem de erro informando o motivo
        } catch (DependenteException error) {
          customLogger.logError("Erro ao cadastrar dependente: " + error.getMessage());
        }

      }
      //Opção para registrar folha de pagamento
      case "FOLHA DE PAGAMENTO" -> {
        FolhaPagamentoDAO folhaDAO = new FolhaPagamentoDAO(conexao);

        System.out.print("Digite o ID do(a) funcionário(a): ");
        int idFuncionario = lerOpcao();

        // FuncionarioDAO funDAO = new FuncionarioDAO(conexao);
        Funcionario f = new Funcionario();
        f.setId_funcionario(idFuncionario);

        FolhaPagamento folha = new FolhaPagamento();
        folha.setFuncionario(f);
        folha.setDataPagamento(LocalDate.now());

        folha.calcularINSS();
        folha.calcularIR();
        folha.calcularSalarioLiquido();

        // Salva os dados no DB
        folhaDAO.salvarFolha(folha);

        customLogger.logSucess("Folha de pagamento do(a) Funcionário(a) Registrado!");
      }

      default -> System.out.println("Entidade inválida para cadastro!");
    }
  }
  // Método para exibir os funcionarios e a quantidade de dependentes
  private void listar(String entidade) {
    switch (entidade) {
      case "FUNCIONARIO" -> {
        System.out.println("Escolha :");
        System.out.println("1 - Funcionarios");
        System.out.println("2 - Lista de Quantidade de Dependente por Funcionários");

        int opcao = lerOpcao();
        FuncionarioDAO funDAO = new FuncionarioDAO(conexao);

        switch (opcao) {
          case 1 -> {
            titulo("RELATORIO DE " + entidade);
            // utilizando o método selecionarFuncionario da classe FuncionarioDAO para
            // listar os funcionários cadastrados
            funDAO.selecionarFuncionario(new Funcionario(), 0, "");
            // select padrão para listar todos os funcionários, sem filtro
          }
          case 2 -> {
            titulo("RELATORIO DE QTD " + entidade);
            funDAO.selececionarQtdDependentePorFUncionario();
          }
          default -> customLogger.logWarning("Opção Invalida!");
        }

      }
      case "DEPENDENTE" -> {
        titulo("RELATORIO - " + entidade);
        DependenteDAO depDAO = new DependenteDAO(conexao);
        depDAO.selecionarDependente(new Dependente(), 0, "");
      }
      case "FOLHA DE PAGAMENTO" -> {
        titulo("RELATORIO - " + entidade);
        FolhaPagamentoDAO folhaDAO = new FolhaPagamentoDAO(conexao);
        folhaDAO.selecionarFolha(new FolhaPagamento(), 0, "");
      }
      default -> customLogger.logWarning("Entidade inválida para listagem!");
    }
  }

  //Metodo para deletar um funcionario ou um dependente
  private void excluir(String entidade) {

    System.out.println("Digite o ID para excluir:");
    int id = lerOpcao();

    switch (entidade) {

      case "FUNCIONARIO" -> {
        // criando o objeto do tipo FuncionarioDAO para acessar os métodos de CRUD do
        // funcionário
        FuncionarioDAO funDAO = new FuncionarioDAO(conexao);
        // valida
        System.out.println("Tem certeza que deseja excluir o funcionário de ID '" + id + "'? (S/N)");
        String confirmacao = sc.nextLine().toUpperCase();
        if (confirmacao.equals("S")) {
          // se sim, executa o método excluir funcionario
          funDAO.excluirFuncionario(id);
        } else {
          customLogger.logWarning("Exclusão cancelada.");
        }
      }

      case "DEPENDENTE" -> {
        DependenteDAO depDAO = new DependenteDAO(conexao);

        System.out.println("Tem certeza que deseja excluir o dependente de ID '" + id + "'? (S/N)");
        String confirmacao = sc.nextLine().toUpperCase();
        if (confirmacao.equals("S")) {
          depDAO.excluirDependente(id);

        } else {
          customLogger.logWarning("Exclusão cancelada.");
        }
      }
    }
  }

  //Método para atualizar informações específicas de Funcionario ou de dependente
  private void atualizar(String entidade) {

    switch (entidade) {

      case "FUNCIONARIO" -> {
        FuncionarioDAO funDAO = new FuncionarioDAO(conexao);
        Funcionario f = new Funcionario();

        System.out.println("Digite o ID do(a) funcionário(a) a ser atualizado: ");
        int id = lerOpcao();
        // informa os dados atuais do funcionário para facilitar a escolha do que
        // atualizar
        System.out.println("O que deseja atualizar? ");
        System.out.println("1 - Nome");
        System.out.println("2 - CPF");
        System.out.println("3 - Data de Nascimento");
        System.out.println("4 - Salário Bruto");
        System.out.print("Opção: ");
        int opcao = lerOpcao();

        // escolhe o parametro que vai ser atualizado
        switch (opcao) {
          case 1 -> {
            System.out.print("Novo nome: ");
            f.setNome(sc.nextLine());
          }

          case 2 -> {
            System.out.print("Novo CPF: ");
            f.setCpf(sc.nextLine());
          }

          case 3 -> {
            System.out.print("Nova data de nascimento: ");
            f.setDataNacimento(converterData(sc.nextLine()));
          }

          case 4 -> {
            System.out.print("Novo salário bruto: ");
            f.setSalarioBruto(Double.parseDouble(sc.nextLine()));
          }

          default -> customLogger.logWarning("Opção inválida!");
        }

        try {
          System.out.println("Funcionário '" + f.getId_funcionario() + " " + f.getNome() + "' atualizado com sucesso!");
          funDAO.atualizarFuncionario(f, opcao, id);

        } catch (CpfDuplicado error) {
          customLogger.logError("Erro ao atualizar funcionário: " + error.getMessage());
        }

      }

      case "DEPENDENTE" -> {
        DependenteDAO depDAO = new DependenteDAO(conexao);
        Dependente d = new Dependente();

        System.out.println("Digite o ID do Dependente a ser atualizado: ");
        int id = lerOpcao();

        System.out.println("O que deseja atualizar? ");
        System.out.println("1 - Nome");
        System.out.println("2 - CPF");
        System.out.println("3 - Data de Nascimento");
        System.out.println("4 - Parentesco");
        System.out.println("5 - ID Funcionário");
        System.out.print("Opção: ");
        int opcao = lerOpcao();

        switch (opcao) {
          case 1 -> {
            System.out.print("Novo nome: ");
            d.setNome(sc.nextLine());
          }

          case 2 -> {
            System.out.print("Novo CPF: ");
            d.setCpf(sc.nextLine());
          }

          case 3 -> {
            System.out.print("Nova data de nascimento: ");
            // Criar método de tratativa da data de nascimento para evitar erros de
            // formatação
            d.setDataNacimento(converterData(sc.nextLine()));
          }

          case 4 -> {
            System.out.print("Novo parentesco, Escolha: ");
            System.out.println("1 - Filho(a)");
            System.out.println("2 - Sobrinho(a)");
            System.out.println("3 - Outros");
            System.out.print("Opção: ");
            int opcaoParentesco = lerOpcao();

            d.escolherParentesco(opcaoParentesco);
          }

          case 5 -> {
            System.out.print("Novo ID do(a) funcionário(a): ");
            d.setFuncionario(Integer.valueOf(sc.nextLine()));
          }

          default -> System.out.println("Opção inválida!");
        }
        try {
          depDAO.atualizarDependente(d, opcao, id);
          customLogger
              .logSucess("Dependente '" + d.getId_dependente() + " " + d.getNome() + "' atualizado com sucesso!");

        } catch (Exception error) {
          customLogger.logWarning("Erro ao atualizar dependente: " + error.getMessage());
        }

      }
      default -> customLogger.logError("Entidade inválida para atualização!");
    }
  }

  // menu para leitura / exportação do arquivo CSV
  private void menu_csv() {
    titulo("MENU CSV");
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
      default -> customLogger.logWarning("Opção invalida!");

    }

  }

  // método para converter a data de nascimento e data de aniversário, utilizando
  // o padrão dd/MM/yyyy, para evitar erros de formatação
  public LocalDate converterData(String valor) {
    DateTimeFormatter formatar = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    return LocalDate.parse(valor, formatar);
  }

  // metodo para imprimir o título dos menus, deixando a interface mais organizada
  // e fácil de ler
  private void titulo(String titulo) {
    System.out.println("\n==========================================");
    System.out.printf("   %s%n", titulo);
    System.out.println("==========================================");
  }
}
