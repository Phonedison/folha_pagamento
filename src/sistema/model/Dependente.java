package sistema.model;

import java.time.LocalDate;
import java.time.Period;
import sistema.enums.Parentesco;
import sistema.exception.DependenteException;

public class Dependente extends Pessoa {

  private Integer funcionario;
  private Parentesco parentesco;
  private Integer idDependente;

  public Dependente() {
    super();
  }

  public Dependente(String nome, String cpf, LocalDate dataNascimento, Funcionario funcionario, Parentesco parentesco) {
    super(nome, cpf, dataNascimento);
    this.funcionario = funcionario.getIdFuncionario();
    this.parentesco = parentesco;
  }

  public Parentesco getParentesco() {
    return this.parentesco;
  }

  public Integer getIdDependente() {
    return this.idDependente;
  }

  public void setIdDependente(int idDependente) {
    this.idDependente = idDependente;
  }

  public void setParentesco(String parente) {
    this.parentesco = parente;
  }

  public void escolherParentesco(String opcao) {

    switch (opcao) {

      case "FILHO" -> setParentesco(Parentesco.FILHO);
      case "SOBRINHO" -> setParentesco(Parentesco.SOBRINHO);
      case "OUTROS" -> setParentesco(Parentesco.OUTROS);
      default -> System.out.println("ERRO");

    }
  }

  public void escolherParentesco(int opcao) {

    switch (opcao) {

      case 1 -> setParentesco(Parentesco.FILHO);
      case 2 -> setParentesco(Parentesco.SOBRINHO);
      case 3 -> setParentesco(Parentesco.OUTROS);
      default -> System.out.println("ERRO");

    }
  }

  public Integer getFuncionario() {
    return this.funcionario;
  }

  public void setFuncionario(Integer funcionario) {
    this.funcionario = funcionario;
  }

  public void validarDependente() throws DependenteException {
    int idade = Period.between(getDataNacimento(), LocalDate.now()).getYears();
    if (idade > 18) {
      throw new DependenteException("Dependente deve ter menos de 18 anos.");
    }
  }

}
