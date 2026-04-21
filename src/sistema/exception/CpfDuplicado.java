package sistema.exception;

// Classe de exceção personalizada que representa um erro de CPF duplicado
public class CpfDuplicado extends Exception {
  // Construtor que recebe apenas uma mensagem de erro
  // Essa mensagem será exibida quando a exceção for lançada.
  public CpfDuplicado(String mensagem) {
    // Chama o construtor da classe Exception passando a mensagem.
    super(mensagem);
  }

  // Construtor que recebe uma mensagem e a causa do erro (outra exceção)
  // Isso ajuda a rastrear o motivo original do problema
  public CpfDuplicado(String mensagem, Throwable causa) {
    // Passa mensagem e causa para a classe Exception.
    super(mensagem, causa);
  }
}
