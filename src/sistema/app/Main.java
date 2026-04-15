package sistema.app;

import sistema.app.menu.Menus;
import sistema.repository.ConexaoDB;
import sistema.repository.InicializarDB;

public class Main {

    public static void main(String[] args) throws Exception {
        Menus menu = new Menus();

        ConexaoDB conexaoDB = menu.conexao_db();
        InicializarDB.inicializar(conexaoDB);

        menu.execute();
    }

}
