package sistema.repository;

import java.sql.Connection;
import java.sql.SQLException;

public class ComandosSQLs {

 
 private ConexaoDB minhaConexao = new ConexaoDB(5432, "meu_banco", "admin", "123"); //-> Passando o parametro de Conexao
 
 public void testeConexao(ComandoSQL comando) {
   try (Connection conexao = minhaConexao.conectarDB()) { //-> Conectando ao Banco
   
    //QUALQUER COMANDO SQL PRESENTE AQUI
    //CLASSE QUE VAI CONTER TODOS OS COMANDO
    //PASSAR ESSES COMANDOS PARA CÁ
    // comandoSQL(tipo,select, tabela, parametro_pesquisa);

    } catch (SQLException error) {
    
      //VAI FECHAR E APRESENTAR O ERRO
      error.printStackTrace();
    
    }
 }


}
