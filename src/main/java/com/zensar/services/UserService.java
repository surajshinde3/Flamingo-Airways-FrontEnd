package com.zensar.services;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.zensar.model.User;

public interface UserService extends UserDetailsService {
	
User findByEmail(String email);
	
	Optional<User> findByUsername(String username);
	
	User save(User userDto);
}
