package com.java.Dao;

import java.util.List;

import com.java.model.Purchase;

public interface PurchaseDao {
	void addPurchase(Purchase purchase, String couponCode);
    Purchase getPurchaseById(int id);
    List<Purchase> getAllPurchases();
    String deletePurchase(int id);
}
