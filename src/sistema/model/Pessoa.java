package sistema.model;

import java.time.LocalDate;

public abstract class Pessoa {

  protected String nome;
  protected String cpf;
  protected LocalDate dataNascimento;

  public Pessoa() {
  }

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

    if (cpfClean.length() != 11) {
      throw new IllegalArgumentException("CPF inválido");
    }

    this.cpf = cpfClean;
  }

  public void setDataNacimento(LocalDate dataNascimento) {
    this.dataNascimento = dataNascimento;
  }
}
