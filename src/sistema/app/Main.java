package sistema.app;

import sistema.app.menu.Menus;

public class Main {

    public static void main(String[] args) throws Exception {
        Menus menu = new Menus();

        // ConexaoDB conexaoDB = menu.conexao_db();
        // InicializarDB.inicializar(conexaoDB);

        menu.execute();

    }

}
