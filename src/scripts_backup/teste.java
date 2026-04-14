package scripts_backup;

public class teste {
  public static void main(String[] args) {
    TabelaJava();
  }

  public static void TabelaJava() {
    String[] nomes = { "Ana", "João Victor", "Bia" };
    int[] idades = { 25, 30, 22 };

    // Cabeçalho da Tabela
    System.out.printf("%-15s | %-10s%n", "NOME", "IDADE");
    System.out.println("----------------------------");

    // Corpo da Tabela com for
    for (int i = 0; i < nomes.length; i++) {
      // %-15s reserva 15 caracteres para o nome
      // %-10d reserva 10 caracteres para a idade
      // %n pula uma linha
      System.out.printf("%-15s | %-10d%n", nomes[i], idades[i]);
    }
  }
}
