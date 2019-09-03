package com.quicksale.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quicksale.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

}
