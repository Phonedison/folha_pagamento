package sistema.app.menu;

import java.time.LocalDate;
import static sistema.app.ui.InputHelper.*;
import sistema.app.ui.Terminal;
import static sistema.app.util.CustomLogger.logError;
import static sistema.app.util.CustomLogger.logSucess;
import static sistema.app.util.CustomLogger.logWarning;
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

            opcao = lerInt("Opção: ");

            switch (opcao) {
                case 1 -> cadastrar(entidade);
                case 2 -> listar(entidade);
                case 3 -> atualizar(entidade);
                case 4 -> excluir(entidade);
                case 0 -> logWarning("Voltando ao menu principal...");
                default -> logError("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);
    }

    public void cadastrar(String entidade) {
        switch (entidade) {

            case "FUNCIONARIO" -> {
                Terminal.titulo("Cadastrar Funcionário");

                FuncionarioDAO funDAO = new FuncionarioDAO(conexao); // estabelece conexao com o DB
                Funcionario f = new Funcionario(); // Criação de um objeto do tipo funcionário

                /* Leitura dos dados */
                f.setNome(lerTexto("Nome: "));
                f.setCpf(lerTexto("CPF: "));
                f.setDataNascimento(lerData("Data de Nascimento (dd/MM/yyyy): "));
                f.setSalarioBruto(lerDouble("Salário Bruto (R$): "));

                try {
                    funDAO.salvar(f);
                    logSucess("Funcionário cadastrado com sucesso!");

                } catch (Exception e) {
                    logError("Erro ao cadastrar funcionário: " + e.getMessage());
                }
            }

            case "DEPENDENTE" -> {
                Terminal.titulo("Cadastrar Dependente");

                DependenteDAO depDAO = new DependenteDAO(conexao);
                Dependente d = new Dependente();

                d.setNome(lerTexto("Nome: "));
                d.setCpf(lerTexto("CPF: "));
                d.setDataNascimento(lerData("Data de Nascimento (dd/MM/yyyy): "));
                d.setIdFuncionario(lerInt("ID do Funcionário responsável: "));
                System.out.println("Parentesco:");
                System.out.println("1 - Filho(a)");
                System.out.println("2 - Sobrinho(a)");
                System.out.println("3 - Outros");
                d.escolherParentesco(lerInt("Opção: "));

                try {
                    d.validar(); // valida a regra de negócio (idade <= 18)
                    depDAO.salvar(d);
                    logSucess("Dependente cadastrado com sucesso!");
                } catch (DependenteException e) {
                    logError("Regra de negócio violada: " + e.getMessage());
                }
            }

            case "FOLHA DE PAGAMENTO" -> {
                Terminal.titulo("Gerar Folha de Pagamento");
                FolhaPagamentoDAO folhaDAO = new FolhaPagamentoDAO(conexao);
                FuncionarioDAO funDAO = new FuncionarioDAO(conexao);

                int idFuncionario = lerInt("Digite o ID do Funcionário: ");

                // Busca o funcionário completo (com dependentes para cálculo de IR)
                Funcionario f = funDAO.buscarPorId(idFuncionario);

                if (f == null) {
                    logError("Funcionário com ID " + idFuncionario + " não encontrado.");
                    return;
                }

                FolhaPagamento folha = new FolhaPagamento();
                folha.setFuncionario(f);
                folha.setDataPagamento(LocalDate.now());
                folha.calcularINSS();
                folha.calcularIR();
                folha.calcularSalarioLiquido();

                folhaDAO.salvar(folha);
                logSucess("Folha de pagamento gerada para: " + f.getNome());
            }

            default -> logWarning("Entidade inválida para cadastro!");
        }
    }

    public void listar(String entidade) {

        switch (entidade) {

            case "FUNCIONARIO" -> {
                Terminal.titulo("Relatório de Funcionário");

                System.out.println("Ordenar por:");
                System.out.println("1 - ID");
                System.out.println("2 - Nome");
                System.out.println("3 - CPF");
                System.out.println("4 - Data de Nascimento");
                System.out.println("5 - Salário");
                System.out.println("0 - Voltar ao Menu anterior");

                int opcao = lerInt("Opção: ");

                FuncionarioDAO funDAO = new FuncionarioDAO(conexao);

                funDAO.listarTodos(opcao)
                        .forEach(f -> System.out.println("[" + f.getIdFuncionario() + "] " + f.getNome() + " | CPF : "
                                + f.getCpf() + " | Salário: R$ " + f.getSalarioBruto()));

            }

            case "DEPENDENTE" -> {
                Terminal.titulo("Relatório de Dependentes");

                System.out.println("Ordenar por:");
                System.out.println("1 - ID");
                System.out.println("2 - Nome");
                System.out.println("3 - CPF");
                System.out.println("4 - Data de Nascimento");
                System.out.println("5 - Parentesco");
                System.out.println("0 - Voltar ao Menu anterior");

                int opcao = lerInt("Opção: ");

                DependenteDAO depDAO = new DependenteDAO(conexao);

                depDAO.listarTodos(opcao).forEach(d -> System.out.println("[" + d.getIdDependente() + "] " + d.getNome()
                        + " | Parentesco: " + d.getParentesco() + " | Funcionário ID: " + d.getIdFuncionario()));
            }

            case "FOLHA DE PAGAMENTO" -> {
                Terminal.titulo("Relatório de Folha de Pagamento");
                System.out.println("Ordenar por:");

                System.out.println("1 - Código da folha");
                System.out.println("2 - Data do Pagamento");
                System.out.println("3 - Por valor de Desconto IR");
                System.out.println("4 - Por valor de Desconto INSS");
                System.out.println("5 - Por valor de Salário Líquido");
                System.out.println("6 - Por ID Funcionário");
                System.out.println("0 - Voltar ao Menu anterior");

                int opcao = lerInt("Opção: ");

                FolhaPagamentoDAO folhaDAO = new FolhaPagamentoDAO(conexao);
                folhaDAO.listarTodos(opcao).forEach(folha -> System.out.println("Cód. " + folha.getCodigo()
                        + " | Func. ID: " + folha.getFuncionario().getIdFuncionario()
                        + " | Desconto INSS: R$ " + folha.getDescontoInss()
                        + " | Desconto IR: R$ " + folha.getDescontoIR()
                        + " | Salário Líquido: R$ " + folha.getSalarioLiquido()));
            }

            default -> logWarning("Entidade inválida para listagem!");
        }
    }

    private void atualizar(String entidade) {
        switch (entidade) {

            case "FUNCIONARIO" -> {
                Terminal.titulo("Atualizar Funcionário");
                FuncionarioDAO funDAO = new FuncionarioDAO(conexao);
                Funcionario f = new Funcionario();

                int id = lerInt("ID do Funcionário a atualizar: ");

                System.out.println("Campo a atualizar:");
                System.out.println("1 - Nome");
                System.out.println("2 - CPF");
                System.out.println("3 - Data de Nascimento");
                System.out.println("4 - Salário Bruto");

                int opcao = lerInt("Opção: ");

                switch (opcao) {
                    case 1 -> f.setNome(lerTexto("Novo nome: "));
                    case 2 -> f.setCpf(lerTexto("Novo CPF: "));
                    case 3 -> f.setDataNascimento(lerData("Nova data de nascimento (dd/MM/yyyy): "));
                    case 4 -> f.setSalarioBruto(lerDouble("Novo salário (R$): "));
                    default -> {
                        logWarning("Opção inválida!");
                        return;
                    }
                }

                try {
                    funDAO.atualizar(f, id, opcao);
                    logSucess("Funcionário ID " + id + " atualizado com sucesso!");
                } catch (CpfDuplicadoException e) {
                    logError("CPF já cadastrado: " + e.getMessage());
                }
            }

            case "DEPENDENTE" -> {
                Terminal.titulo("Atualizar Dependente");
                DependenteDAO depDAO = new DependenteDAO(conexao);
                Dependente d = new Dependente();

                int id = lerInt("ID do Dependente a atualizar: ");

                System.out.println("Campo a atualizar:");
                System.out.println("1 - Nome");
                System.out.println("2 - CPF");
                System.out.println("3 - Data de Nascimento");
                System.out.println("4 - Parentesco");
                System.out.println("5 - ID Funcionário");

                int opcao = lerInt("Opção: ");

                switch (opcao) {
                    case 1 -> d.setNome(lerTexto("Novo nome: "));
                    case 2 -> d.setCpf(lerTexto("Novo CPF: "));
                    case 3 -> d.setDataNascimento(lerData("Nova data de nascimento (dd/MM/yyyy): "));

                    case 4 -> {
                        System.out.println("Defina o Parentesco:");
                        System.out.println("1 - Filho(a)");
                        System.out.println("2 - Sobrinho(a)");
                        System.out.println("3 - Outros");
                        d.escolherParentesco(lerInt("Opção: "));
                    }
                    case 5 -> d.setIdFuncionario(lerInt("Novo ID Funcionário: "));
                    default -> {
                        logWarning("Opção inválida!");
                        return;
                    }
                }

                try {
                    depDAO.atualizar(d, id, opcao);
                    logSucess("Dependente ID " + id + " atualizado com sucesso!");
                } catch (Exception e) {
                    logError("Erro ao atualizar dependente: " + e.getMessage());
                }
            }

            default -> logError("Entidade inválida para atualização!");
        }
    }

    private void excluir(String entidade) {

        int id = lerInt("ID do registro a excluir: ");
        if (!confirmar("Tem certeza? ")) {
            logWarning("Exclusão cancelada.");
            return;
        }

        switch (entidade) {

            case "FUNCIONARIO" -> {
                FuncionarioDAO funDAO = new FuncionarioDAO(conexao);
                funDAO.excluir(id);
                logSucess("Funcionário ID " + id + " excluído.");
            }

            case "DEPENDENTE" -> {
                DependenteDAO depDAO = new DependenteDAO(conexao);
                depDAO.excluir(id);
                logSucess("Dependente ID " + id + " excluído.");
            }

            case "FOLHA DE PAGAMENTO" -> {
                FolhaPagamentoDAO folhaDAO = new FolhaPagamentoDAO(conexao);
                folhaDAO.excluir(id);
                logSucess("Folha de Pagamento cód. " + id + " excluída.");
            }

            default -> logWarning("Entidade inválida para exclusão!");
        }
    }
}
