package sistema.app.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import sistema.app.util.CustomLogger;

public class InputHelper {

    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter ftData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private InputHelper() {
    }

    // leitura e validação do tipo Int
    public static int lerInt() {
        try {
            int valor = Integer.parseInt(sc.nextLine().trim());
            return valor;
        } catch (NumberFormatException e) {
            CustomLogger.logWarning("Entrada inválida! Digite um número inteiro.");
            return -1;
        }
    }

    // leitura e validação do tipo Double
    public static double lerDouble() {
        try {
            return Double.parseDouble((sc.nextLine().trim().replace(",", ".")));
        } catch (Exception e) {
            CustomLogger.logWarning("Entrada inválida! Digite um número decimal.");
            return -1;
        }
    }

    // leitura e validação do tipo texto
    public static String lerTexto() {
        return sc.nextLine().trim();
    }

    // leitura e validação do tipo Data
    public static LocalDate lerData() {
        try {
            return LocalDate.parse(sc.nextLine().trim(), ftData);
        } catch (Exception e) {
            CustomLogger.logWarning("Data inválida! Use o formato dd/MM/yyyy.");
            return null;
        }
    }

    // leitura e validação do tipo Boolean
    public static boolean confirmar() {
        String resposta = sc.nextLine().trim().toUpperCase();
        return resposta.equals("S");
    }
}

/*
 * Os métodos servem para ser utilizado nas classes e facilitar a leitura /
 * retorno dos dados passados pelo usuario!
 */