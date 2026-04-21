package sistema.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import sistema.app.menu.CustomLogger;
import sistema.model.FolhaPagamento;
// Classe responsável por acessar e manipular dados da tabela folha_pagamento no banco
public class FolhaPagamentoDAO implements CriacaoTabela {
  // Logger personalizado para registrar mensagens (erro, sucesso, aviso)
  CustomLogger customLogger = new CustomLogger();
  // Objeto responsável pela conexão com o banco
  private final ConexaoDB conexao;
  // Construtor que recebe a conexão
  public FolhaPagamentoDAO(ConexaoDB conexao) {
    this.conexao = conexao;
  }

  // Sobrescreve método da interface CriacaoTabela
  @Override
  // Método que cria a tabela no banco
  public final void criarTabela() { // -> Comando para executar a criação da tabela

    // Comando SQL para criar a tabela folha_pagamento se ela não existir
    String comandoSQL = "CREATE TABLE IF NOT EXISTS folha_pagamento ( "
        + " codigo SERIAL PRIMARY KEY,"
        + " data_pagamento DATE NOT NULL,"
        + " desconto_inss NUMERIC(15,2),"
        + " desconto_ir NUMERIC(15,2),"
        + " salario_liquido NUMERIC(15,2),"
        + " id_funcionario INT REFERENCES funcionario(id_funcionario) NOT NULL"
        + " );";

    try (
        // Abre conexão
        Connection con = conexao.conectarDB();
        // Prepara SQL
        PreparedStatement stmt = con.prepareStatement(comandoSQL);) {
      // Executa criação da tabela
      stmt.execute();

    } catch (Exception error) {
      customLogger.logError("Erro ao inicializar tabela Folha_pagamento: ");
      throw new RuntimeException(error.getMessage(),
          error);
    }
  }

