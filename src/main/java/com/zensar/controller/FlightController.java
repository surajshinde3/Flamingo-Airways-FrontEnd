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

import com.zensar.model.Flight;
import com.zensar.model.Passenger;
import com.zensar.services.FlightService;

@Controller
public class FlightController {

	@Autowired
	FlightService flightService;

	@Autowired
	RestTemplate restTemplate;

	@GetMapping("/flight/new")
	public String showNewFlightPage(Model model) {
		model.addAttribute("flight", new Flight());
		model.addAttribute("aircrafts", restTemplate.getForObject("http://localhost:9092/flightnew", ArrayList.class));
		model.addAttribute("airports", restTemplate.getForObject("http://localhost:9092/flightnew1", ArrayList.class));
		return "newFlight";
	}

	@PostMapping("/flight/new")
	public String saveFlight(@Valid @ModelAttribute("flight") Flight flight, BindingResult bindingResult,
			@RequestParam("departureAirport") int departureAirport,
			@RequestParam("destinationAirport") int destinationAirport, @RequestParam("aircraft") Long aircraftId,
			@RequestParam("arrivalTime") String arrivalTime, @RequestParam("departureTime") String departureTime,
			Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("errors", bindingResult.getAllErrors());
			model.addAttribute("flight", new Flight());
			model.addAttribute("aircrafts",
					restTemplate.getForObject("http://localhost:9092/flightnew", ArrayList.class));
			model.addAttribute("airports",
					restTemplate.getForObject("http://localhost:9092/flightnew1", ArrayList.class));
			return "newFlight";
		}
		if (departureAirport == destinationAirport) {
			model.addAttribute("sameAirportError", "Departure and destination airport can't be same");
			model.addAttribute("flight", new Flight());
			model.addAttribute("aircrafts",
					restTemplate.getForObject("http://localhost:9092/flightnew", ArrayList.class));
			model.addAttribute("airports",
					restTemplate.getForObject("http://localhost:9092/flightnew1", ArrayList.class));
			return "newFlight";
		}
		System.out.println("front insert start");

		restTemplate.postForObject("http://localhost:9092/settingFlight/" + aircraftId + "/" + departureAirport + "/"
				+ destinationAirport + "/" + departureTime + "/" + arrivalTime, flight, Flight.class);

