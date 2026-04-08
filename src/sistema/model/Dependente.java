package sistema.model;
import sistema.enums.Parentesco;

public class Dependente extends Pessoa {
  
  private Parentesco parentesco;

  public Parentesco getParentesco() {
    return this.parentesco;
  }

  public void setParentesco(Parentesco parente) {
    this.parentesco = parente;
  }

  public void escolherParentesco(Parentesco opcao) {

    if (null == opcao) {
        System.out.print("ERRO");
    } else switch (opcao) {
      
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
}
