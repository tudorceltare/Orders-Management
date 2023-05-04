package connection;


import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ConnectionFactory class is used to create a connection to the database. It is a singleton class.
 */
public class ConnectionFactory {
    public static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
    public static final String DRIVER = "org.postgresql.Driver";
    public static final String URL = "jdbc:postgresql://localhost:5432/pt-database";
    public static final String USER = "postgres";
    public static final String PASS = "postgres";

    private static ConnectionFactory singleInstance = new ConnectionFactory();

    private ConnectionFactory() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING, "ERROR: Unable to connect to database.");
        }
        return connection;
    }

    public static Connection getConnection() {
        return singleInstance.createConnection();
    }

    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                LOGGER.log(Level.WARNING, "ERROR: Unable to close connection.");
            }
        }
    }

    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                LOGGER.log(Level.WARNING, "ERROR: Unable to close statement.");
            }
        }
    }

    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
                LOGGER.log(Level.WARNING, "ERROR: Unable to close result set.");
            }
        }
    }
}
