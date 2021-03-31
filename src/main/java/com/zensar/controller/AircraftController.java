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

import com.zensar.model.Aircraft;
import com.zensar.services.AircraftService;

@Controller
public class AircraftController {

	@Autowired
	AircraftService aircraftService;

	@Autowired
	RestTemplate restTemplate;

	@GetMapping("/aircraft/new")
	public String showAddAircraftPage(Model model) {
		model.addAttribute("aircraft", new Aircraft());
		return "newAircraft";
	}

	@PostMapping("/aircraft/new")
	public String saveAircraft(@Valid @ModelAttribute("aircraft") Aircraft aircraft, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("errors", bindingResult.getAllErrors());
			model.addAttribute("aircraft", new Aircraft());
			return "newAircraft";
		}
		restTemplate.postForObject("http://localhost:9092/aircraftnew", aircraft, Aircraft.class);
		// aircraftService.saveAircraft(aircraft);
		model.addAttribute("aircrafts", restTemplate.getForObject("http://localhost:9092/flightnew", ArrayList.class));
		model.addAttribute("currentPage", 0);
		return "aircrafts";
	}

	@GetMapping("/aircraft/delete")
	public String deleteAircraft(@PathParam("aircraftId") Long aircraftId, Model model) {

		restTemplate.delete("http://localhost:9092/aircraftdelete/" + aircraftId);
		// aircraftService.deleteAircraftById(aircraftId);
		model.addAttribute("aircrafts", restTemplate.getForObject("http://localhost:9092/flightnew", ArrayList.class));
		model.addAttribute("currentPage", 0);
		return "aircrafts";
	}

	@GetMapping("/aircrafts")
	public String showAircraftsList(@RequestParam(defaultValue = "0") int pageNo, Model model) {
		model.addAttribute("aircrafts", restTemplate.getForObject("http://localhost:9092/flightnew", ArrayList.class));
		model.addAttribute("currentPage", pageNo);
		return "aircrafts";
	}

}
