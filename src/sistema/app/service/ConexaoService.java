package sistema.app.service;

import sistema.app.ui.InputHelper;
import sistema.app.ui.Terminal;
import sistema.app.util.CustomLogger;
import sistema.repository.connection.DatabaseConfig;

public class ConexaoService {

    /* Método para solicitar os dados do BD */
    public DatabaseConfig solicitarDadosConexao() {

        Terminal.titulo("Conexao com o Banco de Dados");

        String nomeDB, usuario, senha;
        int porta;

        do {
            System.out.println("Informe os dados para conexão com o Banco de Dados: ");

            System.out.print("Nº da Porta: ");
            porta = InputHelper.lerInt();

            System.out.print("Nome do DB: ");
            nomeDB = InputHelper.lerTexto();

            System.out.print("Usuario / Login: ");
            usuario = InputHelper.lerTexto();

            System.out.print("Senha : ");
            senha = InputHelper.lerTexto();

            if (nomeDB.isBlank() || usuario.isBlank() || senha.isBlank() || porta <= 0) {
                CustomLogger.logWarning("Dados incompletos! Por favor, preencha todos os campos.");
            }
        } while (nomeDB.isBlank() || usuario.isBlank() || senha.isBlank() || porta <= 0);

        return new DatabaseConfig(porta, nomeDB, usuario, senha);
    }
}
