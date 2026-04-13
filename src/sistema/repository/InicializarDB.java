package sistema.repository;

import java.util.ArrayList;
import java.util.List;

public class InicializarDB {
  public static void inicilizar(ConexaoDB conexao) {
    try {
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
}
