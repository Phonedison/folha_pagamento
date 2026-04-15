package sistema.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import sistema.model.Dependente;
import sistema.model.Funcionario;

public class LeitorCSV {

  public List<Funcionario> lerArquivo(String caminhoArquivo) {
    List<Funcionario> funcionarios = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(
        new InputStreamReader(
            new FileInputStream(caminhoArquivo),
            StandardCharsets.UTF_8));) {

      String linha;
      Funcionario funcionarioAtual = null;

      while ((linha = br.readLine()) != null) {

        linha = linha.trim();

        // ignora linhas vazias
        if (linha.isEmpty() || linha.equals(",,,")) {
          funcionarioAtual = null;
          continue;
        }

        linha = linha.replace("\"", "");

        String[] dados = linha.split("[;,]");
        //
        // if (dados.length != 4) {
        // System.out.println(linha);
        // continue;
        // }

        if (isSalario(dados[3])) {
          funcionarioAtual = new Funcionario();
          funcionarioAtual.setNome(dados[0]);
          funcionarioAtual.setCpf(dados[1]);
          funcionarioAtual.setDataNacimento(formatarData(dados[2]));
          funcionarioAtual.setSalarioBruto(Double.parseDouble(dados[3].replace(",", ".")));
          funcionarios.add(funcionarioAtual);

        } else if (funcionarioAtual != null) {
          try {

            Dependente dependente = new Dependente();
            dependente.setNome(dados[0]);
            dependente.setCpf(dados[1]);
            dependente.setDataNacimento(formatarData(dados[2]));
            funcionarioAtual.getDependentes().add(dependente);

          } catch (Exception er) {
            System.out.println("Erro ao processar dependente: " + linha);
            System.out.println("Erro : " + er.getMessage());
          }
        }
      }

    } catch (Exception error) {
      throw new RuntimeException("Erro ao ler arquivo: " + error.getMessage());

    }
    return funcionarios;
  }

  private LocalDate formatarData(String data) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    return LocalDate.parse(data, formatter);
  }

  private boolean isSalario(String salario) {
    return salario.matches("\\d+[\\.,]?\\d*");
  }
}
