package scripts_backup;

public class ConsultasSQL {

  /* ver métodologia utilizando DAO -> OBSOLETO PORÉM assunto da matéria */
  public String comandoSQL(String tipo, String select, String tabela, String parametro_pesquisa) {
    // O método 'comandoSQL' recebe os parâmetros 'tipo' (tipo de comando SQL),
    // 'select' (campos a serem selecionados), 'tabela' (nome da tabela),
    // e 'parametro_pesquisa' (condição de busca ou 'null' se não houver).

    // Inicia um switch que vai decidir o que fazer com base no tipo de SQL (select,
    // insert, etc.).
    switch (tipo) {

      case "select" -> {
        // Se o tipo for "select", chama o método 'sqlSelect' passando os parâmetros
        // necessários
        return sqlSelect(select, tabela, parametro_pesquisa);
      }

      case "insert into" -> {
        // Se o tipo for "insert into", chama o método 'sqlInsert' com os parâmetros
        // necessários.
        return sqlInsert(select, tabela, parametro_pesquisa);
      }

      case "update" -> {
        // Se o tipo for "update", chama o método 'sqlUpdate' com os parâmetros
        // necessários.
        return sqlUpdate(select, tabela, parametro_pesquisa);

      }

      case "delete" -> {
        // Se o tipo for "delete", chama o método 'sqlDelete' com os parâmetros
        // necessários.
        return sqlDelete(select, tabela, parametro_pesquisa);
      }

      default -> {
        // Se o tipo não for nenhum dos anteriores, lança um erro.
        throw new AssertionError();
      }
    }
  }

  // Método responsável por montar o comando SQL de SELECT.
  public String sqlSelect(String select, String tabela, String parametro_pesquisa) {
    // Declara a variável 'comando' para armazenar a consulta SQL gerada.
    String comando;
    if (parametro_pesquisa == null) {
      // Se não houver parâmetro de pesquisa (WHERE), monta o comando SELECT simples:
      comando = ("SELECT " + select + " FROM " + tabela);

    } else {
      // Caso contrário, monta o comando SELECT com a condição WHERE:
      comando = ("SELECT " + select + " FROM " + tabela + " WHERE " + parametro_pesquisa);
    }
    // Retorna o comando SQL montado.
    return comando;
  }

  // Método responsável por montar o comando SQL de INSERT.
  public String sqlInsert(String select, String tabela, String parametro_pesquisa) {
    // Declara a variável 'comando' para armazenar a consulta SQL gerada.
    String comando;

    if (parametro_pesquisa == null) {
      // Se não houver parâmetro de pesquisa, o código tenta montar uma consulta
      // SELECT (erro lógico).
      comando = ("SELECT " + select + " FROM " + tabela);

    } else {
      // Caso contrário, tenta adicionar um WHERE (erro lógico)
      comando = ("SELECT " + select + " FROM " + tabela + " WHERE " + parametro_pesquisa);
    }
    // Retorna o comando SQL gerado (não está correto para INSERT).
    return comando;
  }

  // Método responsável por montar o comando SQL de UPDATE.
  public String sqlUpdate(String select, String tabela, String parametro_pesquisa) {
    // Declara a variável 'comando' para armazenar a consulta SQL gerada.
    String comando;

    if (parametro_pesquisa == null) {
      // Se não houver parâmetro de pesquisa, o código tenta montar uma consulta
      // SELECT (erro lógico).
      comando = ("SELECT " + select + " FROM " + tabela);

    } else {
      // Caso contrário, tenta adicionar um WHERE (erro lógico).
      comando = ("SELECT " + select + " FROM " + tabela + " WHERE " + parametro_pesquisa);
    }
    // Retorna o comando SQL gerado (não está correto para UPDATE).
    return comando;
  }

  // Método responsável por montar o comando SQL de DELETE.
  public String sqlDelete(String select, String tabela, String parametro_pesquisa) {
    // Declara a variável 'comando' para armazenar a consulta SQL gerada.
    String comando;

    if (parametro_pesquisa == null) {
      // Se não houver parâmetro de pesquisa, o código tenta montar uma consulta
      // SELECT (erro lógico).
      comando = ("SELECT " + select + " FROM " + tabela);

    } else {
      // Caso contrário, tenta adicionar um WHERE (erro lógico).
      comando = ("SELECT " + select + " FROM " + tabela + " WHERE " + parametro_pesquisa);
    }
    // Retorna o comando SQL gerado (não está correto para DELETE).
    return comando;
  }

}
