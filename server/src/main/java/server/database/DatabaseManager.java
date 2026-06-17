package server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String URL = "jdbc:postgresql://localhost:5432/studs";

    private final String username;
    private final String password;

    private Connection connection;

    public DatabaseManager(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(URL, username, password);
        System.out.println("Connected to PostgreSQL");
    }
    public void resetWorkersTable() {
        String drop = "DROP TABLE IF EXISTS workers CASCADE";

        String create = """
        CREATE TABLE workers (
            id SERIAL PRIMARY KEY,
            name TEXT NOT NULL,
            coordinate_x INT NOT NULL,
            coordinate_y BIGINT NOT NULL,
            creation_date TIMESTAMP NOT NULL,
            salary DOUBLE PRECISION NOT NULL,
            start_date TIMESTAMP NOT NULL,
            end_date TIMESTAMP,
            status TEXT,
            person_passport_id TEXT,
            person_eye_color TEXT,
            person_hair_color TEXT,
            person_nationality TEXT,
            owner_login TEXT NOT NULL
        )
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(drop);
            stmt.execute(create);
            System.out.println("DB RESET SUCCESS");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

}