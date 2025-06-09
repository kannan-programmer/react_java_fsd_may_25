package com.java.service;

import com.java.Dao.CategoryDao;
import com.java.exception.*;
import com.java.factory.DaoFactory;
import com.java.model.Category;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class CategoryService {
    private final CategoryDao categoryDao = DaoFactory.getCategoryDao();
    private final Scanner sc = new Scanner(System.in);

    public void viewAllCategories() throws InvalidCategory, ClassNotFoundException {
        List<Category> categories = categoryDao.getAllCategories();
        if (categories.isEmpty()) {
            System.out.println("No categories found.");
        } else {
        	categories.stream()
            .sorted(Comparator.comparingInt(Category::getId))
            .forEach(System.out::println);
        }
    }

    public void getCategoryById() throws InvaliIdException {
        System.out.print("Enter category ID: ");
        int id = sc.nextInt();
        try {
            Category category = categoryDao.getCategoryById(id);
            System.out.println(category != null ? category : "Category not found");
        } catch (ClassNotFoundException e) {
            System.out.println("Invalid id. Please check the ID entered.");
        }
    }
    
    public void deleteCategory() throws InvaliIdException {
        try (Scanner sc = new Scanner(System.in)) {
			System.out.print("Enter category ID to delete: ");
			int id = sc.nextInt();
			try {
				categoryDao.deleteCategory(id);
			} catch (ClassNotFoundException e) {
				System.out.println("unable to delete the category");
			}
		}
    }
}

