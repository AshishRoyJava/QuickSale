package com.quicksale.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quicksale.models.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer>{

}
