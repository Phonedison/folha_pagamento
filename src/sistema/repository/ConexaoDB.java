package sistema.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException; // classe que permite fornecer informações sobre erros durante a conexao com banco
import sistema.app.menu.CustomLogger;

public class ConexaoDB {
  CustomLogger customLogger = new CustomLogger();

  protected int porta;
  protected String meuDb;
  protected String usuario;
  protected String senha;
  protected String stringConexao;

  public ConexaoDB(int porta, String meuDb, String usuario, String senha) {
    this.porta = porta;
    this.meuDb = meuDb;
    this.senha = senha;
    this.usuario = usuario;
    gerarConexao();
  }

  public Connection conectarDB() throws SQLException {
    try {

      Class.forName("org.postgresql.Driver");

      return conectar();

    } catch (ClassNotFoundException error) {
      customLogger.logError("Drive não encontrado:");
      throw new RuntimeException(error.getMessage());

    } catch (SQLException error) {
      throw error;
    }
  }

  public void gerarConexao() {
    this.stringConexao = "jdbc:postgresql://localhost:" + this.porta + "/" + this.meuDb;
  }

  private Connection conectar() throws SQLException {

    return DriverManager.getConnection(this.stringConexao, this.usuario, this.senha);
  }

}
