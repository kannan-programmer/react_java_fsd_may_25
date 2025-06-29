package com.java.controller;

import com.java.exception.InvaliIdException;
import com.java.exception.InvalidCategory;
import com.java.exception.InvalidCouponException;
import com.java.service.CategoryService;
import com.java.service.CustomerService;
import com.java.service.ProductService;
import com.java.service.PurchaseService;

import java.sql.SQLException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, InvaliIdException, InvalidCouponException {
        try (Scanner sc = new Scanner(System.in)) {
			CustomerService customerService = new CustomerService();
			ProductService productservice = new ProductService();
			PurchaseService service = new PurchaseService();
			CategoryService Categoryservice = new CategoryService();
			int choice;

			do {
			    System.out.println("********************WELCOME TO ECOM****************");
			    System.out.println("1. Add Customer");
			    System.out.println("2. View All Customers");
			    System.out.println("3. Get Customer By ID");
			    System.out.println("4. Add Product");
			    System.out.println("5. View All Products");
			    System.out.println("6. Filter Products by Category");
			    System.out.println("7. View Product by ID");
			    System.out.println("8. Delete Product by ID");
			    System.out.println("9. View All Categories");
			    System.out.println("10. Get Category By ID");
			    System.out.println("11. Delete Category");
			    System.out.println("12. Add Purchase");
			    System.out.println("13. View All Purchases");
			    System.out.println("14. Get Purchase by ID");
			    System.out.println("15. Delete Purchase");
			    System.out.println("0. Exit");
			    System.out.print("Enter choice: ");
			    choice = sc.nextInt();

			    switch (choice) {
			    case 1:
			    	customerService.addCustomer();
			        break;
			    case 2:
			    	customerService.viewAllCustomers();
			        break;
			    case 3:
			    	customerService.getCustomerById();
			        break;
			    case 4:
			    	productservice.addProduct();
			        break;
			    case 5:
			    	productservice.getAllProducts();
			        break;
			    case 6:
			    	try {
						productservice.getProductsByCategory();
					} catch (InvalidCategory e) {
						e.printStackTrace();
					} catch (InvaliIdException e) {
						e.printStackTrace();
					}
			        break;
			    case 7:
			    	productservice.getProductById();
			        break;
			    case 8:
			    	productservice.deleteProductById();
			            break;
			       
			    case 9:
			    	try {
						Categoryservice.viewAllCategories();
					} catch (ClassNotFoundException | InvalidCategory e) {
						System.out.println(e.getMessage());
					}
			        break;
			    case 10:
			    	Categoryservice.getCategoryById();
			        break;
			    case 11:
			    	Categoryservice.deleteCategory();
			    case 12:
			            service.addNewPurchase();
			            break;
			            
			        case 13:
			            service.viewAllPurchases();
			            break;
			        case 14:
			            service.getPurchaseById();
			            break;
			        case 15:
			            service.deletePurchase();
			            break;
			            
			        case 0:
			            System.out.println("Thank you..! \n Exiting....");
			            break;
			        default:
			            System.out.println("Invalid choice....!");
			    }
			} while (choice != 0);
		}
    }
}
