package sistema.model;

import java.time.LocalDate;

public class FolhaPagamento {
    // Criação de Variaveis.
    private int codigo;
    private Funcionario funcionario;
    private LocalDate dataPagamento;
    private double descontoInss;
    private double descontoIR;
    private double salarioLiquido;

    // Construtor Vazio
    public FolhaPagamento() {

    }

    // Construtor com parametros.
    public FolhaPagamento(int codigo, Funcionario funcionario, LocalDate dataPagamento, double descontoInss,
            double descontoIR, double salarioLiquido) {
        this.codigo = codigo;
        this.funcionario = funcionario;
        this.dataPagamento = dataPagamento;
        this.descontoInss = descontoInss;
        this.descontoIR = descontoIR;
        this.salarioLiquido = salarioLiquido;
    }

    /* GET */
    public int getCodigo() {
        return this.codigo;
    }

    public LocalDate getDataPagamento() {
        return this.dataPagamento;
    }

    public double getDescontoInss() {
        return this.descontoInss;
    }

    public double getDescontoIR() {
        return this.descontoIR;
    }

    public double getSalarioLiquido() {
        return this.salarioLiquido;
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

    // public void setFuncionario(int funcionario) {
    // this.funcionario.setId_funcionario(funcionario);
    // }

    public void setSalarioLiquido(double salarioLiquido) {
        this.salarioLiquido = salarioLiquido;
    }

    public void calcularINSS() {
        double salario = funcionario.getSalarioBruto();
        double teto = 8157.41;

        double base = Math.min(salario, teto);

        if (salario <= 1518.00) {
            this.descontoInss = base * 0.075;

        } else if (salario <= 2793.88) {
            this.descontoInss = base * 0.09;

        } else if (salario <= 4190.83) {
            this.descontoInss = base * 0.12;

        } else {
            this.descontoInss = base * 0.14;
        }
    }

    // Calcula a base de cálculo do Imposto de Renda.
    public void calcularIR() {
        double base = funcionario.getSalarioBruto()
                - this.descontoInss
                - (funcionario.getDependentes().size() * 189.59);
        // Se a base for menor ou igual a 2259, não há desconto de IR.
        if (base <= 2259.00) {
            this.descontoIR = 0;
            // Aplica 7,5%.
        } else if (base <= 2826.65) {
            this.descontoIR = (base * 0.075) - 169.44;
            // Aplica 15%.
        } else if (base <= 3751.05) {
            this.descontoIR = (base * 0.15) - 381.44;
            // Aplica 22,5%.
        } else if (base <= 4664.68) {
            this.descontoIR = (base * 0.225) - 662.77;
            // Aplica 27,5%.
        } else {
            this.descontoIR = (base * 0.275) - 896.00;
        }
    }

    // Calcula o salário líquido do funcionário.
    public void calcularSalarioLiquido() {
        this.salarioLiquido = funcionario.getSalarioBruto()
                - this.descontoInss
                - this.descontoIR;
    }

    // Define o funcionário atual para este objeto.
    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }
}
