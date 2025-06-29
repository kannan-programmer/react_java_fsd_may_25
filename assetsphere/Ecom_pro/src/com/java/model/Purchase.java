package com.java.model;

import java.time.LocalDate;

public class Purchase {
private int id;
private LocalDate date_of_purchase;
Customer customer;
Product product;
public Purchase() {
}
public Purchase(int id, LocalDate date_of_purchase, Customer customer, Product product) {
	super();
	this.id = id;
	this.date_of_purchase = date_of_purchase;
	this.customer = customer;
	this.product = product;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public LocalDate getDate_of_purchase() {
	return date_of_purchase;
}
public void setDate_of_purchase(LocalDate date_of_purchase) {
	this.date_of_purchase = date_of_purchase;
}
public Customer getCustomer() {
	return customer;
}
public void setCustomer(Customer customer) {
	this.customer = customer;
}
public Product getProduct() {
	return product;
}
public void setProduct(Product product) {
	this.product = product;
}
@Override
public String toString() {
	return "Purchase [id=" + id + ", date_of_purchase=" + date_of_purchase + ", customer=" + customer + ", product="
			+ product + "]";
}

}
