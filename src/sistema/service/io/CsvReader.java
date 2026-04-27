package sistema.service.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import sistema.app.util.CustomLogger;
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

  public List<Funcionario> lerArquivo(String caminho) {

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

          funcionarioAtual = processarFuncionario(dados, listaFuncionarios);
          proximaLinhaFuncionario = false;

        } else {

          if (funcionarioAtual != null) {
            processarDependente(dados, funcionarioAtual);
          }
        }
      }
      CustomLogger.logSucess("Importação conclúda: Funcionários, Dependentes e Folha de Pagamentos registrados!");
    } catch (Exception error) {
      throw new RuntimeException("Erro crítico ao ler CSV" + error.getMessage(), error);
    }

    return listaFuncionarios;
  }

  private Funcionario processarFuncionario(String[] dados, List<Funcionario> lista) {

    String cpf = dados[1];
    Funcionario existente = funcionarioDAO.buscarPorCpf(cpf);

    if (existente != null) {
      CustomLogger.logWarning("Funcionário '" + existente.getIdFuncionario() + " - " + existente.getNome() + " | CPF: "
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
      // Gerando a folha automaticamente após cadastrar o funcionário;
      lista.add(processarFolhaPagamento(f));

    } catch (Exception error) {
      CustomLogger.logError("Erro ao salvar funcionário '" + f.getNome() + "': " + error.getMessage());
    }

    return f;
  }

  /* Processa uma linha de dependente e vincula ao funcionário atual. */
  private void processarDependente(String[] dados, Funcionario funcionarioAtual) {

    Dependente d = new Dependente();

    d.setNome(dados[0]);
    d.setCpf(dados[1]);
    d.setDataNascimento(formatarData(dados[2]));
    d.escolherParentesco(dados[3]);// lê como String: "FILHO", "SOBRINHO" ou "OUTROS"

    d.setIdFuncionario(funcionarioAtual.getIdFuncionario());

    try {
      dependenteDAO.salvar(d);
      funcionarioAtual.getDependentes().add(d);
    } catch (Exception error) {
      CustomLogger.logWarning("Dependente '" + dados[0] + "' já cadastrado ou erro: " + error.getMessage());
    }
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
}