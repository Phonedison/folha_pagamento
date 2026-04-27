package sistema.model;

import java.time.LocalDate;
import java.time.Period;
import sistema.enums.Parentesco;
import sistema.exception.DependenteException;

public class Dependente extends Pessoa {

  private Integer idFuncionario;
  private Integer idDependente;
  private Parentesco parentesco;

  public Dependente() {
    super();
  }

  public Dependente(String nome, String cpf, LocalDate dataNascimento, Funcionario funcionario, Parentesco parentesco) {
    super(nome, cpf, dataNascimento);
    this.idFuncionario = funcionario.getIdFuncionario();
    this.parentesco = parentesco;
  }

  /* GET */

  public Integer getIdDependente() {
    return this.idDependente;
  }

  public Integer getIdFuncionario() {
    return this.idFuncionario;
  }

  public Parentesco getParentesco() {
    return this.parentesco;
  }

  /* SET */

  public void setIdDependente(int idDependente) {
    this.idDependente = idDependente;
  }

  public void setIdFuncionario(Integer idFuncionario) {
    this.idFuncionario = idFuncionario;
  }

  public void setParentesco(Parentesco parente) {
    this.parentesco = parente;
  }

  /* METODOS */

  /*
   * Método para escolher o parentesco via String (como no caso da leitura do CSV)
   */
  public void escolherParentesco(String opcao) {

    switch (opcao.toUpperCase()) {

      case "FILHO" -> setParentesco(Parentesco.FILHO);
      case "SOBRINHO" -> setParentesco(Parentesco.SOBRINHO);
      case "OUTROS" -> setParentesco(Parentesco.OUTROS);
      default -> throw new IllegalArgumentException("Parentesco inválido: " + opcao);

    }
  }

  /* Método para validar o tipo de dependente informado */
  public void escolherParentesco(int opcao) {

    switch (opcao) {

      case 1 -> setParentesco(Parentesco.FILHO);
      case 2 -> setParentesco(Parentesco.SOBRINHO);
      case 3 -> setParentesco(Parentesco.OUTROS);
      default -> throw new IllegalArgumentException("Parentesco inválido: " + opcao);

    }
  }

  /* Método para validar a a idade do dependente */
  public void validar() throws DependenteException {
    int idade = Period.between(getDataNascimento(), LocalDate.now()).getYears();
    if (idade > 18) {
      throw new DependenteException("Dependente deve ter menos de 18 anos. Idade encontrada: " + idade);
    }
  }

}
