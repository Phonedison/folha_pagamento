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
import sistema.repository.*;

public class LeitorCSV {
  private final FuncionarioDAO funcionarioDAO;
  private final DependenteDAO dependenteDAO;

  public LeitorCSV(FuncionarioDAO funcionarioDAO, DependenteDAO dependenteDAO) {
    this.funcionarioDAO = funcionarioDAO;
    this.dependenteDAO = dependenteDAO;
  }

  public List<Funcionario> lerArquivo(String caminhoArquivo) {
    List<Funcionario> funcionarios = new ArrayList<>();

    boolean proximaLinhaEhFuncionario = true;
    // faz a leitura do arquivo e garante que seja lido de forma concisa
    try (BufferedReader br = new BufferedReader(
        new InputStreamReader(new FileInputStream(caminhoArquivo), StandardCharsets.UTF_8))) {

      String linha;
      Funcionario funcionarioAtual = null;

      // Passa em cada linha do arquivo

      while ((linha = br.readLine()) != null) { // valida se alinha atual que está é diferente de nulo

        // retorna os valores da linha sem espaço
        linha = linha.trim();

        if (linha.isBlank()) {
          funcionarioAtual = null;
          proximaLinhaEhFuncionario = true;
          continue;
        }

        // separa as linhas por ';'
        String[] dados = linha.split(";");

        if (proximaLinhaEhFuncionario) {
          // se possuir, preenche nomem cpf, data e salário
          String cpf = dados[1];
          Funcionario funcionarioExistente = funcionarioDAO.buscarPorCpf(cpf);

          if (funcionarioExistente != null) {
            System.out.println("Pulando! Funcionário " + funcionarioExistente.getNome() + " já está no banco.");
            funcionarioAtual = funcionarioExistente;
            proximaLinhaEhFuncionario = false;
            continue;
          }

          funcionarioAtual = new Funcionario();
          funcionarioAtual.setNome(dados[0]);
          funcionarioAtual.setCpf(dados[1]);
          funcionarioAtual.setDataNacimento(formatarData(dados[2]));
          funcionarioAtual.setSalarioBruto(Double.parseDouble(dados[3]));

          try {
            // força o comando sql para inserir a lista dos funcionários
            funcionarioDAO.salvarFuncionario(funcionarioAtual);
            funcionarios.add(funcionarioAtual);

          } catch (Exception error) {
            System.out.println("Error ao salvar funcionário: " + error.getMessage());
            continue;
          }

          proximaLinhaEhFuncionario = false;

        } else { // forma de adicionar o dependente do funcionario listado acima

          if (funcionarioAtual == null)
            continue;

          Dependente dependente = new Dependente();
          dependente.setNome((dados[0]));
          dependente.setCpf(dados[1]);
          dependente.setDataNacimento(formatarData(dados[2]));
          dependente.escolherParentesco((dados[3])); // String
          System.out.println("Erro aqui mano o/ " + dados[3]);
          dependente.setFuncionario(funcionarioAtual.getId_funcionario());

          try {
            dependenteDAO.salvarDependente(dependente);
            funcionarioAtual.getDependentes().add(dependente);

          } catch (Exception error) {
            System.out.println("Aviso: Dependente " + dados[0] + " já cadastrado ou erro na inserção. Pulandoooo...");
          }
        }
      }

      System.out.print("Funcionários e Dependentes registrados!");

    } catch (Exception erou) {
      throw new RuntimeException("Erro cirítico ao ler CSV: " + erou.getMessage(), erou);
    }

    return funcionarios;
  }

  // checar se insere numero ou texto (acho que é texto)
  private int mapearParentesco(String valor) {
    return switch (valor.toUpperCase()) {
      case "FILHO" -> 1;
      case "SOBRINHO" -> 2;
      case "OUTROS" -> 3;
      default -> 0;
    };
  }

  // transforma data String em LocalDate
  private LocalDate formatarData(String data) {
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    return LocalDate.parse(data, fmt);
  }

}
