package sistema.model;

import java.util.HashSet;
import java.util.Set;

public class Funcionario extends Pessoa {
  
  private double salarioBruto;
  private double descontoInss;
  private double descontoIR;
  private Set<Dependente> dependentes = new HashSet<>();
  /* 
  
  *  Set <tipo da lista> ->  Declarando uma lista chamado que não pode ter valor de repetido
  
  * new HashSet<> -> declarando um novo index / 'pasta organizada' de dependentes do 'FUNCIONARIO'

  */

  /* GET */
  public double getSalarioBruto(){
    return this.salarioBruto;
  }

  public double getDescontoInss() {
    return this.descontoInss;
  }

  public double getDescontoIR() {
    return this. descontoIR;
  }

  public Set<Dependente> getDependentes() {
    return dependentes;
  }

  /* SET */
  public void setSalarioBruto(double salarioBruto){ 
    this.salarioBruto = salarioBruto;
  }

  public void setDescontoInss(double descontoInss) {
    this.descontoInss = descontoInss;
  }

  public void setDescontoIR(double descontoIR) {
    this.descontoIR = descontoIR;
  }

  public void setDependentes (Set<Dependente> dependentes) {
    this.dependentes = dependentes;
  }

}
