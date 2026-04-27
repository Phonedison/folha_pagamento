package sistema.app.ui;

public class Terminal {

    public static void titulo(String titulo) {
        System.out.println("\n==========================================");
        System.out.printf("   %s%n", titulo.toUpperCase());
        System.out.println("==========================================");
    }

    public static void separador() {
        System.out.println("------------------------------------------");
    }
}
