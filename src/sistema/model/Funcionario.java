package sistema.model;

public class Funcionario extends Pessoa {
  private double salarioBruto;
  private double descontoInss;
  private double descontoIR;

  //   Get
    public double getDescontoInss() {
        return descontoInss;
    }

    public double getDescontoIR() {
        return descontoIR;
    }

    public double getSalarioBruto() {
        return salarioBruto;
    }
//    Set

    public void setDescontoInss(double descontoInss) {
        this.descontoInss = descontoInss;
    }

    public void setDescontoIR(double descontoIR) {
        this.descontoIR = descontoIR;
    }

    public void setSalarioBruto(double salarioBruto) {
        this.salarioBruto = salarioBruto;
    }
}

