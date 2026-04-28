package sistema.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Funcionario extends Pessoa {

  private int idFuncionario;
  private double salarioBruto;
  // private double descontoInss;
  // private double descontoIR;
  private Set<Dependente> dependentes = new HashSet<>();
  /*
   * 
   * Set <tipo da lista> -> Declarando uma lista chamado que não pode ter valor de
   * repetido
   * 
   * new HashSet<> -> declarando um novo index / 'pasta organizada' de dependentes
   * do 'FUNCIONARIO'
   * 
   */

  public Funcionario() {
    super();
  }

  public Funcionario(String nome, String cpf, LocalDate dataNascimento, double salarioBruto,
      Set<Dependente> dependentes) {
    super(nome, cpf, dataNascimento);
    this.salarioBruto = salarioBruto;
    this.dependentes = dependentes;
  }

  /* GET */
  public double getSalarioBruto() {
    return this.salarioBruto;
  }

  public int getIdFuncionario() {
    return this.idFuncionario;
  }

  public Set<Dependente> getDependentes() {
    return dependentes;
  }

  /* SET */
  public void setSalarioBruto(double salarioBruto) {
    this.salarioBruto = salarioBruto;
  }

  public void setDependentes(Set<Dependente> dependentes) {
    this.dependentes = dependentes;
  }

  public void setIdFuncionario(int idFuncionario) {
    this.idFuncionario = idFuncionario;
  }
}
