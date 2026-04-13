package sistema.repository;

import java.sql.Connection;
import java.sql.SQLException;

public class DadosConexao extends ConexaoDB {

 public DadosConexao(int porta, String meuDb, String usuario, String senha) {
    super(porta, meuDb, usuario, senha);
  }

 private ConexaoDB minhaConexao = new ConexaoDB(this.porta, this.meuDb, this.usuario, this.senha); //-> Passando o parametro de Conexao
 
  public void testeConexao(ConsultasSQL comando) {

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
