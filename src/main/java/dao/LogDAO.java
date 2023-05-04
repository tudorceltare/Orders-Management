package dao;

import connection.ConnectionFactory;
import model.Bill;

import java.util.logging.Level;

/**
 * LogDAO class extends AbstractDAO class and has access to all its methods.
 * The create method is overridden to insert a new log into the database.
 * The getTableName method is overridden to return the name of the table in the database.
 */
public class LogDAO extends AbstractDAO<Bill>{

    /**
     * Inserts a new bill into the database. The id and the timestamp are automatically generated, that is why this
     * method is overridden.
     * @param bill The bill to be inserted into the database, only the message is used
     */
    @Override
    public void create(Bill bill) {
        String sql = "INSERT INTO " + getTableName() + " (message) VALUES (?)";
        try (var connection = ConnectionFactory.getConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setString(1, bill.message());
            statement.executeUpdate();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, Bill.class.getName() + "DAO:create " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Returns the name of the table in the database. The name of the table is "logs" and that is why this method
     * is overridden.
     * @return the string "logs"
     */
    @Override
    public String getTableName() {
        return "logs";
    }
}
