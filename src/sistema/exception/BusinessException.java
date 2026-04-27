package sistema.exception;

/* Classe base de todas as exceções de regras do projeto */
public class BusinessException extends Exception {

    public BusinessException(String mensagem) {
        super(mensagem);
    }

    public BusinessException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
