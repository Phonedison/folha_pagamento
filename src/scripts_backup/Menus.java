package scripts_backup;
import java.text.ParseException;
import java.util.Scanner;
import sistema.enums.Parentesco;
import sistema.model.Dependente;
import sistema.model.Funcionario;
import sistema.repository.ConexaoDB;

public class Menus {
    Scanner sc = new Scanner(System.in);


    public ConexaoDB menuConexao() {

        String nomeDB;
        String usuario;
        String senha;
        Integer porta;

        System.out.println("===================");
        System.out.println("    Bem Vindo!   ");
        System.out.println("===================\n");

        do {
            System.out.println("Informe a Porta :");
            porta = sc.nextInt();

            System.out.println("Informe o nome do Banco :");
            nomeDB = sc.next();

            System.out.println("Informe o usuário do DB :");
            usuario = sc.next();

            System.out.println("Informe a senha do DB :");
            senha = sc.next();

            if (nomeDB.isEmpty() && usuario.isEmpty() && senha.isEmpty() && porta == null) {
                System.out.println("Valores invalidos");

            } else {
                if (nomeDB.isEmpty()) {
                    System.out.println("nomeDB invalidos");
                }

                if (usuario.isEmpty()) {
                    System.out.println("usuario invalidos");
                }

                if (senha.isEmpty()) {
                    System.out.println("senha invalidos");
                }

                if (porta == null) {
                    System.out.println("porta invalidos");
                }
            }

        } while (nomeDB.isEmpty() && usuario.isEmpty() && senha.isEmpty() && porta == null);
        ConexaoDB conexao = new ConexaoDB(porta, nomeDB, usuario, senha);
        return conexao;
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


