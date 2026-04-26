package sistema.repository.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sistema.app.util.CustomLogger;
import sistema.model.FolhaPagamento;
import sistema.repository.connection.DatabaseConfig;

public class FolhaPagamentoDAO extends BaseDAO implements GenericDAO<FolhaPagamento> {

    public FolhaPagamentoDAO(DatabaseConfig db) {
        super(db);
    }

    @Override
    public void atualizar(FolhaPagamento folha, int id, int opcao) {

        StringBuilder comandoSQL = new StringBuilder("UPDATE folha_pagamento SET ");

        List<Object> parametro = new ArrayList<>();

        switch (opcao) {
            case 0 -> {
                comandoSQL.append(
                        "data_pagamento = ?, desconto_inss = ?, desconto_ir = ?, salario_liquido = ?, id_funcionario = ?");
                parametro.add(folha.getDataPagamento());
                parametro.add(folha.getDescontoInss());
                parametro.add(folha.getDescontoIR());
                parametro.add(folha.getSalarioLiquido());
                parametro.add(folha.getFuncionario().getIdFuncionario());
            }

            case 1 -> {
                comandoSQL.append("data_pagamento = ?");
                parametro.add(folha.getDataPagamento());
            }

            case 2 -> {
                comandoSQL.append("desconto_inss = ?");
                parametro.add(folha.getDescontoInss());
            }

            case 3 -> {
                comandoSQL.append("desconto_ir = ?");
                parametro.add(folha.getDescontoIR());
            }

            case 4 -> {
                comandoSQL.append("salario_liquido = ?");
                parametro.add(folha.getSalarioLiquido());
            }

            case 5 -> {
                comandoSQL.append("id_funcionario = ?");
                parametro.add(folha.getFuncionario().getIdFuncionario());
            }

            default -> {
                CustomLogger.logWarning("Opção inválida!");
                return;
            }
        }

        comandoSQL.append("WHERE codigo = ?");
        parametro.add(id);

        try {
            executeUpdate(comandoSQL.toString(), parametro.toArray());
            CustomLogger
                    .logSucess("Folha de Pagamento do " + folha.getFuncionario().getIdFuncionario() + " - "
                            + folha.getFuncionario().getNome() + " Atualizada!");

        } catch (Exception e) {

            CustomLogger.logError("Erro ao Atualizar Folha de Pagamento: ");
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    @Override
    public void excluir(int id) {
        String comandoSQL = "DELETE FROM folha_pagamento WHERE codigo = ?";
        executeUpdate(comandoSQL, id);
    }

    @Override
    public List<FolhaPagamento> listarTodos(int opcao) {
        StringBuilder comandoSQL = new StringBuilder("SELECT * FROM folha_pagamento ORDER BY ");

        switch (opcao) {
            case 1 -> comandoSQL.append("codigo DESC;");
            case 2 -> comandoSQL.append("data_pagamento  DESC;");
            case 3 -> comandoSQL.append("desconto_ir DESC;");
            case 4 -> comandoSQL.append("desconto_inss DESC;");
            case 5 -> comandoSQL.append("salario_liquido DESC;");
            case 6 -> comandoSQL.append("id_funcionario DESC;");
            default -> {
                CustomLogger.logWarning("Opção inválida!");
                return null;
            }
        }

        List<FolhaPagamento> lista = new ArrayList<>();

        try (
                Connection conexao = db.conectarDB();
                PreparedStatement stmt = conexao.prepareStatement(comandoSQL.toString());
                ResultSet rs = stmt.executeQuery();) {

            while (rs.next()) {
                lista.add(mapearFolha(rs));
            }

        } catch (Exception error) {
            CustomLogger.logFuncionarioError("Erro ao listar Folha de Pagamento");
            throw new RuntimeException(error.getMessage(), error);
        }

        return lista;
    }

    @Override
    public void salvar(FolhaPagamento folha) {

        String comandoSQL = """
                INSERT INTO folha_pagamento (data_agamento, desconto_inss, desconto_ir, salario_liquido, id_funcionario) VALUES (?, ?, ?, ?, ?)
                """;

        executeUpdate(
                comandoSQL,
                folha.getDataPagamento(),
                folha.getDescontoInss(),
                folha.getDescontoIR(),
                folha.getSalarioLiquido(),
                folha.getFuncionario().getIdFuncionario());
    }

    private FolhaPagamento mapearFolha(ResultSet rs) throws SQLException {

        FolhaPagamento folha = new FolhaPagamento();

        folha.setCodigo(rs.getInt("codigo"));
        folha.setDataPagamento(rs.getDate("data_pagamento").toLocalDate());
        folha.setDescontoInss(rs.getDouble("desconto_inss"));
        folha.setDescontoIR(rs.getDouble("desconto_ir"));
        folha.setSalarioLiquido(rs.getDouble("salario_liquido"));
        folha.getFuncionario().setIdFuncionario(rs.getInt("id_funcionario")); // ! Possibilidade de ERROR

        return folha;
    }
}