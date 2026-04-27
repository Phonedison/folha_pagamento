package sistema.exception;

public class DependenteException extends BusinessException {

  public DependenteException(String mensagem) {
    super(mensagem);
  }

  public DependenteException(String mensagem, Throwable causa) {
    super(mensagem, causa);
  }

}