  // INSET INTO
  // Método para inserir uma nova folha de pagamento
  public void salvarFolha(FolhaPagamento folhaPagamento) {
    // Comando SQL de inserção com parâmetros
    String comandoSQL = "INSERT INTO folha_pagamento (data_pagamento, desconto_inss, desconto_ir, salario_liquido, id_funcionario) VALUES (?, ?, ?, ?, ?);";

    try (
        Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL);) {
      // Define os valores dos parâmetros
      stmt.setObject(1, folhaPagamento.getDataPagamento());
      // -> checar com o professor se o melhor é setObject ou setDate
      stmt.setObject(2, folhaPagamento.getDescontoInss());
      stmt.setObject(3, folhaPagamento.getDescontoIR());
      stmt.setObject(4, folhaPagamento.getSalarioLiquido());
      stmt.setObject(5, folhaPagamento.getFuncionario().getId_funcionario());
      // Executa inserção
      stmt.executeUpdate();
      // Log de sucesso
      customLogger
          .logFolhaSucess(
              "Folha de pagamento do(a) funcionário(a) '" + folhaPagamento.getFuncionario().getId_funcionario()
                  + " " + folhaPagamento.getFuncionario().getNome() + "' Registrado!");

    } catch (Exception error) {
      // Log de erro
      customLogger.logError("Erro na inserção da folha de pagamento do(a) funcionário(a) '"
          + folhaPagamento.getFuncionario().getId_funcionario() + " " + folhaPagamento.getFuncionario().getNome()
          + "' !");
      throw new RuntimeException("Erro: " + error.getMessage());

    }
  }

  // UPDATE
  // Método para atualizar dados da folha de pagamento
  public void atualizarFolha(FolhaPagamento folhaPagamento, int opcao) {
    String parteSQL;

    // Define qual campo será atualizado
    switch (opcao) {
      case 1 -> parteSQL = " data_pagamento = ? ";
      case 2 -> parteSQL = " desconto_inss = ? ";
      case 3 -> parteSQL = " desconto_ir = ? ";
      case 4 -> parteSQL = " salario_liquido = ?";
      case 5 -> parteSQL = " id_funcionario = ?";

      default -> throw new AssertionError("Opção inválida! -> parteSQL");
    }
    // Monta SQL de UPDATE
    String comandoSQL = "UPDATE folha_pagamento SET" + parteSQL + " WHERE codigo = ?";

    try (
        Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL);) {

      // Define valor do campo conforme opção
      switch (opcao) {
        case 1 -> stmt.setObject(1, folhaPagamento.getDataPagamento());
        case 2 -> stmt.setObject(1, folhaPagamento.getDescontoInss());
        case 3 -> stmt.setObject(1, folhaPagamento.getDescontoIR());
        case 4 -> stmt.setObject(1, folhaPagamento.getSalarioLiquido());
        case 5 -> stmt.setObject(1, folhaPagamento.getFuncionario().getId_funcionario());

        default -> throw new AssertionError("Opção inválida! -> stmt");
      }
      // Log de sucesso
      customLogger.logFolhaSucess(
          "Folha de Pagamento do(a) funcionário(a) '" + folhaPagamento.getFuncionario().getId_funcionario() + " "
              + folhaPagamento.getFuncionario().getNome() + "' atualizado!");
      // Executa atualização
      stmt.executeUpdate();

    } catch (SQLException error) {
      customLogger.logError("Erro ao atualizar:");
      throw new RuntimeException(error.getMessage());
    }
  }

  // SELECT
  // Método para buscar dados da folha de pagamento
  public void selecionarFolha(FolhaPagamento folhaPagamento, int opcao, String condicao) {

    String comandoSQL;
    // Se opcao = 0, busca tudo
    if (opcao == 0) {
      comandoSQL = "SELECT * FROM folha_pagamento ORDER BY codigo DESC";
    } else {
      String parametroSQL;
      // Define campo de filtro
      switch (opcao) {
        case 0 -> parametroSQL = "";
        case 1 -> parametroSQL = "codigo";
        case 2 -> parametroSQL = "data_pagamento";
        case 3 -> parametroSQL = "id_funcionario";
        default -> throw new AssertionError("Opção inválida! -> opcaoSQL");
      }
      comandoSQL = "SELECT * FROM folha_pagamento WHERE " + parametroSQL + " " + condicao + " ? ORDER BY codigo DESC;";
      /* selecionaFolha ( folhaPagamento, 1, "") */
    }

    try (
        Connection con = conexao.conectarDB();
        PreparedStatement stmt = con.prepareStatement(comandoSQL);) {

      if (opcao != 0) {
        switch (opcao) {
          case 1 -> stmt.setObject(1, folhaPagamento.getCodigo());
          case 2 -> stmt.setObject(1, folhaPagamento.getDataPagamento());
          case 3 -> stmt.setObject(1, folhaPagamento.getFuncionario().getId_funcionario());
        }
      }
      // Executa consulta
      try (ResultSet resultado = stmt.executeQuery()) {
        if (resultado != null) {
          // Cabeçalho da tabela
          System.out
              .println("---------------------------------------------------------------------------------------------");
          String formato = "| %-7s | %-14s | %-13s | %-11s | %-15s | %-14s |%n";
          System.out.printf(formato, "CÓDIGO", "DATA PAGAMENTO", "DESC. INSS", "DESC. IR", "SAL. LÍQUIDO", "ID FUNC.");

          System.out
              .println("---------------------------------------------------------------------------------------------");
          // Percorre resultados
          while (resultado.next()) {

            System.out.printf("| %-7d | %-14s | R$ %-10.2f | R$ %-8.2f | R$ %-12.2f | %-14d |%n",
                resultado.getInt("codigo"),
                resultado.getDate("data_pagamento"),
                resultado.getDouble("desconto_inss"),
                resultado.getDouble("desconto_ir"),
                resultado.getDouble("salario_liquido"),
                resultado.getInt("id_funcionario"));
          }
          System.out
              .println("---------------------------------------------------------------------------------------------");
        } else {
          // Caso não tenha dados
          customLogger.logWarning("Tabela FOLHA PAGAMENTO vazio!");
        }

      }
    } catch (Exception error) {
      customLogger.logError("Erro ao buscar dados da Folha de pagamento!");
      throw new RuntimeException(error.getMessage());
    }
  }
}
