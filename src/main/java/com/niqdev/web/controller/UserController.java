package com.niqdev.web.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

	@GetMapping
	public String getUser() {
		return "Get User";
	}
	
	@PostMapping
	public String createUser() {
		return "Create User";
	}
	
	@PutMapping
	public String updateUser() {
		return "Update User";
	}
	
	@DeleteMapping
	public String deleteUser() {
		return "Delete User";
	}
	
}
