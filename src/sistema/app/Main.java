package sistema.app;

import java.sql.Date;
import java.time.LocalDate;

import scripts_backup.CalculoTesteData;
import sistema.model.Dependente;

public class Main {
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        /* Conexao DB */

        // Método para gerar a conexao do Banco
        // ConexaoDB conexao = new ConexaoDB (
        // (int porta), // porta que configurou
        // (String meuDb), // nome do DB que deu
        // (String usuario), // usuario que definiu para mexer no DB
        // (String senha)) // a senha de acesso ao DB

        // comando para executar verificacao e criação das tabelas
        // InicializarDB.inicializar(conexao);

        // ------------------------------------------------------------------------//

        /* package model / funcionario, folhaPagamento e dependente */

        // Exemplo de como usar a classe funcionario:
        // Funcionario funcionario = new Funcionario("Fulano", "123.456.789-01",
        // "2002-10-11", 1750.12, Paulo);

        // ------------------------------------------------------------------------//

        /* Exemplo de como usar as classes DAOs: */

        // FuncionarioDAO funcionarioDao = new FuncionarioDAO(conexao);
        // funcionarioDao.salvarFuncionario(funcionario);

        // CalculoTesteData data = new CalculoTesteData();
        // System.out.println(data.diferencaAno(("2000-04-13")));

    }

}
