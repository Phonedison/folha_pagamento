package sistema.exception;
// Classe de exceção personalizada para erros relacionados a Dependente
// Ela herda de Exception
public class DependenteException extends Exception {
  // Construtor que recebe apenas uma mensagem de erro
  public DependenteException(String mensagem) {
    super(mensagem);
  }
  // Construtor que recebe mensagem + causa do erro
  public DependenteException(String mensagem, Throwable causa) {
    super(mensagem, causa);
  }

}
