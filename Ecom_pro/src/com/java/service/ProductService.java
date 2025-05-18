package com.java.service;

import com.java.Dao.ProductDao;
import com.java.DaoImpl.ProductDaoImpl;
import com.java.model.Category;
import com.java.model.Product;
import com.java.enums.CategoryEnum;
import com.java.exception.InvaliIdException;
import com.java.exception.InvalidCategory;

import java.util.List;
import java.util.Scanner;

public class ProductService {
    Scanner sc = new Scanner(System.in);
    ProductDao dao = new ProductDaoImpl();

    public void addProduct() {
        System.out.print("Enter Product Title: ");
        String title = sc.nextLine();
        System.out.print("Enter Price: ");
        double price = sc.nextDouble();
        sc.nextLine(); 
        System.out.print("Enter Description: ");
        String description = sc.nextLine();
        System.out.println("Select Category:");
        for (CategoryEnum cat : CategoryEnum.values()) {
            System.out.println(cat.getId() + ". " + cat.getName());
        }
        System.out.print("Enter Category ID: ");
        int categoryId = sc.nextInt();
        sc.nextLine(); 
        CategoryEnum selectedEnum = null;
        for (CategoryEnum cat : CategoryEnum.values()) {
            if (cat.getId() == categoryId) {
                selectedEnum = cat;
                break;
            }
        }

        if (selectedEnum == null) {
            System.out.println("Invalid Category ID.");
            return;
        }

        Category category = new Category(selectedEnum.getId(), selectedEnum.getName());
        Product product = new Product(0, title, price, description, category);
        dao.insertProduct(product);
    }

    public void getAllProducts() {
        List<Product> list = dao.getAllProduct();
        if (list.isEmpty()) {
            System.out.println("No products found.");
        } else {
            list.forEach(System.out::println);
        }
    }

    public void getProductsByCategory() throws InvalidCategory, InvaliIdException {
        System.out.println("Select Category to Filter:");
        for (CategoryEnum cat : CategoryEnum.values()) {
            System.out.println(cat.getId() + ". " + cat.getName());
        }

        System.out.print("Enter Category ID: ");
        int categoryId = sc.nextInt();
        sc.nextLine(); 
        List<Product> list = dao.getProductsByCategoryId(categoryId);
        if (list.isEmpty()) {
            System.out.println("No products found in this category.");
        } else {
            list.forEach(System.out::println);
        }
    }

    public void deleteProductById() throws ClassNotFoundException, InvaliIdException {
        System.out.print("Enter Product ID to delete: ");
        int id = sc.nextInt();
        sc.nextLine();
        dao.deleteProduct(id);
    }

    public void getProductById() throws ClassNotFoundException, InvaliIdException {
        System.out.print("Enter Product ID to view: ");
        int id = sc.nextInt();
        sc.nextLine();
        Product product = dao.getById(id);
        if (product == null) {
            System.out.println("Product not found.");
        } else {
            System.out.println(product);
        }
    }
}
