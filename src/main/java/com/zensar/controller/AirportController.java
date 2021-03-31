package com.zensar.controller;

import java.util.ArrayList;

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

import com.zensar.model.Airport;
import com.zensar.services.AirportService;

@Controller
public class AirportController {

	@Autowired
	AirportService airportService;

	@Autowired
	RestTemplate restTemplate;

	@GetMapping("/airport/new")
	public String showAddAirportPage(Model model) {
		model.addAttribute("airport", new Airport());
		return "newAirport";
	}

	@PostMapping("/airport/new")
	public String saveAirport(@Valid @ModelAttribute("airport") Airport airport, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("errors", bindingResult.getAllErrors());
			model.addAttribute("airport", new Airport());
			return "newAirport";
		}

		restTemplate.postForObject("http://localhost:9092/airportnew", airport, Airport.class);
		System.out
				.println("abcd  abcd" + restTemplate.getForObject("http://localhost:9092/flightnew1", ArrayList.class));
		model.addAttribute("airports", restTemplate.getForObject("http://localhost:9092/flightnew1", ArrayList.class));
		model.addAttribute("currentPage", 0);
		return "airports";
	}

	@GetMapping("/airport/delete")
	public String deleteAirport(@PathParam("airportId") int airportId, Model model) {
		System.out.println("front delete" + airportId);
		restTemplate.delete("http://localhost:9092/airportdelete/" + airportId);
		// airportService.deleteAirport(airportId);
		model.addAttribute("airports", restTemplate.getForObject("http://localhost:9092/flightnew1", ArrayList.class));
		model.addAttribute("currentPage", 0);
		return "airports";
	}

	@GetMapping("/airports")
	public String showAirportsList(@RequestParam(defaultValue = "0") int pageNo, Model model) {
		model.addAttribute("airports", restTemplate.getForObject("http://localhost:9092/flightnew1", ArrayList.class));
		model.addAttribute("currentPage", pageNo);
		return "airports";
	}
}
