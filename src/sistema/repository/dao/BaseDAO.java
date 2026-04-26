package sistema.repository.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import sistema.app.util.CustomLogger;
import sistema.repository.connection.DatabaseConfig;

public abstract class BaseDAO {
    protected final DatabaseConfig db;

    public BaseDAO(DatabaseConfig db) {
        this.db = db;
    }

    protected void executeUpdate(String sql, Object... parametros) {

        try (Connection conexao = db.conectarDB();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {
            for (int i = 0; i < parametros.length; i++) {
                stmt.setObject(i + 1, parametros[i]);
            }
            stmt.executeUpdate();

        } catch (SQLException error) {

            CustomLogger.logConectionError("Erro ao executar o comando SQL:");
            throw new RuntimeException(error.getMessage(), error);
        }
    }
}
