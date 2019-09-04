package com.quicksale.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quicksale.models.Product;
import com.quicksale.models.RegisteredUser;
import com.quicksale.models.User;

/**
 * Repository interface for Registered User related operations
 * 
 * @author ashishr
 *
 */
@Repository
public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Integer> {

	public RegisteredUser findByUserAndProduct(User user, Product product);

}
