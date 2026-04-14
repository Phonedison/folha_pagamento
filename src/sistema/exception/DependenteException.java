package sistema.exception;

public class DependenteException extends Exception {

  public DependenteException(String mensagem) {
    super(mensagem);
  }

  public DependenteException(String mensagem, Throwable causa) {
    super(mensagem, causa);
  }

}
