package sistema.app.menu;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CustomLogger {

  // Formatação do texto
  public static final String RESET = "\u001B[0m";
  public static final String BOLD = "\u001B[1m";
  public static final String ITALIC = "\u001B[3m";

  // Cores de Texto
  public static final String VERMELHO = "\u001B[31m";
  public static final String VERDE = "\u001B[32m";
  public static final String AMARELO = "\u001B[33m";
  public static final String AZUL = "\u001B[34m";
  public static final String ROXO = "\u001B[35m";
  public static final String CIANO = "\u001B[36m";

  // Cores de Fundo
  public static final String BG_VERMELHO = "\u001B[41m";
  public static final String BG_VERDE = "\u001B[42m";
  public static final String BG_AMARELO = "\u001B[43m";
  public static final String BG_AZUL = "\u001B[44m";
  public static final String BG_ROXO = "\u001B[45m";
  public static final String BG_CIANO = "\u001B[46m";

  public void logError(String message) {
    System.out.println(BG_VERMELHO + BOLD + " " + LocalDateTime.now() + " ERROR - " + message + RESET + " ");
  }

  public void logWarning(String message) {
    System.out.println(AMARELO + BOLD + " " + LocalDateTime.now() + " WARNING - " + message + RESET + " ");
  }

  public void logSucess(String message) {
    System.out.println(VERDE + BOLD + " " + LocalDate.now() + " SUCESS - " + message + RESET + " ");
  }

  public void logConectionSucess(String message) {
    System.out.println(BG_CIANO + BOLD + " " + LocalDateTime.now() + " CONEXAO - " + message + RESET + " ");
  }

  public void logFinal(String message) {
    System.out.println(BG_ROXO + ITALIC + " " + LocalDateTime.now() + " FINAL - " + message + RESET + " ");
  }

  public void logFolhaSucess(String message) {
    System.out.println(CIANO + ITALIC + " " + LocalDate.now() + " " + message + RESET + " ");
  }

  public void logFuncionarioSucess(String message) {
    System.out.println(VERDE + ITALIC + " " + LocalDate.now() + " " + message + RESET + " ");
  }

  public void logFuncionarioError(String message) {
    System.out.println(VERMELHO + ITALIC + " " + LocalDate.now() + " " + message + RESET + " ");
  }

  public void logDependenteError(String message) {
    System.out.println(VERMELHO + ITALIC + " " + LocalDate.now() + " " + message + RESET + " ");
  }

  public void logDependenteSucess(String message) {
    System.out.println(AMARELO + ITALIC + " " + LocalDate.now() + " " + message + RESET + " ");
  }
}
