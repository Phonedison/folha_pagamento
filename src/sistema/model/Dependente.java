package sistema.model;

import java.time.LocalDate;
import java.time.Period;
import sistema.enums.Parentesco;
import sistema.exception.DependenteException;
// Classe Dependente herda de Pessoa (nome, cpf, dataNascimento)
public class Dependente extends Pessoa {
  // Guarda o ID do funcionário responsável por esse dependente
  private Integer funcionario;
  // Enum que representa o tipo de parentesco
  private Parentesco parentesco;
  // ID único do dependente, chave primária no banco
  private Integer id_dependente;
  // Construtor padrão
  public Dependente() {
    super();
  }
  // Construtor com parâmetros
  public Dependente(String nome, String cpf, LocalDate dataNascimento, Funcionario funcionario, Parentesco parentesco) {
    super(nome, cpf, dataNascimento);
    // Armazena apenas o ID do funcionário (e não o objeto inteiro)
    this.funcionario = funcionario.getId_funcionario();
    // Define o parentesco
    this.parentesco = parentesco;
  }

  public Parentesco getParentesco() {
    return this.parentesco;
  }

  public Integer getId_dependente() {
    return this.id_dependente;
  }

  public void setParentesco(Parentesco parente) {
    this.parentesco = parente;
  }

  //Método de menu para escolha de parentesco de dependente
  public void escolherParentesco(String opcao) {

    switch (opcao) {

      case "FILHO" -> setParentesco(Parentesco.FILHO);
      case "SOBRINHO" -> setParentesco(Parentesco.SOBRINHO);
      case "OUTROS" -> setParentesco(Parentesco.OUTROS);
      default -> System.out.println("ERRO");

    }
  }

  //Método para definir parentesco
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

  //Método de validação do dependente pela idade
  public void validarDependente() throws DependenteException {
    int idade = Period.between(getDataNacimento(), LocalDate.now()).getYears();
    if (idade > 18) {
      throw new DependenteException("Dependente deve ter menos de 18 anos.");
    }
  }

}
