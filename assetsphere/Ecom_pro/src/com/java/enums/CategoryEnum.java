package com.java.enums;

public enum CategoryEnum {
    ELECTRONICS(1, "Electronics"),
    FASHION(2, "Fashion"),
    GROCERY(3, "Grocery"),
    BOOKS(4, "Books"),
	KITCHEN(5, "KITCHEN"),
	STATIONARY(6,"Stationary"),
	GROOMING(7,"Grooming");

    private int id;
    private String name;

    CategoryEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

}
