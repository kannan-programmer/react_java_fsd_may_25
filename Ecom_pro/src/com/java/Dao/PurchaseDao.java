package com.java.Dao;

import java.util.List;

import com.java.exception.*;
import com.java.model.Purchase;

public interface PurchaseDao {
	void addPurchase(Purchase purchase, String couponCode) throws ClassNotFoundException, InvalidCouponException;
    Purchase getPurchaseById(int id) throws ClassNotFoundException, InvaliIdException;
    List<Purchase> getAllPurchases() throws ClassNotFoundException;
    String deletePurchase(int id) throws ClassNotFoundException, InvaliIdException;
}
