package com.zensar.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.zensar.model.Aircraft;
import com.zensar.model.Airport;
import com.zensar.model.Flight;
import com.zensar.model.Passenger;
import com.zensar.model.User;
import com.zensar.services.AircraftService;
import com.zensar.services.AirportService;
import com.zensar.services.FlightService;
import com.zensar.services.UserService;

@Controller
public class MainController {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	UserService userService;

	@GetMapping("/")
	public String showHomePage() {
		return "index";
	}

	@GetMapping("/register")
	public String getRegisterPage() {
		return "register";
	}

	@PostMapping("/register")
	public String registerUserAccount(@ModelAttribute("user") User userDto, BindingResult result, Model model) {
		System.out.println(userDto.getEmail());
		System.out.println(userDto.getUsername());

		/*
		 * User existing = userService.findByEmail(userDto.getEmail()); String email =
		 * existing.getEmail(); Optional<User> uexist =
		 * userService.findByUsername(userDto.getUsername());
		 * 
		 * // String username = uexist.get().getUsername();
		 * 
		 * 
		 * if (existing != null && uexist != null){ model.addAttribute("existingemail",
		 * email); model.addAttribute("existingusername", username);
		 * result.rejectValue("email", null,
		 * "There is already an account registered with that email"); }
		 * 
		 * 
		 * 
		 * if (existing != null){ model.addAttribute("existingemail", email);
		 * result.rejectValue("email", null,
		 * "There is already an account registered with that email"); }
		 * 
		 * 
		 * 
		 * if (username != null) { model.addAttribute("existingusername", username);
		 * result.rejectValue("username", null,
		 * "There is already an account registered with that username"); }
		 */

		if (result.hasErrors()) {
			return "register";
		} else {

			userService.save(userDto);
			return "redirect:/login";
		}

	}

	@GetMapping("/login")
	public String showLoginPage() {
		return "login";
	}
}
