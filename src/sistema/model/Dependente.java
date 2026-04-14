package sistema.model;

import java.time.LocalDate;
import sistema.enums.Parentesco;

public class Dependente extends Pessoa {

  private Integer funcionario;
  private Parentesco parentesco;

  public Dependente() {
    super();
  }

  public Dependente(String nome, String cpf, LocalDate dataNascimento, Funcionario funcionario) {
    super(nome, cpf, dataNascimento);
    this.funcionario = funcionario.getId_funcionario();
  }

  public Parentesco getParentesco() {
    return this.parentesco;
  }

  public void setParentesco(Parentesco parente) {
    this.parentesco = parente;
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

}
