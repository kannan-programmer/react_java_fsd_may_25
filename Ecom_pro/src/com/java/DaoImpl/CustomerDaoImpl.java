package com.java.DaoImpl;

import com.java.Dao.CustomerDao;
import com.java.exception.InvaliIdException;
import com.java.model.Customer;
import com.java.utility.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {

    @Override
    public void insertCustomer(Customer customer) throws ClassNotFoundException {
        String sql = "INSERT INTO customer (name, city) VALUES (?, ?)";
        try (Connection con = DbUtil.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            if (customer == null || customer.getName() == null || customer.getCity() == null) {
                System.out.println("Invalid customer data. Name and city are required.");
                return;
            }
            pst.setString(1, customer.getName());
            pst.setString(2, customer.getCity());
            int rows = pst.executeUpdate();
            if (rows > 0) {
                System.out.println("Customer inserted successfully.");
            } else {
                System.out.println("Failed to insert customer.");
            }

        } catch (SQLException e) {
            System.out.println("Error during customer insertion: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Database driver not found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    @Override
    public List<Customer> getAllCustomer() throws ClassNotFoundException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT id, name, city FROM customer";
        try (Connection con = DbUtil.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Customer c = new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("city")
                );
                customers.add(c);}
        } catch (SQLException e) {
            System.out.println("Error while fetching customers: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Database driver not found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }

        return customers;
    }

    @Override
    public Customer getById(int id) throws ClassNotFoundException,InvaliIdException {
        Customer customer = null;
        String sql = "SELECT id, name, city FROM customer WHERE id = ?";

        if (id <= 0) {
            System.out.println("Invalid customer ID.");
            return null;
        }

        try (Connection con = DbUtil.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    customer = new Customer(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("city")
                    );
                } else {
                    System.out.println("No customer found with ID: " + id);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching customer by ID: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Database driver not found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }

        return customer;
    }
}
