package service;

import dao.OrderDAO;
import model.Order;

import java.util.List;

public class OrderService {
    private OrderDAO orderDAO;
    public OrderService() {
        this.orderDAO = new OrderDAO();
    }

    public List<Order> findAll() {
        try {
            return orderDAO.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Order findById(int id) {
        try {
            return orderDAO.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void create(Order order) {
        try {
            orderDAO.create(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateById(Order order) {
        try {
            orderDAO.updateById(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteById(int id) {
        try {
            orderDAO.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
