package sistema.app.ui;

public class Terminal {
    // metodo para imprimir o título dos menus, deixando a interface mais organizada
    // e fácil de ler
    public static void titulo(String titulo) {
        System.out.println("\n==========================================");
        System.out.printf("   %s%n", titulo);
        System.out.println("==========================================");
    }
}
