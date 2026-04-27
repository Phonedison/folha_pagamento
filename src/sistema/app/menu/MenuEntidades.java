package sistema.app.menu;

import java.time.LocalDate;
import sistema.app.ui.InputHelper;
import sistema.app.ui.Terminal;
import sistema.app.util.CustomLogger;
import sistema.exception.CpfDuplicadoException;
import sistema.exception.DependenteException;
import sistema.model.Dependente;
import sistema.model.FolhaPagamento;
import sistema.model.Funcionario;
import sistema.repository.connection.DatabaseConfig;
import sistema.repository.dao.DependenteDAO;
import sistema.repository.dao.FolhaPagamentoDAO;
import sistema.repository.dao.FuncionarioDAO;

public class MenuEntidades {
    private final DatabaseConfig conexao;

    public MenuEntidades(DatabaseConfig conexao) {
        this.conexao = conexao;
    }

    public void exibir(String entidade) {
        int opcao;

        do {
            Terminal.titulo("Gestão de " + entidade);
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar");
            System.out.println("3 - Atualizar");
            System.out.println("4 - Excluir");
            System.out.println("0 - Voltar ao Menu Principal");

            System.out.print("Opção :");
            opcao = InputHelper.lerInt();

            switch (opcao) {
                case 1 -> cadastrar(entidade);
                case 2 -> listar(entidade);
                case 3 -> atualizar(entidade);
                case 4 -> excluir(entidade);
                case 0 -> CustomLogger.logWarning("Voltando ao menu principal...");
                default -> CustomLogger.logError("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);
    }

    public void cadastrar(String entidade) {
        switch (entidade) {

            case "FUNCIONARIO" -> {
                Terminal.titulo("Cadastrar Funcionario");

                FuncionarioDAO funDAO = new FuncionarioDAO(conexao); // estabelece conexao com o DB
                Funcionario f = new Funcionario(); // Criação de um objeto do tipo funcionario

                /* Leitura dos dados */
                System.out.print("Nome: ");
                f.setNome(InputHelper.lerTexto());
                System.out.print("CPF: ");
                f.setCpf(InputHelper.lerTexto());
                System.out.print("Data de Nascimento (dd/MM/yyyy): ");
                f.setDataNascimento(InputHelper.lerData());
                System.out.print("Salário Bruto: R$ ");
                f.setSalarioBruto(InputHelper.lerDouble());

                try {
                    funDAO.salvar(f);
                    CustomLogger.logSucess("Funcionário cadastrado com sucesso!");

                } catch (Exception e) {
                    CustomLogger.logError("Erro ao cadastrar funcionário: " + e.getMessage());
                }
            }

            case "DEPENDENTE" -> {
                Terminal.titulo("Cadastrar Dependente");

                DependenteDAO depDAO = new DependenteDAO(conexao);
                Dependente d = new Dependente();

                System.out.print("Nome: ");
                d.setNome(InputHelper.lerTexto());
                System.out.print("CPF: ");
                d.setCpf(InputHelper.lerTexto());
                System.out.print("Data de Nascimento (dd/MM/yyyy): ");
                d.setDataNascimento(InputHelper.lerData());
                System.out.print("ID do Funcionário responsável: ");
                d.setIdFuncionario(InputHelper.lerInt());
                System.out.println("Parentesco:");
                System.out.println("1 - Filho(a)  |  2 - Sobrinho(a)  |  3 - Outros");
                System.out.print("Opção: ");
                d.escolherParentesco(InputHelper.lerInt());

                try {
                    d.validar(); // valida a regra de negócio (idade <= 18)
                    depDAO.salvar(d);
                    CustomLogger.logSucess("Dependente cadastrado com sucesso!");
                } catch (DependenteException e) {
                    CustomLogger.logError("Regra de negócio violada: " + e.getMessage());
                }
            }

            case "FOLHA DE PAGAMENTO" -> {
                Terminal.titulo("Gerar Folha de Pagamento");
                FolhaPagamentoDAO folhaDAO = new FolhaPagamentoDAO(conexao);
                FuncionarioDAO funDAO = new FuncionarioDAO(conexao);

                System.out.print("Digite o ID do Funcionário: ");
                int idFuncionario = InputHelper.lerInt();

                // Busca o funcionário completo (com dependentes para cálculo de IR)
                Funcionario f = funDAO.buscarPorId(idFuncionario);

                if (f == null) {
                    CustomLogger.logError("Funcionário com ID " + idFuncionario + " não encontrado.");
                    return;
                }

                FolhaPagamento folha = new FolhaPagamento();
                folha.setFuncionario(f);
                folha.setDataPagamento(LocalDate.now());
                folha.calcularINSS();
                folha.calcularIR();
                folha.calcularSalarioLiquido();

                folhaDAO.salvar(folha);
                CustomLogger.logSucess("Folha de pagamento gerada para: " + f.getNome());
            }

            default -> CustomLogger.logWarning("Entidade inválida para cadastro!");
        }
    }

    public void listar(String entidade) {

        switch (entidade) {

            case "FUNCIONARIO" -> {
                Terminal.titulo("Relatório de Funcionário");

                System.out.println("Ordernar por:");
                System.out.println("1 - ID");
                System.out.println("2 - Nome");
                System.out.println("3 - CPF");
                System.out.println("4 - Data Nascimento");
                System.out.println("5 - Salário");
                System.out.println("0 - Voltar ao Menu anterior");

                System.out.print("Opção: ");
                int opcao = InputHelper.lerInt();

                FuncionarioDAO funDAO = new FuncionarioDAO(conexao);

                funDAO.listarTodos(opcao)
                        .forEach(f -> System.out.println("[" + f.getIdFuncionario() + "] " + f.getNome() + " | CPF : "
                                + f.getCpf() + " | Salário: R$ " + f.getSalarioBruto()));

            }

            case "DEPENDENTE" -> {
                Terminal.titulo("Relatório de Depenendetes");

                System.out.println("Ordernar por:");
                System.out.println("1 - ID");
                System.out.println("2 - Nome");
                System.out.println("3 - CPF");
                System.out.println("4 - Data Nascimento");
                System.out.println("5 - Parentesco");
                System.out.println("0 - Voltar ao Menu anterior");

                System.out.print("Opção: ");
                int opcao = InputHelper.lerInt();

                DependenteDAO depDAO = new DependenteDAO(conexao);

                depDAO.listarTodos(opcao).forEach(d -> System.out.println("[" + d.getIdDependente() + "] " + d.getNome()
                        + " | Parentesco: " + d.getParentesco() + " | Funcionário ID: " + d.getIdFuncionario()));
            }

            case "FOLHA DE PAGAMENTO" -> {
                Terminal.titulo("Relatório de Folha de Pagamento");
                System.out.println("Ordenar por:");

                System.out.println("Ordernar por:");
                System.out.println("1 - Código da folha");
                System.out.println("2 - Data do Pagamento");
                System.out.println("3 - Por valor de Desconto IR");
                System.out.println("4 - Por valor de Desconto INSS");
                System.out.println("5 - Por valor de Salário Líquido");
                System.out.println("6 - Por ID Funcionário");
                System.out.println("0 - Voltar ao Menu anterior");

                System.out.print("Opção: ");
                int opcao = InputHelper.lerInt();

                FolhaPagamentoDAO folhaDAO = new FolhaPagamentoDAO(conexao);
                folhaDAO.listarTodos(opcao).forEach(folha -> System.out.println("Cód. " + folha.getCodigo()
                        + " | Func. ID: " + folha.getFuncionario().getIdFuncionario()
                        + " | Desconto INSS: R$ " + folha.getDescontoInss()
                        + " | Desconto IR: R$ " + folha.getDescontoIR()
                        + " | Salário Líquido: R$ " + folha.getSalarioLiquido()));
            }

            default -> CustomLogger.logWarning("Entidade inválida para listagem!");
        }
    }

    private void atualizar(String entidade) {
        switch (entidade) {

            case "FUNCIONARIO" -> {
                Terminal.titulo("Atualizar Funcionário");
                FuncionarioDAO funDAO = new FuncionarioDAO(conexao);
                Funcionario f = new Funcionario();

                System.out.print("ID do Funcionário a atualizar: ");
                int id = InputHelper.lerInt();

                System.out.println("Campo a atualizar:");
                System.out.println("1 - Nome");
                System.out.println("2 - CPF");
                System.out.println("3 - Data de Nascimento");
                System.out.println("4 - Salário Bruto");

                System.out.print("Opção: ");
                int opcao = InputHelper.lerInt();

                switch (opcao) {
                    case 1 -> {
                        System.out.print("Novo nome: ");
                        f.setNome(InputHelper.lerTexto());
                    }
                    case 2 -> {
                        System.out.print("Novo CPF: ");
                        f.setCpf(InputHelper.lerTexto());
                    }
                    case 3 -> {
                        System.out.print("Nova data (dd/MM/yyyy): ");
                        f.setDataNascimento(InputHelper.lerData());
                    }
                    case 4 -> {
                        System.out.print("Novo salário: ");
                        f.setSalarioBruto(InputHelper.lerDouble());
                    }
                    default -> {
                        CustomLogger.logWarning("Opção inválida!");
                        return;
                    }
                }

                try {
                    funDAO.atualizar(f, id, opcao);
                    CustomLogger.logSucess("Funcionário ID " + id + " atualizado com sucesso!");
                } catch (CpfDuplicadoException e) {
                    CustomLogger.logError("CPF já cadastrado: " + e.getMessage());
                }
            }

            case "DEPENDENTE" -> {
                Terminal.titulo("Atualizar Dependente");
                DependenteDAO depDAO = new DependenteDAO(conexao);
                Dependente d = new Dependente();

                System.out.print("ID do Dependente a atualizar: ");
                int id = InputHelper.lerInt();

                System.out.println("Campo a atualizar:");
                System.out.println("1 - Nome");
                System.out.println("2 - CPF");
                System.out.println("3 - Data de Nascimento");
                System.out.println("4 - Parentesco");
                System.out.println("5 - ID Funcionário");

                System.out.print("Opção: ");
                int opcao = InputHelper.lerInt();

                switch (opcao) {
                    case 1 -> {
                        System.out.print("Novo nome: ");
                        d.setNome(InputHelper.lerTexto());
                    }
                    case 2 -> {
                        System.out.print("Novo CPF: ");
                        d.setCpf(InputHelper.lerTexto());
                    }
                    case 3 -> {
                        System.out.print("Nova data (dd/MM/yyyy): ");
                        d.setDataNascimento(InputHelper.lerData());
                    }
                    case 4 -> {
                        System.out.println("Defina o Parentesco:");
                        System.out.println("1 - Filho(a)");
                        System.out.println("2 - Sobrinho(a)");
                        System.out.println("3 - Outros");
                        System.out.print("Opção: ");
                        d.escolherParentesco(InputHelper.lerInt());
                    }
                    case 5 -> {
                        System.out.print("Novo ID Funcionário: ");
                        d.setIdFuncionario(InputHelper.lerInt());
                    }
                    default -> {
                        CustomLogger.logWarning("Opção inválida!");
                        return;
                    }
                }

                try {
                    depDAO.atualizar(d, id, opcao);
                    CustomLogger.logSucess("Dependente ID " + id + " atualizado com sucesso!");
                } catch (Exception e) {
                    CustomLogger.logError("Erro ao atualizar dependente: " + e.getMessage());
                }
            }

            default -> CustomLogger.logError("Entidade inválida para atualização!");
        }
    }

    private void excluir(String entidade) {
        System.out.print("ID do registro a excluir: ");
        int id = InputHelper.lerInt();

        System.out.print("Tem certeza? (S/N): ");
        if (!InputHelper.confirmar()) {
            CustomLogger.logWarning("Exclusão cancelada.");
            return;
        }

        switch (entidade) {

            case "FUNCIONARIO" -> {
                FuncionarioDAO funDAO = new FuncionarioDAO(conexao);
                funDAO.excluir(id);
                CustomLogger.logSucess("Funcionário ID " + id + " excluído.");
            }

            case "DEPENDENTE" -> {
                DependenteDAO depDAO = new DependenteDAO(conexao);
                depDAO.excluir(id);
                CustomLogger.logSucess("Dependente ID " + id + " excluído.");
            }

            case "FOLHA DE PAGAMENTO" -> {
                FolhaPagamentoDAO folhaDAO = new FolhaPagamentoDAO(conexao);
                folhaDAO.excluir(id);
                CustomLogger.logSucess("Folha de Pagamento cód. " + id + " excluída.");
            }

            default -> CustomLogger.logWarning("Entidade inválida para exclusão!");
        }
    }
}
