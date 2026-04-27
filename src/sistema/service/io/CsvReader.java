package sistema.service.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import static sistema.app.ui.InputHelper.confirmar;
import static sistema.app.ui.Terminal.titulo;
import static sistema.app.util.CustomLogger.logError;
import static sistema.app.util.CustomLogger.logSucess;
import static sistema.app.util.CustomLogger.logWarning;
import sistema.model.Dependente;
import sistema.model.FolhaPagamento;
import sistema.model.Funcionario;
import sistema.repository.dao.DependenteDAO;
import sistema.repository.dao.FolhaPagamentoDAO;
import sistema.repository.dao.FuncionarioDAO;

public class CsvReader {

  private static final DateTimeFormatter frtmData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  private final FuncionarioDAO funcionarioDAO;
  private final DependenteDAO dependenteDAO;
  private final FolhaPagamentoDAO folhaPagamentoDAO;

  public CsvReader(FuncionarioDAO funcionarioDAO, DependenteDAO dependenteDAO, FolhaPagamentoDAO folhaPagamentoDAO) {
    this.funcionarioDAO = funcionarioDAO;
    this.dependenteDAO = dependenteDAO;
    this.folhaPagamentoDAO = folhaPagamentoDAO;
  }

  public ImportacaoResultado lerArquivo(String caminho) {

    ImportacaoResultado resultado = new ImportacaoResultado();
    List<Funcionario> listaFuncionarios = new ArrayList<>();

    try (
        BufferedReader br = new BufferedReader(
            new InputStreamReader(new FileInputStream(caminho), StandardCharsets.UTF_8))) {

      String linha;
      Funcionario funcionarioAtual = null;
      boolean proximaLinhaFuncionario = true;

      while ((linha = br.readLine()) != null) {

        linha = linha.trim();

        if (linha.isBlank()) {

          funcionarioAtual = null;
          proximaLinhaFuncionario = true;
          continue;

        }

        String[] dados = linha.split(";");

        if (proximaLinhaFuncionario) {

          funcionarioAtual = processarFuncionario(dados, listaFuncionarios, resultado);
          proximaLinhaFuncionario = false;

        } else {

          if (funcionarioAtual != null) {
            processarDependente(dados, funcionarioAtual, resultado);
          }
        }
      }
      logSucess("Importação concluida: Funcionários, Dependentes e Folha de Pagamentos registrados!");

      exibirResumo(resultado);

    } catch (Exception error) {
      throw new RuntimeException("Erro crítico ao ler CSV" + error.getMessage(), error);
    }

    return resultado;
  }

  /*
   * TODO - Cogitar
   * a possíbilidade de criar uma classe a parte só para fazer o processamento dos
   * dados após a leitura
   */

  /* Processa uma linha de dependente e vincula ao funcionário atual. */
  private Funcionario processarFuncionario(String[] dados, List<Funcionario> lista, ImportacaoResultado resultado) {

    String cpf = dados[1];
    Funcionario existente = funcionarioDAO.buscarPorCpf(cpf);

    if (existente != null) {
      resultado.funcionariosDuplicados++;
      resultado.funcionariosDuplicadosLista.add(cpf);

      logWarning("Funcionário '" + existente.getIdFuncionario() + " - " + existente.getNome() + " | CPF: "
          + cpf + "' Já cadastrado.");
      return existente;
    }

    Funcionario f = new Funcionario();
    f.setNome(dados[0]);
    f.setCpf(dados[1]);
    f.setDataNascimento(formatarData(dados[2])); // converte tipo String para Data
    f.setSalarioBruto(Double.parseDouble(dados[3]));

    try {
      funcionarioDAO.salvar(f);
      resultado.funcionarioImportados++;
      // Gerando a folha automaticamente após cadastrar o funcionário;
      lista.add(processarFolhaPagamento(f));

    } catch (Exception error) {
      logError("Erro ao salvar funcionário '" + f.getNome() + "': " + error.getMessage());
    }

    return f;
  }

  /* Processa uma linha de dependente e vincula ao funcionário atual. */
  private Dependente processarDependente(String[] dados, Funcionario funcionarioAtual, ImportacaoResultado resultado) {

    String cpf = dados[1];

    Dependente existente = null;

    if (existente != null) {
      resultado.dependentesDuplicados++;
      resultado.dependentesDuplicadosLista.add(cpf);

      logWarning("Dependente duplicado ignorado: CPF " + cpf);

      return existente;
    }

    Dependente d = new Dependente();

    d.setNome(dados[0]);
    d.setCpf(cpf);
    d.setDataNascimento(formatarData(dados[2]));
    d.escolherParentesco(dados[3]);// lê como String: "FILHO", "SOBRINHO" ou "OUTROS"

    d.setIdFuncionario(funcionarioAtual.getIdFuncionario());

    try {
      dependenteDAO.salvar(d);
      resultado.dependentesImportados++;
      funcionarioAtual.getDependentes().add(d);

    } catch (Exception error) {
      resultado.dependentesDuplicados++;
      resultado.dependentesDuplicadosLista.add(cpf);

      logWarning("Dependente duplicado ignorado: CPF " + cpf);
    }

    return d;
  }

  /* Processa a folha de pagamento referente ao funcionário atual. */
  private Funcionario processarFolhaPagamento(Funcionario funcionario) {
    FolhaPagamento folha = new FolhaPagamento();
    folha.setFuncionario(funcionario);
    folha.setDataPagamento(LocalDate.now());
    folha.calcularINSS();
    folha.calcularIR();
    folha.calcularSalarioLiquido();
    folhaPagamentoDAO.salvar(folha);

    return funcionario;
  }

  /* Método de conversão do tipo texto para Data */
  private LocalDate formatarData(String data) {
    return LocalDate.parse(data.trim(), frtmData);
  }

  private void exibirResumo(ImportacaoResultado resultado) {
    logSucess("Importação conclúida: Funcionários, Dependentes e Folha de Pagamentos registrados!");

    titulo("RESUMO DA IMPORTAÇÃO");

    System.out.println("Qtd. Funcionário importados: " + resultado.funcionarioImportados);
    System.out.println("Qtd. Dependentes importados: " + resultado.dependentesImportados);

    if (resultado.funcionariosDuplicados > 0 || resultado.dependentesDuplicados > 0) {
      System.out.println("\n Deseja visualizar os duplicados? (S/N): ");

      // executa o método de confirmação dentro da comparação
      if (confirmar()) {
        titulo("Funcionários duplicados");
        resultado.funcionariosDuplicadosLista.stream().forEach(funcionario -> System.out.println(funcionario));

        titulo("Dependentes duplicados");
        resultado.dependentesDuplicadosLista.stream().forEach(dependentes -> System.out.println(dependentes));
      }
    }

  }
}