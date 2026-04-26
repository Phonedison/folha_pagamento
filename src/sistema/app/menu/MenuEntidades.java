package sistema.app.menu;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import sistema.model.Funcionario;
import sistema.model.Dependente;
import sistema.model.FolhaPagamento;
import sistema.repository.connection.DatabaseConfig;
import sistema.repository.dao.DependenteDAO;
import sistema.repository.dao.FolhaPagamentoDAO;
import sistema.repository.dao.FuncionarioDAO;
import sistema.app.ui.PrintSystem;
// Imports de exceções e utilitários
import sistema.exception.CpfDuplicadoException;
import sistema.exception.DependenteException;

public class MenuEntidades {
    private final Scanner sc = new Scanner(System.in);
    // No topo da classe MenuEntidades:
    private final DatabaseConfig conexao;

    public MenuEntidades(DatabaseConfig conexao) {
        this.conexao = conexao;
    }

    // método para cadastrar
    public void cadastrar(String entidade) {
        switch (entidade) {

            case "FUNCIONARIO" -> {

                // criando o objeto do tipo FuncionarioDAO para acessar os métodos de CRUD do
                // funcionário
                FuncionarioDAO funDAO = new FuncionarioDAO(conexao);
                // criando o objeto do tipo Funcionario para acessar os atributos e métodos da
                // classe Funcionario, e posteriormente passar como parâmetro para os métodos de
                // CRUD
                Funcionario f = new Funcionario();

                // Inserção de valores referente a funcionário
                PintSystem.titulo("Cadastrar Funcionário");

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
                    CustomLogger.logError("Erro ao cadastrar funcionário: " + e.getMessage());
                }
            }
            case "DEPENDENTE" -> {

                DependenteDAO depDAO = new DependenteDAO(conexao);
                Dependente d = new Dependente();

                PrintSystem.titulo("Cadastrar Dependente");

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

                try {
                    d.validarDependente();
                    depDAO.atualizarDependente(d, opcaoParentesco, idFuncionario);
                    depDAO.salvarDependente(d);
                    CustomLogger.logSucess("Dependente cadastrado com sucesso!");

                    // caso ocorra um erro durante a validação ou inserção do dependente, será
                    // capturada a exceção e exibida uma mensagem de erro informando o motivo
                } catch (DependenteException error) {
                    CustomLogger.logError("Erro ao cadastrar dependente: " + error.getMessage());
                }

            }

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

                CustomLogger.logSucess("Folha de pagamento do(a) Funcionário(a) Registrado!");
            }

            default -> System.out.println("Entidade inválida para cadastro!");
        }
    }

    public void listar(String entidade) {
        switch (entidade) {
            case "FUNCIONARIO" -> {
                System.out.println("Escolha :");
                System.out.println("1 - Funcionarios");
                System.out.println("2 - Lista de Quantidade de Dependente por Funcionários");

                int opcao = lerOpcao();
                FuncionarioDAO funDAO = new FuncionarioDAO(conexao);

                switch (opcao) {
                    case 1 -> {
                        PrintSystem.titulo("RELATORIO DE " + entidade);
                        // utilizando o método selecionarFuncionario da classe FuncionarioDAO para
                        // listar os funcionários cadastrados
                        funDAO.selecionarFuncionario(new Funcionario(), 0, "");
                        // select padrão para listar todos os funcionários, sem filtro
                    }
                    case 2 -> {
                        PrintSystem.titulo("RELATORIO DE QTD " + entidade);
                        funDAO.selececionarQtdDependentePorFUncionario();
                    }
                    default -> CustomLogger.logWarning("Opção Invalida!");
                }

            }
            case "DEPENDENTE" -> {
                PrintSystem.titulo("RELATORIO - " + entidade);
                DependenteDAO depDAO = new DependenteDAO(conexao);
                depDAO.selecionarDependente(new Dependente(), 0, "");
            }
            case "FOLHA DE PAGAMENTO" -> {
                PrintSystem.titulo("RELATORIO - " + entidade);
                FolhaPagamentoDAO folhaDAO = new FolhaPagamentoDAO(conexao);
                folhaDAO.selecionarFolha(new FolhaPagamento(), 0, "");
            }
            default -> CustomLogger.logWarning("Entidade inválida para listagem!");
        }
    }

    public void excluir(String entidade) {

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
                    CustomLogger.logWarning("Exclusão cancelada.");
                }
            }

            case "DEPENDENTE" -> {
                DependenteDAO depDAO = new DependenteDAO(conexao);

                System.out.println("Tem certeza que deseja excluir o dependente de ID '" + id + "'? (S/N)");
                String confirmacao = sc.nextLine().toUpperCase();
                if (confirmacao.equals("S")) {
                    depDAO.excluirDependente(id);

                } else {
                    CustomLogger.logWarning("Exclusão cancelada.");
                }
            }
        }
    }

    public void atualizar(String entidade) {

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

                    default -> CustomLogger.logWarning("Opção inválida!");
                }

                try {
                    System.out.println(
                            "Funcionário '" + f.getId_funcionario() + " " + f.getNome() + "' atualizado com sucesso!");
                    funDAO.atualizarFuncionario(f, opcao, id);

                } catch (CpfDuplicadoException error) {
                    CustomLogger.logError("Erro ao atualizar funcionário: " + error.getMessage());
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
                    CustomLogger
                            .logSucess("Dependente '" + d.getId_dependente() + " " + d.getNome()
                                    + "' atualizado com sucesso!");

                } catch (Exception error) {
                    CustomLogger.logWarning("Erro ao atualizar dependente: " + error.getMessage());
                }

            }
            default -> CustomLogger.logError("Entidade inválida para atualização!");
        }
    }

    private LocalDate converterData(String valor) {
        DateTimeFormatter formatar = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(valor, formatar);
    }
}
