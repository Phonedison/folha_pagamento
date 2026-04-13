package scripts_backup;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Scanner;
import sistema.enums.Parentesco;
import sistema.model.Dependente;

import sistema.model.Funcionario;
import sistema.repository.ConexaoDB;

public class Menus {

    Scanner sc = new Scanner(System.in);

    public ConexaoDB menuConexao(){

        String nomeDB;
        String usuario;
        String senha;
        Integer porta;

        System.out.println("===================");
        System.out.println("    Bem Vindo!   ");
        System.out.println("===================\n");

        do {
            System.out.println("Informe a Porta do Banco de Dados: ");
            porta = sc.nextInt();

            System.out.println("Informe o nome do Banco de Dados: ");
            nomeDB = sc.next();

            System.out.println("Informe o usuário do Banco de Dados: ");
            usuario = sc.next();

            System.out.println("Informe a senha do Banco de Dados: ");
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

    public void menuOpcoes(){

        Integer opcao;

        System.out.println("=====================");
        System.out.println(" O que deseja fazer? ");
        System.out.println("=====================");

        System.out.println(" 1-Cadastrar Funcionário ");
        System.out.println("--------------------------");
        System.out.println(" 2-Cadastrar dependente ");
        System.out.println("--------------------------");
        System.out.println(" 3-Remover Funcionário ");
        System.out.println("--------------------------");
        System.out.println(" 4-Remover Dependente ");
        System.out.println("--------------------------");
        System.out.println("       0-Sair             ");
        opcao = sc.nextInt();

        do {
            switch (opcao) {

                case 1:
                    try {
                        criarFuncionario();
                    } catch (ParseException e){
                        throw new RuntimeException(e);
                    }
                    break;

                case 2:
                    cadastroDependentes();
                    break;

                case 3:
                    removerFuncionario();
                    break;

                case 4:
                    break;

                case 0:
                    System.out.println("Finalizando atendimento...");
                    break;

                default:
                    System.out.println("Número inválido!");
                    break;
            }

        }while(opcao != 0);
    }

    public Funcionario criarFuncionario() throws ParseException{
            String nome;
            String cpf;
            String dataNascimento;
//          Dependente dependete;
            Double salario;

            System.out.println("===== CADASTRO DE FUNCIONÁRIO =====");

            do {
                System.out.println("Informe o Nome do Funcionário: ");
                nome = sc.next();

                System.out.println("Informe o CPF do Funcionário: ");
                cpf = sc.next();

                System.out.println("Informe a Data de Nascimento do Funcionário (xx/xx/xxxx): ");
                dataNascimento = sc.nextLine();

                System.out.println("Informe o Salario Bruto do Funcionário: ");
                salario = sc.nextDouble();

//                System.out.println("Informe o nome do dependente se houver: ");
//                dependentes = sc.next();


                if (nome.isEmpty() && cpf.isEmpty() && dataNascimento.isEmpty()){
                    System.out.println("Valores inválidos!");

                } else {
                    if (nome.isEmpty()) {
                        System.out.println("Nome inválido!");
                    }

                    if (cpf.isEmpty()) {
                        System.out.println("CPF inválido!");
                    }

                    if (dataNascimento.isEmpty()) {
                        System.out.println("Data inválida!");
                    }

                    if (salario <= 0) {
                        System.out.println("Salário inválido!");
                    }
                }

            } while (nome.isEmpty() || cpf.isEmpty() || dataNascimento.isEmpty());


            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date utilDate = sdf.parse(dataNascimento);

            Date data = new Date(utilDate.getTime());

            Funcionario funcionario = new Funcionario(nome, cpf, data, salario);
            return funcionario;
        }

    public void removerFuncionario(){
        System.out.println("Digite o ID do funcionário que deseja remover: ");
        int id = sc.nextInt();

    }
    public Dependente cadastroDependentes() {

        String nomeDependente;
        String cpfDependente;
        Parentesco Parentesco;
        String dataNascDependente;
        Integer idFuncionario;

        do {
            System.out.println("====================================\n");
            System.out.println("::::::::CADASTRO DEPENDENTE::::::::\n");
            System.out.println("=====================================\n");
            System.out.println("Digite o nome do dependente : ");
            nomeDependente = sc.nextLine();
            System.out.println("\nDigite o cpf do dependente : ");
            cpfDependente = sc.nextLine();
            System.out.println("\nDigite a data de nascimento : ");
            dataNascDependente = sc.nextLine();
            System.out.println("\nDigite o id do Funcionário:");
            idFuncionario = sc.nextInt();
            System.out.println("\nEscolha grau do Dependente : ");
            System.out.println("1 - FILHOS   2 - SOBRINHO  3 - OUTROS");
            System.out.println("Escolha uma opção => ");
            int opcao = sc.nextInt();

            switch (opcao) {
                case 1:
                    Parentesco = sistema.enums.Parentesco.FILHO;
                    break;
                case 2:
                    Parentesco = sistema.enums.Parentesco.SOBRINHO;
                    break;
                case 3:
                    Parentesco = sistema.enums.Parentesco.OUTROS;
                    break;
            }
            System.out.println("Dependente cadastrado com surcess!!");

            if (nomeDependente.isEmpty() && cpfDependente.isEmpty() && dataNascDependente.isEmpty()) {
                System.out.println("Error,informações inválidas");
            } else if (nomeDependente.isEmpty() || nomeDependente == null) {
                System.out.println("Nome inválido");
            } else if (cpfDependente.isEmpty() || cpfDependente == null) {
                System.out.println("CPF inválido");
            } else if (dataNascDependente.isEmpty() || dataNascDependente == null) {
                System.out.println("Nome inválido");
            }
        }while (nomeDependente.isBlank() && cpfDependente.isEmpty() && dataNascDependente.isEmpty() || nomeDependente.isEmpty() && cpfDependente.isEmpty() && dataNascDependente.isEmpty());
        Dependente dependente = new Dependente(nomeDependente,cpfDependente,dataNascDependente,idFuncionario);
        return dependente;
    }
}


