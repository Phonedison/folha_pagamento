package sistema.model;

import java.time.LocalDate;
import java.time.Period;
import sistema.enums.Parentesco;
import sistema.exception.DependenteException;

// Classe Dependente herda de Pessoa, ou seja,
// possui todos os atributos e comportamentos de Pessoa
public class Dependente extends Pessoa {
  // Criação de variaveis.
  private Integer funcionario;
  private Parentesco parentesco;
  private Integer id_dependente;

  // Construtor Vazio.
  public Dependente() {
    super();
  }

  // Construtor com atributos.
  public Dependente(String nome, String cpf, LocalDate dataNascimento, Funcionario funcionario, Parentesco parentesco) {
    super(nome, cpf, dataNascimento);
    this.funcionario = funcionario.getId_funcionario();
    this.parentesco = parentesco;
  }

  // get Parentesco
  public Parentesco getParentesco() {
    return this.parentesco;
  }

  // get id_dependente
  public Integer getId_dependente() {
    return this.id_dependente;
  }

  // set Parentesco
  public void setParentesco(Parentesco parente) {
    this.parentesco = parente;
  }

  // Método para escolher parentesco usando String.
  public void escolherParentesco(String opcao) {

    switch (opcao) {

      case "FILHO" -> setParentesco(Parentesco.FILHO);
      case "SOBRINHO" -> setParentesco(Parentesco.SOBRINHO);
      case "OUTROS" -> setParentesco(Parentesco.OUTROS);

      default -> System.out.println("ERRO");

    }
  }

  // Sobrecarga do método escolherParentesco usando número inteiro.
  public void escolherParentesco(int opcao) {

    switch (opcao) {

      case 1 -> setParentesco(Parentesco.FILHO);
      case 2 -> setParentesco(Parentesco.SOBRINHO);
      case 3 -> setParentesco(Parentesco.OUTROS);
      default -> System.out.println("ERRO");

    }
  }

  // get do Funcionario
  public Integer getFuncionario() {
    return this.funcionario;
  }

  // set do funcionario
  public void setFuncionario(Integer funcionario) {
    this.funcionario = funcionario;
  }

  // Função para Calcular a idade deixando registrar apenas < 18.
  public void validarDependente() throws DependenteException {
    int idade = Period.between(getDataNacimento(), LocalDate.now()).getYears();
    if (idade > 18) {
      throw new DependenteException("Dependente deve ter menos de 18 anos.");
    }
  }

}
