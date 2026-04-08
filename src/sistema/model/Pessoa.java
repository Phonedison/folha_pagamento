package sistema.model;

public class Pessoa {
  
  private  String nome;
  private String cpf;
  private String dataNacimento;

  /* GET */
  public String getNome (){
    return this.nome;
  }

  public String getCpf() {
    return this.cpf;
  }

  public String getDataNacimento(){
    return this.dataNacimento;
  }
  
  /* SET */
  public void setNome (String nome){
    this.nome = nome;
  }

  public void setCpf(String cpf) {
   String cpfClean = cpf.trim();
   int tamanho = cpfClean.length();

    if (tamanho == 11) {
      this.cpf =
                cpfClean.substring(0,3) + "." +
                cpfClean.substring(3,6) + "." +
                cpfClean.substring(6,9) + "-" +
                cpfClean.substring(9,11);

    } else if (tamanho == 14) {
      this.cpf = cpfClean;

    } else {
      System.out.println("Formato de CPF inválido!");
      System.out.println("Use: 000.000.000-00");
    }
   
  }

  public void setDataNacimento(String dataNacimento){
     this.dataNacimento = dataNacimento;
  }
}
