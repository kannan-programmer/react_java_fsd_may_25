package com.java.DaoImpl;

import com.java.Dao.ProductDao;
import com.java.exception.InvaliIdException;
import com.java.exception.InvalidCategory;
import com.java.model.Category;
import com.java.model.Product;
import com.java.utility.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {

    @Override
    public void insertProduct(Product product) throws ClassNotFoundException  {
        String sql = "INSERT INTO product (title, price, description, category_id) VALUES (?, ?, ?, ?)";
        try (Connection con = DbUtil.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, product.getTitle());
            pst.setDouble(2, product.getPrice());
            pst.setString(3, product.getDescription());
            pst.setInt(4, product.getCategory().getId());

            int rows = pst.executeUpdate();
            System.out.println(rows > 0 ? "Product inserted successfully." : "Failed to insert product.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> getAllProduct() throws ClassNotFoundException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.id, p.title, p.price, p.description, c.id AS category_id, c.name AS category_name " +
                     "FROM product p JOIN category c ON p.category_id = c.id";

        try (Connection con = DbUtil.getInstance().getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Category category = new Category(rs.getInt("category_id"), rs.getString("category_name"));
                Product p = new Product(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        category
                );
                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Product getById(int id) throws ClassNotFoundException,InvaliIdException {
        Product product = null;
        String sql = "SELECT p.id, p.title, p.price, p.description, c.id AS category_id, c.name AS category_name " +
                     "FROM product p JOIN category c ON p.category_id = c.id WHERE p.id = ?";

        try (Connection con = DbUtil.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Category category = new Category(rs.getInt("category_id"), rs.getString("category_name"));
                product = new Product(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        category
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return product;
    }

    @Override
    public void deleteProduct(int id) throws ClassNotFoundException,InvaliIdException {
        String sql = "DELETE FROM product WHERE id = ?";

        try (Connection con = DbUtil.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            int rows = pst.executeUpdate();
            System.out.println(rows > 0 ? "Product deleted." : "Product not found.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> getProductsByCategoryId(int categoryId) throws InvalidCategory,InvaliIdException  {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.id, p.title, p.price, p.description, c.id AS category_id, c.name AS category_name " +
                     "FROM product p JOIN category c ON p.category_id = c.id WHERE c.id = ?";

        try (Connection con = DbUtil.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, categoryId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Category category = new Category(rs.getInt("category_id"), rs.getString("category_name"));
                Product p = new Product(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        category
                );
                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
