package service;

import dao.ClientDAO;
import model.Client;

import java.util.List;

public class ClientService {
    private ClientDAO clientDAO;

    public ClientService() {
        this.clientDAO = new ClientDAO();
    }

    public List<Client> findAll() {
        try {
            return clientDAO.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Client findById(int id) {
        try {
            return clientDAO.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void create(Client client) throws IllegalArgumentException {
        checkIfClientValid(client);
        try {
            clientDAO.create(client);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateById(Client client) throws IllegalArgumentException {
        checkIfClientValid(client);
        try {
            clientDAO.updateById(client);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkIfClientValid(Client client) throws IllegalArgumentException {
        if (client.getName() == null || client.getName().equals("")) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        if (client.getEmail() == null || client.getEmail().equals("")) {
            throw new IllegalArgumentException("Email cannot be empty");
        } else if (!client.getEmail().matches(regexPattern)) {
            throw new IllegalArgumentException("Email is not valid");
        }
    }

    public void deleteById(int id) {
        try {
            clientDAO.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
