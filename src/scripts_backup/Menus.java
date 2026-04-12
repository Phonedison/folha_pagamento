package scripts_backup;

import java.util.Scanner;
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
            System.out.println("Informe a Porta do Banco de Dados :");
            porta = sc.nextInt();

            System.out.println("Informe o nome do Banco de Dados :");
            nomeDB = sc.next();

            System.out.println("Informe o usuário do Banco de Dados :");
            usuario = sc.next();

            System.out.println("Informe a senha do Banco de Dados :");
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
}
