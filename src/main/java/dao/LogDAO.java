package dao;

import connection.ConnectionFactory;
import model.Bill;

import java.util.logging.Level;

public class LogDAO extends AbstractDAO<Bill>{
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

    @Override
    public String getTableName() {
        return "logs";
    }
}
