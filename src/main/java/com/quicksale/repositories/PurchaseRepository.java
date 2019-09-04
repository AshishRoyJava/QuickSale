package com.quicksale.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quicksale.models.Purchase;

/**
 * Repository interface for Purchase related operations
 * 
 * @author ashishr
 *
 */
@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

}
