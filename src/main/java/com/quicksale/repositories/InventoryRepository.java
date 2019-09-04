package com.quicksale.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quicksale.models.Inventory;
import com.quicksale.models.Product;

/**
 * Repository interface for Inventory related operations
 * 
 * @author ashishr
 *
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

	public Inventory findByProduct(Product product);
}
