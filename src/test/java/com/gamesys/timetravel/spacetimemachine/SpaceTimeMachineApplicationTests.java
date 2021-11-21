package com.gamesys.timetravel.spacetimemachine;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gamesys.timetravel.spacetimemachine.controller.TravellerController;
import com.gamesys.timetravel.spacetimemachine.entity.TravelLogEntity;
import com.gamesys.timetravel.spacetimemachine.entity.TravellerEntity;
import com.gamesys.timetravel.spacetimemachine.model.TravelLog;
import com.gamesys.timetravel.spacetimemachine.model.Traveller;
import com.gamesys.timetravel.spacetimemachine.repository.TravelLogRepository;
import com.gamesys.timetravel.spacetimemachine.repository.TravellerRepository;
import com.gamesys.timetravel.spacetimemachine.service.TravellerServiceImpl;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class SpaceTimeMachineApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private TravellerRepository travellerRepository;
	@MockBean
	private TravelLogRepository travelLogRepository;

	@InjectMocks
	private TravellerServiceImpl travellerService;

	@InjectMocks
	private TravellerController travellerController;

	private static String testPgi = "KART2021";
	private static String testName = "Karthick";
	private static String testEmailId = "karthick@galaxy.com";
	private static String testPlace = "chennai";
	private static Date testDate;
	
	
	{
		String testDateStr = "2021-08-17";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
		try {
			testDate = formatter.parse(testDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
	}
	
	@Test
	@DisplayName("Get traveller by PGI API")
	public void getTravellerByPGI() {
		Optional<TravellerEntity> travellerEntityResponse = Optional.of(new TravellerEntity(testPgi,testName,testEmailId));
		Traveller travellerResponse = travellerEntityResponse.get().convertToTraveller();
		
		when(travellerRepository.findById(any(String.class))).thenReturn(travellerEntityResponse);

		ResponseEntity<Traveller> response = restTemplate.getForEntity("/traveller/"+testPgi, Traveller.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getName()).isEqualTo(travellerResponse.getName());
		assertThat(response.getBody().getEmailId()).isEqualTo(travellerResponse.getEmailId());
	}
	
	@Test
	@DisplayName("Get travel logs by PGI API")
	public void getTravelLogsByPGI() {
		List<TravelLogEntity> travelLogEntitiesResponse = new ArrayList<>();
		travelLogEntitiesResponse.add(new TravelLogEntity(testPgi,testPlace, testDate));
	
		when(travelLogRepository.findByPgi(any(String.class))).thenReturn(travelLogEntitiesResponse);

		ResponseEntity<List<TravelLog>> response = restTemplate.exchange("/traveller/travelHistory/"+testPgi, HttpMethod.GET,null,new ParameterizedTypeReference<List<TravelLog>>(){});
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().get(0).getPgi()).isEqualTo(testPgi);
		assertThat(response.getBody().get(0).getPlace()).isEqualTo(testPlace);
	}


	@Test
	@DisplayName("Register new traveller API")
	public void registerNewTraveller() {
		Traveller travellerRequest = new Traveller(null,testName,testEmailId);
		TravellerEntity travellerEntityResponse = new TravellerEntity(testPgi,travellerRequest.getName(),travellerRequest.getEmailId());

		when(travellerRepository.save(any(TravellerEntity.class))).thenReturn(travellerEntityResponse);

		ResponseEntity<Traveller> response = restTemplate.postForEntity("/traveller",travellerRequest, Traveller.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody().getPgi()).isEqualTo(testPgi);
		assertThat(response.getBody().getName()).isEqualTo(travellerRequest.getName());
		assertThat(response.getBody().getEmailId()).isEqualTo(travellerRequest.getEmailId());
	}
	
	@Test
	@DisplayName("Do travel API")
	public void doTravel() {
		TravelLog travelLogRequest = new TravelLog(testPgi,testPlace,testDate);
		TravelLogEntity travelLogEntityResponse = new TravelLogEntity(testPgi,testPlace, testDate);
		Optional<TravellerEntity> travellerEntityResponse = Optional.of(new TravellerEntity(testPgi,testName,testEmailId));

		when(travellerRepository.findById(any(String.class))).thenReturn(travellerEntityResponse);
		when(travelLogRepository.save(any(TravelLogEntity.class))).thenReturn(travelLogEntityResponse);

		ResponseEntity<TravelLog> response = restTemplate.postForEntity("/traveller/travel",travelLogRequest, TravelLog.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody().getPgi()).isEqualTo(testPgi);
		assertThat(response.getBody().getPlace()).isEqualTo(travelLogRequest.getPlace());
	}

}
