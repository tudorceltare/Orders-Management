package dao;

import connection.ConnectionFactory;
import model.Client;
import model.Order;
import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * OrderDAO class extends AbstractDAO class and has access to all its methods.
 * All methods are overridden because the Orders table has a many-to-many relationship with the Products table.
 */
public class OrderDAO extends AbstractDAO<Order> {

    private ClientDAO clientDAO;
    private ProductDAO productDAO;

    public OrderDAO() {
        clientDAO = new ClientDAO();
        productDAO = new ProductDAO();
    }

    @Override
    public void create(Order order) {
        String sql1 = "INSERT INTO " + getTableName() + " (client_id, total_price) VALUES (?, ?)";
        String sql2 = "INSERT INTO order_products (order_id, product_id, quantity) VALUES (?, ?, ?)";
        try(Connection connection = ConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql1, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, order.getClient().getId());
            statement.setDouble(2, order.getTotalPrice());
            statement.executeUpdate();
            try(ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    order.setId(rs.getInt("id"));
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
            for (Product product : order.getProducts()) {
                try(PreparedStatement statement1 = connection.prepareStatement(sql2)) {
                    statement1.setInt(1, order.getId());
                    statement1.setInt(2, product.getId());
                    statement1.setInt(3, product.getQuantity());
                    statement1.executeUpdate();
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, Order.class.getName() + "DAO:create " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Executes two queries to get all the orders from the database and all the products from the database.
     * @return a list of orders
     * @throws SQLException if the connection to the database fails
     */
    @Override
    public List<Order> findAll() throws SQLException {
        String sql1 = "SELECT * FROM " + getTableName();
        String sql2 = "SELECT * FROM order_products";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement1 = connection.prepareStatement(sql1);
             PreparedStatement statement2 = connection.prepareStatement(sql2);
             ResultSet rs1 = statement1.executeQuery();
             ResultSet rs2 = statement2.executeQuery()) {
            List<Product> products = productDAO.findAll();
            List<Client> clients = clientDAO.findAll();
            return mapToEntity(rs1, rs2, products, clients);
        }
    }

    /**
     * Executes two queries to get all the orders: one to get the order with the given id and one to get all the
     * products from the database where the order_id is the given id.
     * @param id The id of the order to be found
     * @return an order with the given id
     */
    @Override
    public Order findById(int id) {
        String sql1 = "SELECT * FROM " + getTableName() + " WHERE id = ?";
        String sql2 = "SELECT * FROM order_products WHERE order_id = ?";
        try(Connection connection = ConnectionFactory.getConnection();
            PreparedStatement statement1 = connection.prepareStatement(sql1);
            PreparedStatement statement2 = connection.prepareStatement(sql2)) {
            statement1.setInt(1, id);
            statement2.setInt(1, id);
            try(ResultSet rs1 = statement1.executeQuery();
                ResultSet rs2 = statement2.executeQuery()) {
                List<Product> products = productDAO.findAll();
                List<Client> clients = clientDAO.findAll();
                return mapToEntity(rs1, rs2, products, clients).get(0);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, Order.class.getName() + "DAO:findById " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Executes two queries to update an order: one to update the order with the given id and one to update all the
     * products from the database where the order_id is the given id.
     * @param order The new order to be updated, the id is the same
     */
    @Override
    public void updateById(Order order) {
        String sql1 = "UPDATE " + getTableName() + " SET client_id = ?, total_price = ? WHERE id = ?";
        String sql2 = "UPDATE order_products SET product_id = ?, quantity = ? WHERE order_id = ?";
        try(Connection connection = ConnectionFactory.getConnection();
            PreparedStatement statement1 = connection.prepareStatement(sql1)) {
            statement1.setInt(1, order.getClient().getId());
            statement1.setDouble(2, order.getTotalPrice());
            statement1.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, Order.class.getName() + "DAO:updateById " + e.getMessage());
        }
        for (Product product : order.getProducts()) {
            try(Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement2 = connection.prepareStatement(sql2)) {
                statement2.setInt(1, product.getId());
                statement2.setInt(2, product.getQuantity());
                statement2.setInt(3, order.getId());
                statement2.executeUpdate();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, Order.class.getName() + "DAO:updateById " + e.getMessage());
            }
        }
    }

    /**
     * Executes two queries to delete an order: one to delete the order with the given id and one to delete all the
     * products from the database where the order_id is the given id. This is a cascade delete.
     * @param id The id of the order to be deleted
     */
    @Override
    public void deleteById(int id) {
        String sql1 = "DELETE FROM " + getTableName() + " WHERE id = ?";
        String sql2 = "DELETE FROM order_products WHERE order_id = ?";
        try(Connection connection = ConnectionFactory.getConnection();
            PreparedStatement statement1 = connection.prepareStatement(sql1)) {
            statement1.setInt(1, id);
            statement1.executeQuery();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, Order.class.getName() + "DAO:deleteById " + e.getMessage());
        }
        try(Connection connection = ConnectionFactory.getConnection();
            PreparedStatement statement2 = connection.prepareStatement(sql2)) {
            statement2.setInt(1, id);
            statement2.executeQuery();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, Order.class.getName() + "DAO:deleteById " + e.getMessage());
        }
    }

    /**
     * Maps the result set to a list of orders with their products and clients.
     * @param rs1 the result set of the first query, it represents the orders
     * @param rs2 the result set of the second query, it represents the products of the orders returned from the first
     *            query
     * @param products the list of products
     * @param clients the list of clients
     * @return the list of orders
     * @throws SQLException if the result set is invalid
     */
    private List<Order> mapToEntity(ResultSet rs1, ResultSet rs2, List<Product> products, List<Client> clients) throws SQLException {
        Map<Integer, Order> orders = new HashMap<>();
        Map<Integer, Product> productMap = new HashMap<>();
        for (Product product : products) {
            productMap.put(product.getId(), product);
        }
        Map<Integer, Client> clientMap = new HashMap<>();
        for (Client client : clients) {
            clientMap.put(client.getId(), client);
        }
        try {
            while (rs1.next()) {
                Order order = new Order();
                order.setId(rs1.getInt("id"));
                Client client = clientMap.get(rs1.getInt("client_id"));
                order.setClient(client);
                order.setTotalPrice(rs1.getDouble("total_price"));
                order.setProducts(new ArrayList<Product>());
                orders.put(order.getId(), order);
            }
            while (rs2.next()) {
                int orderId = rs2.getInt("order_id");
                int productId = rs2.getInt("product_id");
                int quantity = rs2.getInt("quantity");
                Product productInOrder = productMap.get(productId);
                productInOrder.setQuantity(quantity);
                orders.get(orderId).getProducts().add(productInOrder);
            }
            return new ArrayList<>(orders.values());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
