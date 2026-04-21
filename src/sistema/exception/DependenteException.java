package sistema.exception;

// Declara uma classe de exceção personalizada chamada DependenteException
// Ela herda de Exception, ou seja, é uma exceção verificada.
public class DependenteException extends Exception {
  // Construtor que recebe apenas uma mensagem de erro
  // Essa mensagem será passada para a classe pai.
  public DependenteException(String mensagem) {
    // chama o construtor da superclasse com a mensagem.
    super(mensagem);
  }

  // Construtor que recebe uma mensagem e a causa da exceção.
  public DependenteException(String mensagem, Throwable causa) {
    // passa mensagem e causa para a Exception.
    super(mensagem, causa);
  }

}
