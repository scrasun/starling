package com.scheshire.starlingtest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.scheshire.starlingtest.models.User;

public interface UserRepo extends JpaRepository<User, Long>
{
	@Query("SELECT u FROM User u WHERE u.email = ?1")
    public User findByEmail(String email);
}
