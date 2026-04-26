package sistema.app.service;

import java.util.Scanner;

import sistema.app.ui.PrintSystem;
import sistema.repository.connection.DatabaseConfig;

public class ConexaoService {
    private final Scanner sc = new Scanner(System.in);

    public DatabaseConfig solicitarDadosConexao() {
        String nomeDB, usuario, senha;
        int porta;

        PrintSystem.titulo("Conexão com o DB");

        do {
            System.out.println("Informe os dados para conexão com o Banco de Dados: ");
            System.out.print("Nº da Porta: ");
            porta = Integer.parseInt(sc.nextLine()); // Evita problemas com buffer do Scanner

            System.out.print("Nome do DB: ");
            nomeDB = sc.nextLine();

            System.out.print("Usuario / Login: ");
            usuario = sc.nextLine();

            System.out.print("Senha : ");
            senha = sc.nextLine();

            if (nomeDB.isBlank() || usuario.isBlank() || senha.isBlank() || porta == 0) {
                System.out.println("Dados incompletos! Por favor, preencha todos os campos.");
            }
        } while (nomeDB.isBlank() || usuario.isBlank() || senha.isBlank() || porta == 0);

        return new DatabaseConfig(porta, nomeDB, usuario, senha);
    }
}
