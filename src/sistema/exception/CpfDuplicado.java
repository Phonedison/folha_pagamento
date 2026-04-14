package sistema.exception;

public class CpfDuplicado extends Exception {

  public CpfDuplicado(String mensagem) {
    super(mensagem);
  }

  public CpfDuplicado(String mensagem, Throwable causa) {
    super(mensagem, causa);
  }
}
