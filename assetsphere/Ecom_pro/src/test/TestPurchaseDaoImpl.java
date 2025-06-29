package test;

import com.java.enums.Coupon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestPurchaseDaoImpl {
    private double calculateFinalPrice(double originalPrice, double discountPercent) {
        return originalPrice - (originalPrice * discountPercent / 100.0);
    }

    @Test
    void testCouponEnumValues() {
        assertEquals(10, Coupon.SUPER_OFFER.getDiscountPercentage());
        assertEquals(20, Coupon.SUMMER_SALE.getDiscountPercentage());
        assertEquals(30, Coupon.FESTIVE_DEAL.getDiscountPercentage());
    }

    @Test
    void testValidCouponParsing() {
        assertEquals(Coupon.SUPER_OFFER, Coupon.valueOf("SUPER_OFFER"));
        assertEquals(Coupon.SUMMER_SALE, Coupon.valueOf("SUMMER_SALE"));
        assertEquals(Coupon.FESTIVE_DEAL, Coupon.valueOf("FESTIVE_DEAL"));
    }

    @Test
    void testInvalidCouponParsingThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Coupon.valueOf("INVALID");
        });
    }
    
    @Test
    void testFinalPriceWithSUPER_OFFERCoupon() {
        double originalPrice = 1000.0;
        double expected = 900.0;
        double actual = calculateFinalPrice(originalPrice, Coupon.SUPER_OFFER.getDiscountPercentage());
        assertEquals(expected, actual);
    }

    @Test
    void testFinalPriceWitSUMMER_SALECoupon() {
        double originalPrice = 2000.0;
        double expected = 1600.0;
        double actual = calculateFinalPrice(originalPrice, Coupon.SUMMER_SALE.getDiscountPercentage());
        assertEquals(expected, actual);
    }

   

    @Test
    void testFinalPriceWithoutCoupon() {
        double originalPrice = 800.0;
        double expected = 800.0;
        double actual = calculateFinalPrice(originalPrice, 0.0);
        assertEquals(expected, actual);
    }
}
