package sistema.app;
import scripts_backup.Menus;
import sistema.repository.InicializarDB;

import sistema.repository.ConexaoDB;



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

        /* Conexao DB */

    // Método para gerar a conexao do Banco

        Menus conexao = new Menus();
        conexao.menuConexao();

    // Comando para executar verificacao e criação das tabelas

        InicializarDB.inicializar(conexao.menuConexao());

        Menus opcao = new Menus();
        opcao.menuOpcoes();

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
