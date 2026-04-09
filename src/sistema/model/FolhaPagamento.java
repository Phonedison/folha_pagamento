package sistema.model;

public class FolhaPagamento {

    private String codigo;
    private Funcionario funcionario;
    private LocalDate dataPagamento
    private double descontoINSS;
    private double descontoIR;
    private double salarioLiquido;



      // CONSUTRUTOR //
    public  FolhaPagamento(String codigo, Funcionario funcionario, Localdate dataPagamento){
        this.codigo = codigo;
        this.funcionario = funcionario;
        this.dataPagamento = dataPagamento;
    }



      //  GET   //

    public String getCodigo(){ return codigo; }

    public Funcionario getFuncionario() { return funcionario; }

    public LocalDate getDataPagamento(){ return dataPagamento; }

    public double getDescontoINSS() { return descontoINSS; }

    public double getSalarioLiquido() { return salarioLiquido; }


    //  SET   //

    public void setDescontoINSS(double descontoINSS) { this.descontoINSS(double descontoINSS) {
    }

    public void setDescontoIR(double descontoIR) { this.descontoIR = descontoIR {
    }

    public void setSalarioLiquido(double salarioLiquido) { this.salarioLiquido = salarioLiquido; {

    }



}

}

