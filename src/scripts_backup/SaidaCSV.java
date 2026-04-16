package scripts_backup;

public class SaidaCSV {

 String comandoSQL = """
        SELECT 
            f.nome,
            f.cpf,
            fp.desconto_inss,
            fp.desconto_ir,
            fp.salario_liquido
        FROM folha_pagamento fp
        JOIN funcionario f 
            ON fp.id_funcionario = f.id_funcionario
        ORDER BY fp.codigo DESC
    """;

    try(
    Connection con = conexao.conectarDB();
    PreparedStatement stmt = con.prepareStatement(comandoSQL);
    ResultSet rs = stmt.executeQuery();
    FileWriter writer = new FileWriter("folha_pagamento.txt"))
    {

        while (rs.next()) {

            writer.append(rs.getString("nome")).append(";");
            writer.append(rs.getString("cpf")).append(";");
            writer.append(String.valueOf(rs.getDouble("desconto_inss"))).append(";");
            writer.append(String.valueOf(rs.getDouble("desconto_ir"))).append(";");
            writer.append(String.valueOf(rs.getDouble("salario_liquido")));
            writer.append("\n");
        }

        System.out.println("Arquivo exportado no formato solicitado!");

    }catch(SQLException|
    IOException e)
    {
        throw new RuntimeException("Erro ao exportar: " + e.getMessage(), e);
    }
}
