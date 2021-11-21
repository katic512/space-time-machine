package com.gamesys.timetravel.spacetimemachine.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesys.timetravel.spacetimemachine.model.TravelLog;
import com.gamesys.timetravel.spacetimemachine.model.Traveller;
import com.gamesys.timetravel.spacetimemachine.service.TravellerService;
/**
 * 
 * @author Karthick Narasimhan
 *
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(TravellerController.class)
public class TravellerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TravellerService travellerService;
	
	@Autowired 
	private ObjectMapper mapper;
	
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
	@DisplayName("Get traveller by PGI Handler")
	public void getTravellerByPGI() throws Exception {
		Traveller travellerResponse = new Traveller(testPgi,testName,testEmailId);

		when(travellerService.getTraveller(testPgi)).thenReturn(travellerResponse);

		mockMvc.perform(MockMvcRequestBuilders.get("/traveller/"+testPgi))
		.andExpect(status().isOk()).andExpect(jsonPath("name").value(travellerResponse.getName()))
		.andExpect(jsonPath("emailId").value(travellerResponse.getEmailId()));
	}

	@Test
	@DisplayName("Get travelLogs by PGI Handler")
	public void getTravelLogsByPGI() throws Exception {
		List<TravelLog> travelLogsResponse = new ArrayList<TravelLog>();
		travelLogsResponse.add(new TravelLog(testPgi,testPlace,testDate));

		when(travellerService.getTravelLogs(testPgi)).thenReturn(travelLogsResponse);

		mockMvc.perform(MockMvcRequestBuilders.get("/traveller/travelHistory/"+testPgi))
		.andExpect(status().isOk()).andExpect(jsonPath("$[0].pgi").value(testPgi))
		.andExpect(jsonPath("$[0].place").value(testPlace));
	}
	

	@Test
	@DisplayName("Register new traveller handler")
	public void registerNewTraveller() throws Exception {
		Traveller travellerRequest = new Traveller(null,testName,testEmailId);
		Traveller travellerResponse = new Traveller(testPgi ,travellerRequest.getName(),travellerRequest.getEmailId());
		when(travellerService.registerNewTraveller(any(Traveller.class))).thenReturn(travellerResponse);

		mockMvc.perform(MockMvcRequestBuilders.post("/traveller")
				.content(mapper.writeValueAsString(travellerRequest))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated()).andExpect( jsonPath("$.name").value(travellerRequest.getName()))
		.andExpect(jsonPath("$.emailId").value(travellerRequest.getEmailId()))
		.andExpect(jsonPath("$.pgi").isNotEmpty());
	}
	
	@Test
	@DisplayName("Do travel handler")
	public void doTravel() throws Exception {
		TravelLog travelLogRequest = new TravelLog(testPgi,testPlace,testDate);
		TravelLog travelLogResponse = new TravelLog(testPgi ,testPlace,testDate);
		when(travellerService.doTravel(any(TravelLog.class))).thenReturn(travelLogResponse);

		mockMvc.perform(MockMvcRequestBuilders.post("/traveller/travel")
				.content(mapper.writeValueAsString(travelLogRequest))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated()).andExpect( jsonPath("$.pgi").value(travelLogResponse.getPgi()))
		.andExpect(jsonPath("$.place").value(travelLogResponse.getPlace()));
	}

}
