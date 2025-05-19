package com.java.Dao;

import java.sql.SQLException;
import java.util.List;

import com.java.exception.InvaliIdException;
import com.java.model.Customer;

public interface CustomerDao {
void insertCustomer(Customer customer) throws ClassNotFoundException, SQLException;
List<Customer> getAllCustomer() throws ClassNotFoundException;
Customer getById(int id) throws ClassNotFoundException, InvaliIdException;
}
