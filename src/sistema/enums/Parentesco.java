package sistema.enums;

public enum Parentesco {
    
    FILHO(1), SOBRINHO(2), OUTROS(3);

    private final int valor;
    
    Parentesco (int valorOpcao) {
        this.valor = valorOpcao;
    }
    public int getValor() {
        return this.valor;
    }

}
        