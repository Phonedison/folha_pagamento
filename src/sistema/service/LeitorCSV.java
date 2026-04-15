package sistema.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import sistema.exception.CpfDuplicado;
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

    // faz a leitura do arquivo e garante que seja lido de forma concisa
    try (BufferedReader br = new BufferedReader(
        new InputStreamReader(new FileInputStream(caminhoArquivo), StandardCharsets.UTF_8))) {

      String linha;
      Funcionario funcionarioAtual = null;

      // Passa em cada linha do arquivo
      while ((linha = br.readLine()) != null) {

        linha = linha.trim();

        if (linha.isEmpty()) {
          funcionarioAtual = null;
          continue;
        }

        // separa as linhas por ';'
        String[] dados = linha.split(";");

        // verifica se a linha que está possui 4 colunas
        if (dados.length == 4) {
          // se possuir, preenche nomem cpf, data e salário
          funcionarioAtual = new Funcionario();
          funcionarioAtual.setNome(dados[0]);
          funcionarioAtual.setCpf(dados[1]);
          funcionarioAtual.setDataNacimento(formatarData(dados[2]));
          funcionarioAtual.setSalarioBruto(Double.parseDouble(dados[3]));

          try {
            // força o comando sql para inserir a lista dos funcionários
            funcionarioDAO.salvarFuncionario(funcionarioAtual);

          } catch (Exception error) {
            System.out.println("Error" + error.getMessage());
            throw new CpfDuplicado("Cpf de funcionário já cadastrado: " + dados[1]);
          }

          funcionarios.add(funcionarioAtual);
        }

        // forma de adicionar o dependente do funcionario listado acima
        else if (dados.length == 4 && funcionarioAtual != null) {
          Dependente dependente = new Dependente();
          dependente.setNome((dados[0]));
          dependente.setCpf(dados[1]);
          dependente.setDataNacimento(formatarData(dados[2]));
          dependente.escolherParentesco(mapearParentesco(dados[3]));
          dependente.setFuncionario(funcionarioAtual.getId_funcionario());

          try {
            dependenteDAO.salvarDependente(dependente);
            funcionarioAtual.getDependentes().add(dependente);
          } catch (Exception error) {
            System.out.println("Error" + error.getMessage());
            throw new CpfDuplicado("CPF de dependente duplicado: " + dados[1]);
          }
        }
      }

    } catch (Exception erou) {
      throw new RuntimeException("Erro ao ler CSV: " + erou.getMessage(), erou);
    }

    return funcionarios;
  }

  // checar se insere numero ou texto (acho que é texto)
  private int mapearParentesco(String valor) {
    return switch (valor.toUpperCase()) {
      case "FILHO" -> 1;
      case "SOBRINHO" -> 2;
      default -> 3;
    };
  }

  // transforma data String em LocalDate
  private LocalDate formatarData(String data) {
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    return LocalDate.parse(data, fmt);
  }

}
