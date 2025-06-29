package com.java.service;

import com.java.Dao.CustomerDao;
import com.java.DaoImpl.CustomerDaoImpl;
import com.java.exception.InvaliIdException;
import com.java.model.Customer;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class CustomerService {
    private final CustomerDao customerDao = new CustomerDaoImpl();
    private final Scanner sc = new Scanner(System.in);

    public void addCustomer() throws ClassNotFoundException, SQLException {
        System.out.print("Enter Customer Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Enter Customer City: ");
        String city = sc.nextLine().trim();

        if (name.isEmpty() || city.isEmpty()) {
            System.out.println("Name and City cannot be empty.");
            return;
        }

        Customer customer = new Customer(0, name, city);
        customerDao.insertCustomer(customer);
    }

    public void viewAllCustomers() throws ClassNotFoundException {
        List<Customer> customers = customerDao.getAllCustomer();

        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
    customers.stream()
            .sorted(Comparator.comparingInt(Customer::getId))
            .forEach(System.out::println);
}
    }

    public void getCustomerById() throws ClassNotFoundException, InvaliIdException {
        System.out.print("Enter Customer ID: ");
        int id;
        try {
            id = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
            return;
        }

        Customer customer = customerDao.getById(id);
        System.out.println(customer != null ? customer : "Customer not found with ID " + id);
    }
}
