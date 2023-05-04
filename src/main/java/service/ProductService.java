package service;

import dao.ProductDAO;
import model.Product;

import java.util.List;

/**
 * The ProductService is in the business layer of the application. It is used to communicate between the controller layer
 * and the data access layer. All the checks are made here before calling the data access layer.
 */
public class ProductService {
    private ProductDAO productDAO;
    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    public List<Product> findAll() {
        try {
            return productDAO.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Product findById(int id) {
        try {
            return productDAO.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void create(Product product) throws IllegalArgumentException {
        try {
            checkIfProductValid(product);
            productDAO.create(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateById(Product product) throws IllegalArgumentException {
        try {
            checkIfProductValid(product);
            productDAO.updateById(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkIfProductValid(Product product) throws IllegalArgumentException {
        if (product.getName() == null || product.getName().equals("")) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (product.getPrice() < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (product.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
    }

    public void deleteById(int id) {
        try {
            productDAO.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
