package sistema.model;

import java.time.LocalDate;

public abstract class Pessoa {
  // Atributos protegidos → acessíveis pelas classes filhas
  protected String nome;
  protected String cpf;
  protected LocalDate dataNascimento;
  // Construtor padrão (vazio)
  public Pessoa() {
  }
  // Construtor com parâmetros
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
    String cpfClean = cpf.replaceAll("\\D", "");

    // Verifica se o CPF possui exatamente 11 dígitos
    // Caso não tenha, lança uma exceção indicando que o CPF é inválido
    if (cpfClean.length() != 11) {
      throw new IllegalArgumentException("CPF inválido");
    }

    this.cpf = cpfClean;
  }

  public void setDataNacimento(LocalDate dataNascimento) {
    this.dataNascimento = dataNascimento;
  }
}

// alooo