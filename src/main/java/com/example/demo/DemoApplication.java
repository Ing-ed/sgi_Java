package com.example.demo;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;

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
		String son = "{\"table\":\"products\",\"fields\":[{\"name\":\"producto\",\"type\":\"pintura\",\"vendorID\":1},{\"name\":\"prod\",\"type\":\"ferreteria\",\"vendorID\":7}]}";
		JsonObject gson = JsonParser.parseString(son).getAsJsonObject();
		System.out.println("El json es "+ gson.get("table").toString());
		JsonArray fields = gson.getAsJsonArray("fields");
		driver.DbInsert(gson);
	}
	


}
