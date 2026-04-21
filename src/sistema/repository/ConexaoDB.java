package sistema.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException; // classe que permite fornecer informações sobre erros durante a conexao com banco
import sistema.app.menu.CustomLogger;

public class ConexaoDB {
  // Instância de um logger personalizado para registrar erros.
  CustomLogger customLogger = new CustomLogger();
  // Variaveis para informar os parametros para conectar no banco de dados.
  protected int porta;
  protected String meuDb;
  protected String usuario;
  protected String senha;
  protected String stringConexao;

  // Construtor da classe que recebe os dados necessários para conexão.
  public ConexaoDB(int porta, String meuDb, String usuario, String senha) {
    this.porta = porta;
    this.meuDb = meuDb;
    this.senha = senha;
    this.usuario = usuario;
    gerarConexao();
  }

  // Método público para conectar ao banco de dados
  public Connection conectarDB() throws SQLException {
    try {
      // Carrega o driver JDBC do PostgreSQL.
      Class.forName("org.postgresql.Driver");
      // Chama o método privado que realiza a conexão.
      return conectar();

    } catch (ClassNotFoundException error) {
      // Caso o driver não seja encontrado, registra o erro.
      customLogger.logError("Drive não encontrado:");
      throw new RuntimeException(error.getMessage());

    } catch (SQLException error) {
      // Caso ocorra erro de SQL, apenas repassa a exceção.
      throw error;
    }
  }

  // Método responsável por montar a string de conexão JDBC.
  public void gerarConexao() {
    this.stringConexao = "jdbc:postgresql://localhost:" + this.porta + "/" + this.meuDb;
  }

  // Método privado que efetivamente abre a conexão com o banco.
  private Connection conectar() throws SQLException {
    // Usa o DriverManager para obter a conexão passando URL, usuário e senha.
    return DriverManager.getConnection(this.stringConexao, this.usuario, this.senha);
  }

}
