package com.java.Dao;

import com.java.exception.InvaliIdException;
import com.java.model.Category;
import java.util.List;

public interface CategoryDao {
    List<Category> getAllCategories() throws ClassNotFoundException;
    Category getCategoryById(int id) throws ClassNotFoundException, InvaliIdException;
    void deleteCategory(int id) throws ClassNotFoundException, InvaliIdException;
}