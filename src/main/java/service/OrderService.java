package service;

import dao.OrderDAO;
import model.Order;
import model.Product;

import java.util.List;

public class OrderService {
    private OrderDAO orderDAO;
    private ProductService productService;
    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.productService = new ProductService();
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
        // decrement quantity of products from stock
        try {
            this.checkIfOrderIsValid(order);
            for (Product product : order.getProducts()) {
                Product productFromStock = productService.findById(product.getId());
                productFromStock.setQuantity(productFromStock.getQuantity() - product.getQuantity());
                productService.updateById(productFromStock);
            }
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

    public boolean checkIfValidQuantityOfProduct(Product selectedProduct, int quantity) {
        Product product = productService.findById(selectedProduct.getId());
        if (product.getQuantity() == 0 || quantity <= 0) {
            return false;
        }
        return product.getQuantity() >= quantity;
    }

    public boolean checkIfNewOrderAlreadyContainsSelectedProduct(Order order, Product selectedProduct) {
        for (Product product : order.getProducts()) {
            if (product.getId() == selectedProduct.getId()) {
                return true;
            }
        }
        return false;
    }

    private void checkIfOrderIsValid(Order order) throws IllegalArgumentException {
        if (order.getClient() == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }
        if (order.getProducts() == null || order.getProducts().size() == 0) {
            throw new IllegalArgumentException("Products cannot be empty");
        }
        if (order.getTotalPrice() < 0) {
            throw new IllegalArgumentException("Total price cannot be negative");
        }
    }
}
