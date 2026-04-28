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
import static sistema.app.ui.Terminal.separador;
import static sistema.app.ui.Terminal.titulo;
import static sistema.app.util.CustomLogger.logError;
import static sistema.app.util.CustomLogger.logSucess;
import static sistema.app.util.CustomLogger.logWarning;
import sistema.exception.DependenteException;
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

        if (linha.isBlank()) { // verifica se é linha em branco
          funcionarioAtual = null; // reseta
          proximaLinhaFuncionario = true; // reseta
          continue; // continua com a lógica
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

    } catch (Exception error) {
      throw new RuntimeException("Erro crítico ao ler CSV" + error.getMessage(), error);
    }

    logSucess("Importação concluida: Funcionários, Dependentes e Folha de Pagamentos registrados!");
    exibirResumo(resultado);

    return resultado;
  }

  /*
   * TODO - Cogitar
   * a possíbilidade de criar uma classe a parte só para fazer o processamento dos
   * dados após a leitura
   */

  /* Processa uma linha de dependente e vincula ao funcionário atual. */
  private Funcionario processarFuncionario(String[] dados, List<Funcionario> lista, ImportacaoResultado resultado) {

    String cpf = dados[1].trim();

    Funcionario existente = funcionarioDAO.buscarPorCpf(cpf); // busca se o cpf do funcionário informado na tabela

    if (existente != null) { // verifica se a variavel tem algum valor
      resultado.funcionariosDuplicados++; // conta caso for
      /* adiciona ele na lista */
      resultado.funcionariosDuplicadosLista
          .add("[ID: "
              + existente.getIdFuncionario()
              + "] " + existente.getNome()
              + " | CPF: " + cpf);

      /* apresente uma mensagem dizendo que vai ignorar o cadastro (pular ele) */
      logWarning("Funcionário Já cadastrado, ignorado: " + existente.getNome() + " | CPF: " + cpf);

      return existente; // retorna o funcionário existente
    }

    Funcionario f = new Funcionario();

    try {
      f.setNome(dados[0]);
      f.setCpf(dados[1]);
      f.setDataNascimento(formatarData(dados[2])); // converte tipo String para Data
      f.setSalarioBruto(Double.parseDouble(dados[3].trim()));

      funcionarioDAO.salvar(f);

      // Gerando a folha automaticamente após cadastrar o funcionário;
      processarFolhaPagamento(f);

      resultado.funcionarioImportados++;

    } catch (IllegalArgumentException error) {
      // Erro de validação de CPF ou dados inválidos
      resultado.funcionariosDuplicados++;
      resultado.funcionariosDuplicadosLista.add(f.getNome() + " | CPF: " + cpf + " | Motivo: " + error.getMessage());

      logWarning("Funcionário não registrado por erro de validação: " + f.getNome() + " | CPF: " + cpf + " | Motivo: "
          + error.getMessage());

    } catch (Exception error) {
      resultado.funcionariosDuplicados++;
      resultado.funcionariosDuplicadosLista.add(f.getNome() + " | CPF: " + cpf + " | Motivo: " + error.getMessage());

      logError("Erro ao salvar funcionário '" + f.getNome() + "': " + error.getMessage());
    }

    return f;
  }

  /* Processa uma linha de dependente e vincula ao funcionário atual. */
  private void processarDependente(String[] dados, Funcionario funcionarioAtual, ImportacaoResultado resultado) {

    String cpf = dados[1].trim();

    Dependente existente = dependenteDAO.buscarPorCpf(cpf);

    if (existente != null) {
      resultado.dependentesDuplicados++;
      resultado.dependentesDuplicadosLista.add(
          "[ID: " + existente.getIdDependente() + "] "
              + existente.getNome()
              + " | CPF: " + cpf
              + " | Funcionário ID: " + existente.getIdFuncionario());

      logWarning("Dependente já cadastrado, ignorado: " + existente.getNome() + " | CPF: " + cpf);

      return;
    }

    Dependente d = new Dependente();

    try {
      d.setNome(dados[0]);
      d.setCpf(cpf);
      d.setDataNascimento(formatarData(dados[2]));
      d.escolherParentesco(dados[3].trim());// lê como String: "FILHO", "SOBRINHO" ou "OUTROS"
      d.setIdFuncionario(funcionarioAtual.getIdFuncionario());

      d.validar(); // valida a regra de negócio (idade <= 18)

      dependenteDAO.salvar(d);
      funcionarioAtual.getDependentes().add(d);
      resultado.dependentesImportados++;

    } catch (IllegalArgumentException error) {
      // Erro de validação de parentesco ou CPF inválido
      resultado.dependentesDuplicados++;
      resultado.dependentesDuplicadosLista.add(d.getNome() + " | CPF: " + cpf + " | Erro: " + error.getMessage());

      logWarning("Dependente não registrado por erro de validação: " + d.getNome() + " | CPF: " + cpf + " | Motivo: "
          + error.getMessage());

    } catch (DependenteException error) {
      // Erro de regra de negócio (idade > 18)
      resultado.dependentesDuplicados++;
      resultado.dependentesDuplicadosLista.add(d.getNome() + " | CPF: " + cpf + " | Erro: " + error.getMessage());

      logWarning("Dependente não registrado por regra de negócio: " + d.getNome() + " | CPF: " + cpf + " | Motivo: "
          + error.getMessage());

    } catch (Exception error) {
      resultado.dependentesDuplicados++;
      resultado.dependentesDuplicadosLista.add(d.getNome() + " | CPF: " + cpf + " | Erro: " + error.getMessage());

      logWarning("Dependente não registrado: " + d.getNome() + " | CPF: " + cpf + " | Motivo: " + error.getMessage());
    }
  }

  /* Processa a folha de pagamento referente ao funcionário atual. */
  private FolhaPagamento processarFolhaPagamento(Funcionario funcionario) {
    FolhaPagamento folha = new FolhaPagamento();
    folha.setFuncionario(funcionario);
    folha.setDataPagamento(LocalDate.now());
    folha.calcularINSS();
    folha.calcularIR();
    folha.calcularSalarioLiquido();
    folhaPagamentoDAO.salvar(folha);
    return folha;
  }

  /* Método de conversão do tipo texto para Data */
  private LocalDate formatarData(String data) {
    return LocalDate.parse(data.trim(), frtmData);
  }

  private void exibirResumo(ImportacaoResultado resultado) {
    titulo("RESUMO DA IMPORTAÇÃO");

    System.out.println("Qtd. Funcionário importados: " + resultado.funcionarioImportados);
    System.out.println("Qtd. Dependentes importados: " + resultado.dependentesImportados);

    separador();

    System.out.println("Qtd. Funcionários duplicados : " + resultado.funcionariosDuplicados);
    System.out.println("Qtd. Dependentes duplicados  : " + resultado.dependentesDuplicados);

    separador();

    if (resultado.funcionariosDuplicados > 0 || resultado.dependentesDuplicados > 0) {
      System.out.println();
      System.out.print("\n Deseja visualizar os duplicados? (S/N): ");

      String formato = "| %-25s | %-12s | %-12s | %-10s |%n";
      System.out.printf(formato, "ID", "NOME", "CPF", "NASC.", "PARENTESCO", "ID FUN.");

      // executa o método de confirmação dentro da comparação
      if (confirmar()) {
        titulo("Funcionários duplicados");
        resultado.funcionariosDuplicadosLista.stream().forEach(funcionario -> System.out.printf(formato, funcionario));

        titulo("Dependentes duplicados");
        resultado.dependentesDuplicadosLista.stream().forEach(dependentes -> System.out.println(dependentes));
      }
    }

  }
}