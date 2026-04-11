package sistema.model;

import java.time.LocalDate;

public class FolhaPagamento {

    private int codigo;
    private Funcionario funcionario;
    private LocalDate dataPagamento;
    private double descontoInss;
    private double descontoIR;
    private double SalarioLiquido;

    /* CONSUTRUTOR */
    public FolhaPagamento(int codigo, Funcionario funcionario, LocalDate dataPagamento, double descontoInss,
            double descontoIR, double SalarioLiquido) {
        this.codigo = codigo;
        this.funcionario = funcionario;
        this.dataPagamento = dataPagamento;
        this.descontoInss = descontoInss;
        this.descontoIR = descontoIR;
        this.SalarioLiquido = SalarioLiquido;
    }

    /* GET */
    public int getCodigo() {
        return codigo;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public double getDescontoInss() {
        return descontoInss;
    }

    public double getDescontoIR() {
        return descontoIR;
    }

    public double getSalarioLiquido() {
        return SalarioLiquido;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    /* SET */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public void setDescontoInss(double descontoInss) {
        this.descontoInss = descontoInss;
    }

    public void setDescontoIR(double descontoIR) {
        this.descontoIR = descontoIR;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public void setSalarioLiquido(double SalarioLiquido) {
        this.SalarioLiquido = SalarioLiquido;
    }
}
