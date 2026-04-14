package sistema.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InicializarDB {
  public static void inicializar(ConexaoDB conexao) {
    try (Connection con = conexao.conectarDB()) {

      verificarEnum(con); // Verifica se o enum existe, caso contrário, cria o enum
      // Criação da lista para todos os nossos DAOs que precisam de uma 'tabela'
      List<CriacaoTabela> listaDAOs = new ArrayList<>();

      // Adicionando os DAOs a lista levando em consideração a chaves estrangeiras!
      listaDAOs.add(new FuncionarioDAO(conexao));
      listaDAOs.add(new DependenteDAO(conexao));
      listaDAOs.add(new FolhaPagamentoDAO(conexao));

      // utilizando 'for in' eitera os objetos e executa o método
      for (CriacaoTabela objetoDAO : listaDAOs) {
        objetoDAO.criarTabela();
      }
      // informa se funcionar
      System.out.print("Banco de Dados verificado e criado as tabelas com sucesso!");

    } catch (Exception e) {
      // se tiver erro, informa
      throw new RuntimeException("Erro na criação da TABELA: " + e.getMessage(), e);
    }
  }

  // criado método para verificar se o enum existe, caso contrário, criar o enum
  // utilizando o comando SQL
  private static void verificarEnum(Connection con) throws SQLException {
    String comandoSQL = "DO $$ BEGIN " +
        "IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'parentesco') THEN " +
        "CREATE TYPE parentesco AS ENUM ('FILHO', 'SOBRINHO', 'OUTROS'); " +
        "END IF; END $$;";

    try (PreparedStatement stmt = con.prepareStatement(comandoSQL)) {
      stmt.execute();
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao verificar/criar enum: " + e.getMessage(), e);
    }
  }
}
