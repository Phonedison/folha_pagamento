package sistema.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
// Classe Funcionario herda de Pessoa (herança)
// Ou seja, já possui: nome, cpf e dataNascimento
public class Funcionario extends Pessoa {
  // ID único do funcionário (chave primária no banco)
  private int id_funcionario;
  // Salário bruto do funcionário
  private double salarioBruto;
  // private double descontoInss;
  // private double descontoIR;

  // Conjunto de dependentes do funcionário
  // Set não permite elementos duplicados
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
  // Construtor padrão (vazio)
  public Funcionario() {
    super();
  }
  // Construtor com parâmetros
  public Funcionario(String nome, String cpf, LocalDate dataNacimento, double salarioBruto,
      Set<Dependente> dependentes) {
    super(nome, cpf, dataNacimento);
    this.salarioBruto = salarioBruto;
  }

  /* GET */
  public double getSalarioBruto() {
    return this.salarioBruto;
  }

  public int getId_funcionario() {
    return this.id_funcionario;
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

  public void setId_funcionario(int id_funcionario) {
    this.id_funcionario = id_funcionario;
  }
}
