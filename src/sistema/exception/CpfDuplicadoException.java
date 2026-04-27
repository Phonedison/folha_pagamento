package sistema.exception;

public class CpfDuplicadoException extends BusinessException {

  public CpfDuplicadoException(String mensagem) {
    super(mensagem);
  }

  public CpfDuplicadoException(String mensagem, Throwable causa) {
    super(mensagem, causa);
  }
}
