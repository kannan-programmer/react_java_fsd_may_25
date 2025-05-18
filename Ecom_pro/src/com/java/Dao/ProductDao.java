package com.java.Dao;

import java.util.List;

import com.java.exception.InvalidCategory;
import com.java.exception.InvaliIdException;
import com.java.model.Product;

public interface ProductDao {
	void insertProduct(Product product);
	List<Product> getAllProduct();
	Product getById(int id) throws ClassNotFoundException, InvaliIdException;
	void deleteProduct(int id) throws ClassNotFoundException, InvaliIdException;
	List<Product> getProductsByCategoryId(int categoryId) throws InvalidCategory, InvaliIdException;
}
