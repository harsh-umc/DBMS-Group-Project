package dbms_queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

/** Shared database configuration for JDBC and jOOQ. */
public final class DatabaseConnection {
    private DatabaseConnection() {}
    public static Connection open() throws SQLException {
        String host = System.getenv().getOrDefault("DB_HOST", "localhost");
        String port = System.getenv().getOrDefault("DB_PORT", "3306");
        String db = System.getenv().getOrDefault("DB_NAME", "Market_Manager");
        String user = System.getenv().getOrDefault("DB_USER", "root");
        String password = System.getenv().getOrDefault("DB_PASSWORD", "");
        String url = "jdbc:mysql://" + host + ":" + port + "/" + db
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        return DriverManager.getConnection(url, user, password);
    }
    /** Caller owns and must close the supplied Connection. */
    public static DSLContext jooq(Connection connection) {
        return DSL.using(connection, SQLDialect.MYSQL);
    }
}
