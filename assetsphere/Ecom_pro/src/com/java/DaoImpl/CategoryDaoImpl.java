package com.java.DaoImpl;

import com.java.Dao.CategoryDao;
import com.java.exception.InvaliIdException;
import com.java.model.Category;
import com.java.utility.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDaoImpl implements CategoryDao {

    @Override
    public List<Category> getAllCategories() throws ClassNotFoundException {
        List<Category> list = new ArrayList<>();
        try (Connection con = DbUtil.getInstance().getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM category")) {
            while (rs.next()) {
                list.add(new Category(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Category getCategoryById(int id) throws ClassNotFoundException,InvaliIdException {
        Category cat = null;
        try (Connection con = DbUtil.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement("SELECT * FROM category WHERE id = ?")) {

            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                cat = new Category(rs.getInt("id"), rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cat;
    }

    @Override
    public void deleteCategory(int id) throws ClassNotFoundException,InvaliIdException {
        try (Connection con = DbUtil.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement("DELETE FROM category WHERE id = ?")) {
            pst.setInt(1, id);
            int rows = pst.executeUpdate();
            if (rows > 0) {
                System.out.println("Category deleted successfully.");
            } else {
                System.out.println("Category not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
