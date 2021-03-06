package com.quicksale.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quicksale.models.Product;

/**
 * Repository interface for Product related informations
 * 
 * @author ashishr
 *
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

}
