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
    public FolhaPagamento(int codigo, Funcionario funcionario, LocalDate dataPagamento, double descontoInss, double descontoIR, double SalarioLiquido) {
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

    public void calcularINSS() {
        double salario = funcionario.getSalarioBruto();

        if (salario <= 1518.00) {
            descontoINSS = salario * 0.075;
        } else if (salario > 1518.00 && salario <= 2793.88) {
            descontoINSS = salario * 0.09;
        } else if (salario > 2793.88 && salario <= 4190.83) {
            descontoINSS = salario * 0.12;
        } else {
            descontoINSS = salario * 0.14;
        }
    }
    public void calcularIR() {
        double base = funcionario.getSalarioBruto()
                - descontoINSS
                - (funcionario.getDependentes().size() * 189.59);

        if (base <= 2259.00) {
            descontoIR = 0;
        } else if (base > 2259.00 && base <= 2826.65) {
            descontoIR = base * 0.075 - 169.44;
        } else if (base > 2826.65 && base <= 3751.05) {
            descontoIR = base * 0.15 - 381.44;
        } else if (base > 3751.05 && base <= 4664.68) {
            descontoIR = base * 0.225 - 662.77;
        } else {
            descontoIR = base * 0.275 - 896.00;
        }
    }

    public void calcularSalarioLiquido() {
        salarioLiquido = funcionario.getSalarioBruto()
                - descontoINSS
                - descontoIR;
    }
}
