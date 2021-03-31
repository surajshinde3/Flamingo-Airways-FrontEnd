package com.zensar.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.zensar.model.Airport;
import com.zensar.model.Flight;
import com.zensar.repository.FlightRepository;
import com.zensar.services.FlightService;


@Service
public class FlightServiceImpl implements FlightService {

	@Autowired
    private FlightRepository flightRepository;

    @Autowired
    public FlightServiceImpl(FlightRepository flightRepository){
        this.flightRepository = flightRepository;
    }
    @Override
    public Page<Flight> getAllFlightsPaged(int pageNum) {
        return flightRepository.findAll(PageRequest.of(pageNum,5, Sort.by("departureAirport")));
    }

    @Override
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    @Override
    public Flight getFlightById(long flightId) {
        return flightRepository.findById(flightId).orElse(null);
    }

    @Override
    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    @Override
    public void deleteFlightById(long flightId) {
        flightRepository.deleteById(flightId);
    }

    @Override
    public List<Flight> getAllFlightsByAirportAndDepartureTime(Airport depAirport, Airport destAirport, LocalDate depDate) {
        return flightRepository.findAllByDepartureAirportEqualsAndDestinationAirportEqualsAndDepartureDateEquals(depAirport, destAirport, depDate);
    }
}
