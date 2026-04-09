package sistema.repository;

public class ComandoSQL {


  public String comandoSQL (String tipo, String select, String tabela, String parametro_pesquisa) {
    
    //teste
    switch (tipo) {

        case "select" -> {
            return sqlSelect(select, tabela, parametro_pesquisa);
        }

        case "insert into" -> {
            return sqlInsert(select, tabela, parametro_pesquisa);
        }

        case "update" -> {
            return sqlUpdate(select, tabela, parametro_pesquisa);
       
        }

        case "delete" -> {
            return sqlDelete(select, tabela, parametro_pesquisa);
        }

        default -> {
          throw new AssertionError();
        }
    }
  }

  public String sqlSelect(String select, String tabela, String parametro_pesquisa) {

    String comando;
    if(parametro_pesquisa == null){
      comando = ("SELECT " + select + " FROM " + tabela);
    
    } else {
      comando = ("SELECT " + select + " FROM " + tabela + " WHERE " + parametro_pesquisa);
    }
    return comando;
  }

  public String sqlInsert(String select, String tabela, String parametro_pesquisa) {

    String comando;
    
    if(parametro_pesquisa == null){
      comando = ("SELECT " + select + " FROM " + tabela);
    
    } else {
      comando = ("SELECT " + select + " FROM " + tabela + " WHERE " + parametro_pesquisa);
    }

    return comando;
  }
  public String sqlUpdate(String select, String tabela, String parametro_pesquisa) {

    String comando;
    
    
    if(parametro_pesquisa == null){
      comando = ("SELECT " + select + " FROM " + tabela);
    
    } else {
      comando = ("SELECT " + select + " FROM " + tabela + " WHERE " + parametro_pesquisa);
    }

    return comando;
  }
  public String sqlDelete(String select, String tabela, String parametro_pesquisa) {

    String comando;
    
    
    if(parametro_pesquisa == null){
      comando = ("SELECT " + select + " FROM " + tabela);
    
    } else {
      comando = ("SELECT " + select + " FROM " + tabela + " WHERE " + parametro_pesquisa);
    }

    return comando;
  }
 
}
