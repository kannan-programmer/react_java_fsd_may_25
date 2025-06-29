package com.springboot.assetsphere;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;



@SpringBootApplication
@EnableTransactionManagement
public class AssetSphereApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssetSphereApplication.class, args);
	}

}
