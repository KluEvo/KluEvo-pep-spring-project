package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Account;

import java.util.Optional;
@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    
    // Custom query method to find an Account by its username
    Optional<Account> findByUsername(String username);
}