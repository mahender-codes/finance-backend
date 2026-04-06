package com.mahender.finance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mahender.finance.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User getByEmail(String email);

}