
package com.java.enums;

	public enum Coupon {
	    SUPER_OFFER(10),SUMMER_SALE(20),FESTIVE_DEAL(30),SUNDAY_SS(50);

	    private final int discountPercentage;
	    Coupon(int discountPercentage) {
	        this.discountPercentage = discountPercentage;
	    }
		public double getDiscountPercentage() {
			return discountPercentage;
		}

	    }
	


