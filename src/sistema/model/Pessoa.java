package sistema.model;

import java.time.LocalDate;

public abstract class Pessoa {
  // Desclaração de Variaveis.
  protected String nome;
  protected String cpf;
  protected LocalDate dataNascimento;

  // Construtor Vazio.
  public Pessoa() {
  }

  // Contrutor com Parametros.
  public Pessoa(String nome, String cpf, LocalDate dataNascimento) {
    this.nome = nome;
    this.cpf = cpf;
    this.dataNascimento = dataNascimento;
  }

  /* GET */
  public String getNome() {
    return this.nome;
  }

  public String getCpf() {
    return this.cpf;
  }

  public LocalDate getDataNacimento() {
    return this.dataNascimento;
  }

  /* SET */
  public void setNome(String nome) {
    this.nome = nome;
  }

  public void setCpf(String cpf) {
    // Remove tudo que não for número do CPF (pontos, traços, espaços etc.).
    String cpfClean = cpf.replaceAll("\\D", "");

    // Valida se o CPF possui exatamente 11 dígitos após a limpeza.
    if (cpfClean.length() != 11) {
      throw new IllegalArgumentException("CPF inválido");
    }
    // Atribui o CPF já limpo (somente números) ao atributo da classe.
    this.cpf = cpfClean;
  }

  public void setDataNacimento(LocalDate dataNascimento) {
    // Define a data de nascimento do objeto.
    this.dataNascimento = dataNascimento;
  }
}
