package sistema.model;

import java.sql.Date;
import sistema.enums.Parentesco;

public class Dependente extends Pessoa {
  private Integer id_funcionario;
  private Parentesco parentesco;

  public Dependente(String nome, String cpf, Date dataNacimento, Integer id_funcionario) {
    super(nome, cpf, dataNacimento);
    this.id_funcionario = id_funcionario;
  }

  public Parentesco getParentesco() {
    return this.parentesco;
  }

  public void setParentesco(Parentesco parente) {
    this.parentesco = parente;
  }

  public void escolherParentesco(Parentesco opcao) {

    if (null == opcao) {
      System.out.print("ERRO");
    } else
      switch (opcao) {

        case FILHO -> {
          setParentesco(Parentesco.FILHO);
          break;
        }

        case SOBRINHO -> {
          setParentesco(Parentesco.SOBRINHO);
          break;
        }

        case OUTROS -> {
          setParentesco(Parentesco.OUTROS);
          break;
        }

        default -> {
          System.out.print("ERRO");
        }
      }

  }

  public Integer getFuncionario() {
    return this.funcionario;
  }

  public void setFuncionario(Integer funcionario) {
    this.funcionario = funcionario;
  }
}
