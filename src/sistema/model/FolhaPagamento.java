package sistema.model;

public class FolhaPagamento {

  private int codigo; 
  private Funcionario funcionario;
  private String dataPagamento; // -> Checar forma de usar o LocalDate para data;
  private double descontoInss;
  private double descontoIR;
  private double SalarioLiquido;

  /* GET */
  public int getCodigo() {
      return codigo;
  }

  public String getDataPagamento() {
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

    public void setDataPagamento(String dataPagamento) {
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
