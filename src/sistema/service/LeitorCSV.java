package sistema.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import sistema.app.menu.CustomLogger;
import sistema.model.*;
import sistema.repository.*;

public class LeitorCSV {
  CustomLogger customLogger = new CustomLogger();

  private final FuncionarioDAO funcionarioDAO;
  private final DependenteDAO dependenteDAO;
  private final FolhaPagamentoDAO folhaPagamentoDAO;

  public LeitorCSV(FuncionarioDAO funcionarioDAO, DependenteDAO dependenteDAO, FolhaPagamentoDAO folhaPagamentoDAO) {
    this.funcionarioDAO = funcionarioDAO;
    this.dependenteDAO = dependenteDAO;
    this.folhaPagamentoDAO = folhaPagamentoDAO;
  }

  public List<Funcionario> lerArquivo(String caminhoArquivo) {
    List<Funcionario> funcionarios = new ArrayList<>();

    boolean linhaFuncionario = true;
    // faz a leitura do arquivo e garante que seja lido de forma concisa
    try (BufferedReader br = new BufferedReader(
        new InputStreamReader(new FileInputStream(caminhoArquivo), StandardCharsets.UTF_8))) {

      String linha;
      Funcionario funcionarioAtual = null;

      // Passa em cada linha do arquivo

      while ((linha = br.readLine()) != null) { // valida se alinha atual que está é diferente de nulo

        // retorna os valores da linha sem espaço
        linha = linha.trim();

        if (linha.isBlank()) { // verifica se a linha está vazia
          funcionarioAtual = null; // objeto setado como nulo
          linhaFuncionario = true; // troca o modo de inserção para funcionario
          continue;
        }

        // separa as linhas por ';'
        String[] dados = linha.split(";");

        if (linhaFuncionario) {
          // se possuir, preenche nomem cpf, data e salário
          String cpf = dados[1];
          Funcionario funcionarioExistente = funcionarioDAO.buscarPorCpf(cpf);

          if (funcionarioExistente != null) {
            funcionarioAtual = funcionarioExistente;
            linhaFuncionario = false;
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

            if (funcionarioAtual != null) {// Caso seja apontado como vazio, ele não gera a folha de pagamento

              FolhaPagamento folha = new FolhaPagamento(); // gerando o objeto folha de pagamento

              folha.setFuncionario(funcionarioAtual); // funcionario inserido na folha
              folha.setDataPagamento(LocalDate.now()); // pegando a data Atual
              folha.calcularINSS();// calcula o INSS do funcionário
              folha.calcularIR();// calcula o IR do funcionário
              folha.calcularSalarioLiquido(); // calcula o salário liquido do funcionário

              folhaPagamentoDAO.salvarFolha(folha); // Salva a folha do funcionário na tabela folha_pagamento
            }

            funcionarios.add(funcionarioAtual); // adiciona o funcionário na tabela funcionario no DB

          } catch (Exception error) {
            System.out.println("Error ao salvar funcionário: " + error.getMessage());
            continue;
          }

          linhaFuncionario = false;

        } else { // forma de adicionar o dependente do funcionario listado acima

          if (funcionarioAtual == null)
            continue;

          Dependente dependente = new Dependente();
          dependente.setNome((dados[0]));
          dependente.setCpf(dados[1]);
          dependente.setDataNacimento(formatarData(dados[2])); // seta o valor do tipo String como Data
          dependente.escolherParentesco((dados[3])); // String
          // System.out.println("Erro aqui mano o/ " + dados[3]);
          dependente.setFuncionario(funcionarioAtual.getId_funcionario());

          try {
            dependenteDAO.salvarDependente(dependente);
            funcionarioAtual.getDependentes().add(dependente);

          } catch (Exception error) {
            customLogger
                .logError("Aviso: Dependente '" + dados[0] + "' já cadastrado!");

          }
        }
      }

      System.out.print("");
      System.out.print("Funcionários e Dependentes registrados!");
      System.out.print("");

    } catch (Exception erou) {
      throw new RuntimeException("Erro cirítico ao ler CSV: " + erou.getMessage(), erou);
    }

    return funcionarios;
  }

  // checar se insere numero ou texto (acho que é texto)
  // private int mapearParentesco(String valor) {
  // return switch (valor.toUpperCase()) {
  // case "FILHO" -> 1;
  // case "SOBRINHO" -> 2;
  // case "OUTROS" -> 3;
  // default -> 0;
  // };
  // }

  // transforma data String em LocalDate
  private LocalDate formatarData(String data) {
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // "21/10/2004"
    return LocalDate.parse(data, fmt);
  }

}
