package sistema.exception;
// Exceção personalizada para representar erro de CPF duplicado
// Herda de Exception
public class CpfDuplicado extends Exception {
  // Construtor que recebe apenas a mensagem de erro
  public CpfDuplicado(String mensagem) {
    super(mensagem);
  }
  // Construtor que recebe mensagem + causa do erro
  public CpfDuplicado(String mensagem, Throwable causa) {
    super(mensagem, causa);
  }
}
