package sistema.model;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

public class Funcionario extends Pessoa {
  
  private int id_funcionario;
  private double salarioBruto;
  // private double descontoInss;
  // private double descontoIR;
  private Set<Dependente> dependentes = new HashSet<>();
  /* 
  
  *  Set <tipo da lista> ->  Declarando uma lista chamado que não pode ter valor de repetido
  
  * new HashSet<> -> declarando um novo index / 'pasta organizada' de dependentes do 'FUNCIONARIO'

  */

   public Funcionario(String nome, String cpf, Date dataNacimento, double salarioBruto, Set<Dependente> dependentes) {
    super(nome, cpf, dataNacimento);
    this.salarioBruto = salarioBruto;
    this.dependentes = dependentes;
  }

  /* GET */
  public double getSalarioBruto(){
    return this.salarioBruto;
  }

  public int getId_funcionario(){
    return this.id_funcionario;
  }

  public Set<Dependente> getDependentes() {
    return dependentes;
  }

  /* SET */
  public void setSalarioBruto(double salarioBruto){ 
    this.salarioBruto = salarioBruto;
  }

  public void setDependentes (Set<Dependente> dependentes) {
    this.dependentes = dependentes;
  }

}
