package com.java.service;

import com.java.Dao.PurchaseDao;
import com.java.DaoImpl.PurchaseDAOImpl;
import com.java.exception.InvaliIdException;
import com.java.exception.InvalidCouponException;
import com.java.model.Customer;
import com.java.model.Product;
import com.java.model.Purchase;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class PurchaseService {
    private final PurchaseDao dao = new PurchaseDAOImpl();

    public void addNewPurchase() throws InvaliIdException, ClassNotFoundException, InvalidCouponException {
        try (Scanner sc = new Scanner(System.in)) {
			System.out.print("Enter Customer ID: ");
			int customerId = sc.nextInt();
			System.out.print("Enter Product ID: ");
			int productId = sc.nextInt();
			sc.nextLine();
			Customer customer = new Customer();
			customer.setId(customerId);
			Product product = new Product();
			product.setId(productId);
			Purchase purchase = new Purchase(0, LocalDate.now(), customer, product);
			String couponOption;
			while (true) {
			    System.out.print("Apply Coupon? (Y/N): ");
			    couponOption = sc.nextLine().trim().toUpperCase();
			    if (couponOption.equals("Y") || couponOption.equals("N")) {
			        break;
			    } else {
			        System.out.println("Invalid input. Please enter 'Y' or 'N'.");
			    }
			}
			dao.addPurchase(purchase, couponOption);
		}
    }


    public void viewAllPurchases() throws ClassNotFoundException {
        List<Purchase> purchases = dao.getAllPurchases();
        for (Purchase p : purchases) {
            System.out.println(p);
        }
    }

    public void getPurchaseById() throws ClassNotFoundException, InvaliIdException {
        try (Scanner sc = new Scanner(System.in)) {
			System.out.print("Enter Purchase ID: ");
			int id = sc.nextInt();
			Purchase p = dao.getPurchaseById(id);
			if (p != null) {
			    System.out.println(p);
			} else {
			    System.out.println("Purchase not found.");
			}
		}
    }

    public void deletePurchase() throws ClassNotFoundException, InvaliIdException {
        try (Scanner sc = new Scanner(System.in)) {
			System.out.print("Enter Purchase ID to delete: ");
			int id = sc.nextInt();
			String result = dao.deletePurchase(id);
			System.out.println(result);
		}
    }
}
