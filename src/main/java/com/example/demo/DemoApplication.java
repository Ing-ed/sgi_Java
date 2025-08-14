package com.example.demo;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		DBDriver driver = new DBDriver();
		SpringApplication.run(DemoApplication.class, args);
		driver.ShowDatabases();
		String[] columns = {
			"id INTEGER PRIMARY KEY AUTO_INCREMENT,\n",
			"name TEXT,\n",
			"type TEXT,\n",
			"vendorID INT"
		};
		driver.CreateTable("test", "products",columns);
		String son = "{\"nombreClave\":\"valor\"}";
		JsonObject gson = JsonParser.parseString(son).getAsJsonObject();
		System.out.println("El json es "+gson);
		for(String key : gson.keySet()){
			System.out.println("clave " + key);
		}
	}
	


}
