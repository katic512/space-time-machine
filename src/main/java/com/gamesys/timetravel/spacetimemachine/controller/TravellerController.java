package com.gamesys.timetravel.spacetimemachine.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gamesys.timetravel.spacetimemachine.model.TravelLog;
import com.gamesys.timetravel.spacetimemachine.model.Traveller;
import com.gamesys.timetravel.spacetimemachine.service.TravellerService;

@RestController
@RequestMapping("/traveller")
public class TravellerController {
	@Autowired
	TravellerService travellerServie;
	
	@GetMapping("/{pgi}")
	public ResponseEntity<Traveller> getTraveller(@PathVariable("pgi") String pgi) {
		return new ResponseEntity<>(travellerServie.getTraveller(pgi),HttpStatus.OK);
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, 
        produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Traveller> registerNewTraveller(@Valid @RequestBody Traveller newTraveller) {
		return new ResponseEntity<>(travellerServie.registerNewTraveller(newTraveller),HttpStatus.CREATED);
	}
	
	@GetMapping("/travelHistory/{pgi}")
	public ResponseEntity<List<TravelLog>> getTravelLogs(@PathVariable("pgi") String pgi) {
		return new ResponseEntity<>(travellerServie.getTravelLogs(pgi),HttpStatus.OK);
	}

	@PostMapping(value="/travel",consumes = MediaType.APPLICATION_JSON_VALUE, 
        produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TravelLog> doTravel(@Valid @RequestBody TravelLog travelRequest) {
		return new ResponseEntity<>(travellerServie.doTravel(travelRequest),HttpStatus.CREATED);
	}
}
