package sistema.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.Buffer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import sistema.model.Dependente;
import sistema.model.Funcionario;

public class LeitorCSV {

  public List<Funcionario> lerArquivo(String caminhoArquivo) {
    List<Funcionario> funcionarios = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader("./data/arquivo.csv"))) {

      String linha;
      Funcionario funcionarioAtual = null;

      while ((linha = br.readLine()) != null) {
        linha = linha.trim();

        // ignora linhas vazias
        if (linha.isEmpty()) {
          funcionarioAtual = null;
          continue;
        }

        String[] dados = linha.split(";");

        if (dados.length != 4) {
          System.out.println("Linha ignorada por formato incorreto: " + linha);
          continue;
        }

        if (isSalario(dados[3])) {
          funcionarioAtual = new Funcionario();
          funcionarioAtual.setNome(dados[0]);
          funcionarioAtual.setCpf(dados[1]);
          funcionarioAtual.setDataNacimento(LocalDate.parse(formatarData(dados[2])));
          funcionarioAtual.setSalarioBruto(Double.parseDouble(dados[3]));
          funcionarios.add(funcionarioAtual);

        } else if (funcionarioAtual != null) {
          try {

            Dependente dependente = new Dependente();
            dependente.setNome(dados[0]);
            dependente.setCpf(dados[1]);
            dependente.setDataNacimento(LocalDate.parse(formatarData(dados[2])));
            funcionarioAtual.getDependentes().add(dependente);

          } catch (Exception error) {
            System.out.println("Erro ao processar dependente: " + linha);
            System.out.println("Erro : " + error.getMessage());
          }
        }
      }

    } catch (Exception error) {

    }
  }

  private String formatarData(String data) {
    return data.substring(0, 4) + "-" + data.substring(4, 6) + "-" + data.substring(6, 8);
  }

  private boolean isSalario(String salario) {
    return salario.matches("\\d+\\.\\d{2}");
  }
}
