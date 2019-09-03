package com.quicksale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.quicksale.models.Inventory;
import com.quicksale.models.Product;
import com.quicksale.models.User;
import com.quicksale.repositories.InventoryRepository;
import com.quicksale.repositories.ProductRepository;
import com.quicksale.repositories.UserRepository;

@SpringBootApplication
public class QuickSaleApplication {

	@Autowired
	private ServletContext servletContext;

	public static void main(String[] args) {
		SpringApplication.run(QuickSaleApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadUsers(UserRepository userRepository) {

		Stream<String> userStream = Stream.of("ABC, Hyderabad, abc@gmail.com", "DEF, Hyderabad, def@gmail.com");

		List<User> users = new ArrayList<>();
		userStream.forEach(userString -> {
			String[] userInfo = userString.split(",");
			users.add(new User(userInfo[0], userInfo[1], userInfo[2]));
		});

		return args -> {
			users.forEach(user -> userRepository.save(user));
		};
	}

	@Bean
	public CommandLineRunner loadProductsAndInventory(ProductRepository productRepository,
			InventoryRepository inventoryRepository) {

		Stream<String> productStream = Stream.of("Watch1, Expensive watch, 5000", "Watch2, Cheap Watch, 500");

		List<Product> products = new ArrayList<>();
		productStream.forEach(productString -> {
			String[] productInfo = productString.split(",");
			products.add(new Product(productInfo[0], productInfo[1], Integer.parseInt(productInfo[2].trim())));
		});
		return args -> {
			products.forEach(product -> {
				Product savedProduct = productRepository.save(product);
				Inventory inventory = new Inventory(savedProduct, 20);
				inventoryRepository.save(inventory);
			});
			loadInventoryToContext(inventoryRepository);
		};
	}

	public void loadInventoryToContext(InventoryRepository inventoryRepository) {
		List<Inventory> inventories = inventoryRepository.findAll();
		Map<Integer, Integer> inventoryMap = new HashMap<>();
		for (Inventory inventory : inventories) {
			inventoryMap.put(inventory.getProduct().getId(), inventory.getCount());
		}
		servletContext.setAttribute("stock", inventoryMap);
	}

}