		System.out.println("front insert ok");
		model.addAttribute("flights",
				restTemplate.getForObject("http://localhost:9092/getallflightsss", ArrayList.class));
		// model.addAttribute("currentPage", 0);
		return "flights";
	}

	@GetMapping("/flight/delete")
	public String deleteFlight(@PathParam("flightId") long flightId, Model model) {

		restTemplate.delete("http://localhost:9092/flightdelete/" + flightId);
		model.addAttribute("flights",
				restTemplate.getForObject("http://localhost:9092/getallflightsss", ArrayList.class));
		model.addAttribute("currentPage", 0);
		return "flights";
	}

	@GetMapping("/flights")
	public String showFlightsList(@RequestParam(defaultValue = "0") int pageNo, Model model) {
		model.addAttribute("flights",
				restTemplate.getForObject("http://localhost:9092/getallflightsss", ArrayList.class));
		model.addAttribute("currentPage", pageNo);
		return "flights";
	}

	@GetMapping("/flight/search")
	public String showSearchFlightPage(Model model) {

		// model.addAttribute("airports",
		// restTemplate.getForEntity("http://localhost:9092/flightnew1",Airport.class));
		System.out.println("yhhhhhhhhhhhhhhh");

		model.addAttribute("airports", restTemplate.getForObject("http://localhost:9092/flightnew1", ArrayList.class));
		System.out.println("here we go");
		model.addAttribute("flights", null);
		return "searchFlight";
	}

	@PostMapping("/flight/search")
	public String searchFlight(@RequestParam("departureAirport") int departureAirport,
			@RequestParam("destinationAirport") int destinationAirport,
			@RequestParam("departureTime") String departureTime, Model model, @ModelAttribute Flight flight) {

		if (departureAirport == destinationAirport) {
			model.addAttribute("AirportError", "Departure and destination airport cant be same!");
			model.addAttribute("airports",
					restTemplate.getForObject("http://localhost:9092/flightnew1", ArrayList.class));
			return "searchFlight";
		}
		// restTemplate.exchange("http://localhost:9092/flightnew1",
		// HttpMethod.GET,null,Airport.class);
		List<Flight> postForObject = restTemplate.postForObject("http://localhost:9092/setSearchFlight/"
				+ departureAirport + "/" + destinationAirport + "/" + departureTime, flight, ArrayList.class);
		if (postForObject.isEmpty()) {
			model.addAttribute("notFound", "No Record Found!");
		} else {
			model.addAttribute("flights", postForObject);
		}

		model.addAttribute("airports", restTemplate.getForObject("http://localhost:9092/flightnew1", ArrayList.class));
		return "searchFlight";
	}

	@GetMapping("/flight/book")
	public String showBookFlightPage(Model model) {
		model.addAttribute("airports", restTemplate.getForObject("http://localhost:9092/flightnew1", ArrayList.class));
		return "bookFlight";
	}

	@PostMapping("/flight/book")
	public String searchFlightToBook(@RequestParam("departureAirport") int departureAirport,
			@RequestParam("destinationAirport") int destinationAirport,
			@RequestParam("departureTime") String departureTime, Model model, @ModelAttribute Flight flight) {

		if (departureAirport == destinationAirport) {
			model.addAttribute("AirportError", "Departure and destination airport cant be same!");
			model.addAttribute("airports",
					restTemplate.getForObject("http://localhost:9092/flightnew1", ArrayList.class));
			return "bookFlight";
		}
		// List<Flight> flights =
		// flightService.getAllFlightsByAirportAndDepartureTime(depAirport, destAirport,
		// deptTime);
		List<Flight> postForObject = restTemplate.postForObject("http://localhost:9092/setBookFlight/"
				+ departureAirport + "/" + destinationAirport + "/" + departureTime, flight, ArrayList.class);
		if (postForObject.isEmpty()) {
			model.addAttribute("notFound", "No Record Found!");
		} else {
			model.addAttribute("flights", postForObject);
		}
		model.addAttribute("airports", restTemplate.getForObject("http://localhost:9092/flightnew1", ArrayList.class));
		return "bookFlight";
	}

	@GetMapping("/flight/book/new")
	public String showCustomerInfoPage(@RequestParam("flightId") long flightId, Model model) {
		model.addAttribute("flightId", flightId);
		model.addAttribute("passenger", new Passenger());
		return "newPassenger";
	}

	@PostMapping("/flight/book/new")
	public String bookFlight(@Valid @ModelAttribute("passenger") Passenger passenger, BindingResult bindingResult,
			@RequestParam("flightId") long flightId, Model model) {
//		Flight flight = restTemplate.getForObject("http://localhost:9092/flightbooknew1/"+flightId,Flight.class);
//		
//		Passenger passenger1 = passenger;
//		passenger1.setFlight(flight);
		Passenger postForObject2 = restTemplate.postForObject("http://localhost:9092/setFlightflight/" + flightId,
				passenger, Passenger.class);

		System.out.println("999999999999999999 " + postForObject2);

		model.addAttribute("passenger", postForObject2);
		return "confirmationPage";
	}

	@GetMapping("/flight/book/verify")
	public String showVerifyBookingPage() {
		return "verifyBooking";
	}

	@PostMapping("/flight/book/verify")
	public String showVerifyBookingPageResult(@RequestParam("flightId") long flightId,
			@RequestParam("passengerId") long passengerId, Model model) {

//		Flight flight = restTemplate.getForObject("http://localhost:9092/flightbooknew1/"+flightId,Flight.class);
//		if (flight != null) {
//			model.addAttribute("flight", flight);
//			System.out.println(flight);

		// List<Passenger> passengers = flight.getPassengers();
		/*
		 * List<Passenger> passengers =
		 * restTemplate.getForObject("http://localhost:9092/getPassengers1/"+
		 * flightId+"/"+passengerId, ArrayList.class);
		 */

		Passenger passengers = restTemplate
				.getForObject("http://localhost:9092/getPassengers1/" + flightId + "/" + passengerId, Passenger.class);
		// List<Passenger> passengers =
		// restTemplate.getForObject("http://localhost:9092/getPassengers2",ArrayList.class);
		System.out.println(passengers);
		if (passengers != null) {
			/*
			 * System.out.println("iiiiiiii"+passengers); Passenger passenger = null; for
			 * (Passenger p : passengers) { System.out.println("$$$$$$$$$$$$$$$$$$$");
			 * 
			 * if (p.getPassengerId() == passengerId) { passenger =
			 * restTemplate.getForObject("http://localhost:9092/flightbookverify/"+
			 * passengerId,Passenger.class); model.addAttribute("passenger", passenger); } }
			 */
			System.out.println(passengers);
			model.addAttribute("passenger", passengers);

			if (passengers != null) {
				return "verifyBooking";
			} else {
				model.addAttribute("notFound", "Not Found");
				return "verifyBooking";
			}
		} else {
			model.addAttribute("notFound", "Not Found");
			return "verifyBooking";
		}
	}

	@PostMapping("/flight/book/cancel")
	public String cancelTicket(@RequestParam("passengerId") long passengerId, Model model) {
		restTemplate.delete("http://localhost:9092/flightbookcancel/" + passengerId);
		// passengerService.deletePassengerById(passengerId);
		model.addAttribute("flights",
				restTemplate.getForObject("http://localhost:9092/getallflightsss", ArrayList.class));

		// model.addAttribute("flights", flightService.getAllFlightsPaged(0));
		model.addAttribute("currentPage", 0);
		return "flights";
	}

	@GetMapping("/passengers")
	public String showPassengerList(@RequestParam("flightId") long flightId, Model model) {
		// List<Passenger> passengers =
		// flightService.getFlightById(flightId).getPassengers();

		System.out.println("inside /passenger" + flightId);

		System.out.println("&&&&777777777777777 "
				+ restTemplate.getForObject("http://localhost:9092/passengerEnter/" + flightId, ArrayList.class));

		// Flight
		// f2=restTemplate.getForObject("http://localhost:9092/flightbooknew1/"+flightId,Flight.class);
		List<Passenger> passengers = restTemplate.getForObject("http://localhost:9092/passengerEnter/" + flightId,
				ArrayList.class);

		model.addAttribute("passengers", passengers);
		model.addAttribute("flight",
				restTemplate.getForObject("http://localhost:9092/flightbooknew1/" + flightId, Flight.class));
		return "passengers";
	}

}
