package com.java.factory;

import com.java.Dao.CategoryDao;
import com.java.DaoImpl.CategoryDaoImpl;

public class DaoFactory {
    private DaoFactory() {
    	
    }

    public static CategoryDao getCategoryDao() {
        return new CategoryDaoImpl();
    }
}
