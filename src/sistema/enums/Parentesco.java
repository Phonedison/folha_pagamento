package sistema.enums;

    // Declaração de um enum chamado Parentesco
public enum Parentesco {

    // Definição das constantes do enum com valores associados  
    FILHO(1), SOBRINHO(2), OUTROS(3);

    // Atributo privado que guarda o valor numérico de cada constante
    private final int valor;

   
    // Construtor do enum (é chamado automaticamente para cada constante)
    Parentesco(int valorOpcao){
        this.valor = valorOpcao;
    }
     
    // Método público para acessar o valor associado ao enum
    public int getValor(){
    return this.valor;
    }
}
        