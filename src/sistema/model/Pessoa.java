package sistema.model;

import java.sql.Date;

public abstract class Pessoa {

  protected String nome;
  protected String cpf;
  protected Date dataNascimento;

  public Pessoa(String nome, String cpf, Date dataNascimento) {
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

  public Date getDataNacimento() {
    return this.dataNascimento;
  }

  /* SET */
  public void setNome(String nome) {
    this.nome = nome;
  }

  public void setCpf(String cpf) {
    String cpfClean = cpf.trim();
    int tamanho = cpfClean.length();

    if (tamanho == 11) {
      this.cpf = cpfClean.substring(0, 3) + "." +
          cpfClean.substring(3, 6) + "." +
          cpfClean.substring(6, 9) + "-" +
          cpfClean.substring(9, 11);

    } else if (tamanho == 14) {
      this.cpf = cpfClean;

    } else {
      System.out.println("Formato de CPF inválido!");
      System.out.println("Use: 000.000.000-00");
    }

  }

  public void setDataNacimento(Date dataNascimento) {
    this.dataNascimento = dataNascimento;
  }
}

// alooo