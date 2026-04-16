package sistema.app.menu;

import java.time.LocalDateTime;

public class CustomLogger {
  public static final String RESET = "\u001B[0m";
  public static final String RED = "\u001B[31m";
  public static final String YELLOW = "\u001B[33m";
  public static final String CYAN = "\u001B[36m";

  public void logError(String message) {
    System.out.println(" " + RED + LocalDateTime.now() + " ERROR - " + message + RESET);
    System.out.println("");
  }

  public void logWarning(String message) {
    System.out.println(" " + YELLOW + LocalDateTime.now() + " WARNING - " + message + RESET);
    System.out.println("");
  }

  public void logSucess(String message) {
    System.out.println(" " + CYAN + LocalDateTime.now() + " SUCESS - " + message + RESET);
    System.out.println("");
  }

}
