package sistema.app.service;

import static sistema.app.ui.InputHelper.*;
import sistema.app.ui.Terminal;
import static sistema.app.util.CustomLogger.logWarning;
import sistema.repository.connection.DatabaseConfig;

public class ConexaoService {

    /* Método para solicitar os dados do BD */
    public DatabaseConfig solicitarDadosConexao() {

        Terminal.titulo("Conexão com o Banco de Dados");

        String nomeDB, usuario, senha;
        int porta;

        do {
            System.out.println("Informe os dados para conexão com o Banco de Dados: ");

            porta = lerInt("Nº da Porta: ");
            nomeDB = lerTexto("Nome do Banco de Dados: ");
            usuario = lerTexto("Usuário/Login: ");
            senha = lerTexto("Senha: ");

            if (nomeDB.isBlank() || usuario.isBlank() || senha.isBlank() || porta <= 0) {
                logWarning("Dados incompletos! Por favor, preencha todos os campos.");
            }
        } while (nomeDB.isBlank() || usuario.isBlank() || senha.isBlank() || porta <= 0);

        return new DatabaseConfig(porta, nomeDB, usuario, senha);
    }
}
