package scripts_backup;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LogicaCSV {

        public static void main(String[] args) throws Exception {

                Connection conn = DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/seu_banco", "usuario", "senha");

                Statement stmt = conn.createStatement();

                String sql = "SELECT f.id, f.nome, d.nome AS dependente_nome, d.parentesco " +
                                "FROM funcionario f " +
                                "LEFT JOIN dependente d ON f.id = d.funcionario_id";

                ResultSet rs = stmt.executeQuery(sql);

                FileWriter writer = new FileWriter("funcionarios_dependentes.csv");

                writer.write("id,nome,dependente_nome,parentesco\n");

                while (rs.next()) {
                        writer.write(
                                        rs.getInt("id") + "," +
                                                        rs.getString("nome") + "," +
                                                        rs.getString("dependente_nome") + "," +
                                                        rs.getString("parentesco") + "\n");
                }

                writer.close();
                conn.close();

                System.out.println("CSV gerado com sucesso!");
        }
}
