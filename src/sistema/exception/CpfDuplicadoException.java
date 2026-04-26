package sistema.exception;

public class CpfDuplicadoException extends Exception {

  public CpfDuplicadoException(String mensagem) {
    super(mensagem);
  }

  public CpfDuplicadoException(String mensagem, Throwable causa) {
    super(mensagem, causa);
  }
}
