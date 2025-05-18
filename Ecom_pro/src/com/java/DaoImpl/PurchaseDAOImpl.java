package com.java.DaoImpl;

import com.java.Dao.PurchaseDao;
import com.java.enums.Coupon;
import com.java.model.*;
import com.java.utility.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PurchaseDAOImpl implements PurchaseDao {

    Scanner sc = new Scanner(System.in);

    @Override
    public void addPurchase(Purchase purchase, String couponOption) {
        try {
            DbUtil db = DbUtil.getInstance();
            Connection con = db.getConnection();
            double originalPrice = 0.0;
            double finalPrice;
            String couponCode = null;
            double discountPercent = 0;

            String productSql = "SELECT price FROM product WHERE id = ?";
            try {
				PreparedStatement productStmt = con.prepareStatement(productSql);
				productStmt.setInt(1, purchase.getProduct().getId());
				ResultSet productRs = productStmt.executeQuery();

				if (productRs.next()) {
				    originalPrice = productRs.getDouble("price");
				} else {
				    throw new IllegalArgumentException("Invalid product ID.");
				}
			} catch (IllegalArgumentException e) {
				System.out.println("Invalid Product Id:");
			}

            if (couponOption.equalsIgnoreCase("Y")) {
                System.out.print("Enter Coupon Code: ");
                couponCode = sc.nextLine().toUpperCase();

                try {
                    Coupon coupon = Coupon.valueOf(couponCode);
                    discountPercent = coupon.getDiscountPercentage();
                    finalPrice = originalPrice - (originalPrice * discountPercent / 100.0);
                    double discounprice = originalPrice - finalPrice;
                    System.out.println("Coupon applied..! Original price :"+originalPrice + "Discount price :"+discounprice + " , Final price: ₹" + finalPrice);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid coupon code.");
                }

            } else if (couponOption.equalsIgnoreCase("N")) {
                finalPrice = originalPrice;
                System.out.println("No coupon applied. Final price: ₹" + finalPrice);
            } else {
                throw new IllegalArgumentException("Invalid input. Please enter Y or N.");
            }

            String insertSql = "INSERT INTO purchase(date_of_purchase, customer_id, product_id, final_price, coupon_code, discount_percent) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = con.prepareStatement(insertSql);
            insertStmt.setString(1, purchase.getDate_of_purchase().toString());
            insertStmt.setInt(2, purchase.getCustomer().getId());
            insertStmt.setInt(3, purchase.getProduct().getId());
            insertStmt.setDouble(4, finalPrice);
            insertStmt.setString(5, couponCode);
            insertStmt.setDouble(6, discountPercent);

            int rows = insertStmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Purchase successful.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Purchase getPurchaseById(int id) {
        Purchase purchase = null;
        try {
            DbUtil db = DbUtil.getInstance();
            Connection con = db.getConnection();

            String sql = "SELECT p.id as pid, p.date_of_purchase, c.id as cid, c.name as cname, c.city, pr.id as prid, pr.title, pr.price, pr.description, cat.id as catid, cat.name as catname " +
                         "FROM purchase p " +
                         "JOIN customer c ON p.customer_id = c.id " +
                         "JOIN product pr ON p.product_id = pr.id " +
                         "JOIN category cat ON pr.category_id = cat.id " +
                         "WHERE p.id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("cid"),
                        rs.getString("cname"),
                        rs.getString("city")
                );

                Category category = new Category(
                        rs.getInt("catid"),
                        rs.getString("catname")
                );

                Product product = new Product(
                        rs.getInt("prid"),
                        rs.getString("title"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        category
                );

                purchase = new Purchase(
                        rs.getInt("pid"),
                        LocalDate.parse(rs.getString("date_of_purchase")),
                        customer,
                        product
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return purchase;
    }

    @Override
    public List<Purchase> getAllPurchases() {
        List<Purchase> list = new ArrayList<>();
        try {
            DbUtil db = DbUtil.getInstance();
            Connection con = db.getConnection();

            String sql = "SELECT p.id as pid, p.date_of_purchase, c.id as cid, c.name as cname, c.city, pr.id as prid, pr.title, pr.price, pr.description, cat.id as catid, cat.name as catname " +
                         "FROM purchase p " +
                         "JOIN customer c ON p.customer_id = c.id " +
                         "JOIN product pr ON p.product_id = pr.id " +
                         "JOIN category cat ON pr.category_id = cat.id";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("cid"),
                        rs.getString("cname"),
                        rs.getString("city")
                );

                Category category = new Category(
                        rs.getInt("catid"),
                        rs.getString("catname")
                );

                Product product = new Product(
                        rs.getInt("prid"),
                        rs.getString("title"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        category
                );

                Purchase purchase = new Purchase(
                        rs.getInt("pid"),
                        LocalDate.parse(rs.getString("date_of_purchase")),
                        customer,
                        product
                );

                list.add(purchase);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String deletePurchase(int id) {
        try {
            DbUtil db = DbUtil.getInstance();
            Connection con = db.getConnection();
            String query = "DELETE FROM purchase WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, id);
            int rows = pst.executeUpdate();
            return (rows > 0) ? "Purchase deleted." : "Purchase ID not found.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
