package sistema.app.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import static sistema.app.util.CustomLogger.logWarning;

public class InputHelper {

    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter ftData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private InputHelper() {
    }

    // leitura e validação do tipo Int
    public static int lerInt(String mensagem) {
        while (true) {
            try {
                System.out.printf(mensagem + " ");
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                logWarning("Entrada inválida! Digite um número inteiro.");
            }
        }
    }

    // leitura e validação do tipo Double
    public static double lerDouble(String mensagem) {
        while (true) {
            try {
                System.out.println(mensagem + " ");
                return Double.parseDouble((sc.nextLine().trim().replace(",", ".")));
            } catch (NumberFormatException e) {
                logWarning("Entrada inválida! Digite um número decimal!");
            }
        }
    }

    // leitura e validação do tipo texto
    public static String lerTexto(String mensagem) {
        System.out.print(mensagem + " ");
        return sc.nextLine().trim();
    }

    // leitura e validação do tipo Data
    public static LocalDate lerData(String mensagem) {
        while (true) {

            try {
                System.out.print(mensagem + " ");
                return LocalDate.parse(sc.nextLine().trim(), ftData);

            } catch (DateTimeParseException e) {
                logWarning("Data inválida! Use o formato dd/MM/yyyy!");
            }
        }
    }

    // leitura e validação do tipo Boolean
    public static boolean confirmar(String mensagem) {
        while (true) {

            try {

                System.out.print(mensagem + " (S/N): ");
                String resposta = sc.nextLine().trim().toUpperCase();

                if (resposta.equals("S")) {
                    return true;
                } else if (resposta.equals("N")) {
                    return false;
                }

            } catch (Exception e) {
                logWarning("Entrada inválida! Digite apenas 'S' para sim ou 'N' para não.");
            }
        }
    }

    public static boolean confirmar() {

        while (true) {
            try {
                String resposta = sc.nextLine().trim().toUpperCase();
                return resposta.equals("S");

            } catch (Exception e) {
                logWarning("Entrada inválida! Digite apenas (S/N)!");
            }
        }

    }
}

/*
 * Os métodos servem para ser utilizado nas classes e facilitar a leitura /
 * retorno dos dados passados pelo usuario!
 */