package com.gamesys.timetravel.spacetimemachine.service;

import static com.gamesys.timetravel.spacetimemachine.constant.AppConstants.DEFAULT_PGI_PREFIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gamesys.timetravel.spacetimemachine.entity.TravelLogEntity;
import com.gamesys.timetravel.spacetimemachine.entity.TravellerEntity;
import com.gamesys.timetravel.spacetimemachine.exception.DataNotFoundException;
import com.gamesys.timetravel.spacetimemachine.exception.DuplicateDataException;
import com.gamesys.timetravel.spacetimemachine.exception.ParadoxException;
import com.gamesys.timetravel.spacetimemachine.model.TravelLog;
import com.gamesys.timetravel.spacetimemachine.model.Traveller;
import com.gamesys.timetravel.spacetimemachine.repository.TravelLogRepository;
import com.gamesys.timetravel.spacetimemachine.repository.TravellerRepository;

@ExtendWith(SpringExtension.class)
public class TravellerServiceTest {

	@Mock
	private TravellerRepository travellerRepository;
	@Mock
	private TravelLogRepository travelLogRepository;
	@InjectMocks
	TravellerServiceImpl travellerService;

	private static String testPgi = "KART2021";
	private static String testName = "Karthick";
	private static String testShortName = "Kar";
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
	@DisplayName("Get traveller by PGI Service")
	public void getTravellerByPGI() {
		Optional<TravellerEntity> travellerEntityResponse = Optional
				.of(new TravellerEntity(testPgi, testName, testEmailId));

		when(travellerRepository.findById(testPgi)).thenReturn(travellerEntityResponse);
		Traveller traveller = travellerService.getTraveller(testPgi);

		assertThat(traveller.getName()).isEqualTo(travellerEntityResponse.get().getName());
		assertThat(traveller.getEmailId()).isEqualTo(travellerEntityResponse.get().getEmailId());
		assertThat(traveller.getPgi()).isEqualTo(travellerEntityResponse.get().getPgi());
	}

	@Test
	@DisplayName("Get traveller by PGI Service throws DataNotFoundException")
	public void getTravellerByPGI_DataNotFoundException() {
		Optional<TravellerEntity> travellerEntityResponse = Optional.empty(); 

		when(travellerRepository.findById(testPgi)).thenReturn(travellerEntityResponse);
		DataNotFoundException thrown = assertThrows(DataNotFoundException.class,() -> travellerService.getTraveller(testPgi));

		assertThat(thrown.getMessage()).isEqualTo("For PGI - "+ testPgi + " not present");
	}

	@Test
	@DisplayName("Get travel log by PGI Service")
	public void getTravelLogByPGI() {
		List<TravelLogEntity> travelLogEntitiesResponse = new ArrayList<>();
		travelLogEntitiesResponse.add(new TravelLogEntity(testPgi, testPlace, testDate));

		when(travelLogRepository.findByPgi(testPgi)).thenReturn(travelLogEntitiesResponse);
		List<TravelLog> travelLogsResponse = travellerService.getTravelLogs(testPgi);

		assertThat(travelLogsResponse.get(0).getPgi()).isEqualTo(testPgi);
		assertThat(travelLogsResponse.get(0).getPlace()).isEqualTo(testPlace);
	}
	
	@Test
	@DisplayName("Get travel log by PGI Service throws DataNotFoundException")
	public void getTravelLogByPGI_dataNotFoundException() {
		when(travelLogRepository.findByPgi(testPgi)).thenReturn(null);
		DataNotFoundException thrown = assertThrows(DataNotFoundException.class, ()->travellerService.getTravelLogs(testPgi));
		
		assertThat(thrown.getMessage()).isEqualTo("For PGI - " + testPgi + " travel log not present");
	}

	@Test
	@DisplayName("Register new traveller  Service")
	public void registerNewTraveller() {
		Traveller travellerRequest = new Traveller(null, testName, testEmailId);
		TravellerEntity travellerEntityResponse = new TravellerEntity(testPgi,testName,testEmailId);

		when(travellerRepository.save(any(TravellerEntity.class))).thenReturn(travellerEntityResponse);
		Traveller travellerResponse = travellerService.registerNewTraveller(travellerRequest);

		assertThat(travellerResponse.getName()).isEqualTo(travellerRequest.getName());
		assertThat(travellerResponse.getEmailId()).isEqualTo(travellerRequest.getEmailId());
		assertThat(travellerResponse.getPgi()).isNotBlank();
	}
	
	@Test
	@DisplayName("Register new traveller  Service PGI with default prefix")
	public void registerNewTraveller_defaultPGIPrefix() {
		Traveller travellerRequest = new Traveller(null, testShortName, testEmailId);
		TravellerEntity travellerEntityResponse = new TravellerEntity(DEFAULT_PGI_PREFIX,testShortName,testEmailId);

		when(travellerRepository.save(any(TravellerEntity.class))).thenReturn(travellerEntityResponse);
		Traveller travellerResponse = travellerService.registerNewTraveller(travellerRequest);

		assertThat(travellerResponse.getPgi()).contains(DEFAULT_PGI_PREFIX);
		assertThat(travellerResponse.getEmailId()).isEqualTo(travellerRequest.getEmailId());
		assertThat(travellerResponse.getName()).isEqualTo(testShortName);
	}
	
	@Test
	@DisplayName("Register new traveller  Service  throws DuplicateDataException")
	public void registerNewTraveller_duplicateRegistration() {
		Traveller travellerRequest = new Traveller(null, testName, testEmailId);
		TravellerEntity travellerEntityResponse = new TravellerEntity(testPgi,testName,testEmailId);
		when(travellerRepository.findByEmailId(any(String.class))).thenReturn(Optional.of(travellerEntityResponse));
		DuplicateDataException thrown = assertThrows(DuplicateDataException.class,() -> travellerService.registerNewTraveller(travellerRequest));

		assertThat(thrown.getMessage()).isEqualTo("Already a person registered with this email id");
		}

	@Test
	@DisplayName("Do Travel Service")
	public void doTravel() {
		TravelLog travelLogRequest = new TravelLog(testPgi, testPlace, testDate);
		TravelLogEntity travelLogEntityResponse = new TravelLogEntity(testPgi, testPlace, testDate);
		Optional<TravellerEntity> travellerEntityResponse = Optional.of(new TravellerEntity(testPgi, testName, testEmailId));

		when(travellerRepository.findById(testPgi)).thenReturn(travellerEntityResponse);
		when(travelLogRepository.save(any(TravelLogEntity.class))).thenReturn(travelLogEntityResponse);
		TravelLog travelLogResponse = travellerService.doTravel(travelLogRequest);

		assertThat(travelLogResponse.getPgi()).isEqualTo(travelLogRequest.getPgi());
		assertThat(travelLogResponse.getDate()).isEqualTo(travelLogRequest.getDate());
	}
	
	@Test
	@DisplayName("Do Travel Service throws ParadoxException")
	public void doTravel_ParadoxException() {
		TravelLog travelLogRequest = new TravelLog(testPgi, testPlace, testDate);
		TravelLogEntity travelLogEntityResponse = new TravelLogEntity(testPgi, testPlace, testDate);
		Optional<TravellerEntity> travellerEntityResponse = Optional.of(new TravellerEntity(testPgi, testName, testEmailId));
		when(travellerRepository.findById(testPgi)).thenReturn(travellerEntityResponse);
		when(travelLogRepository.findByPgiAndPlaceAndDate(testPgi, testPlace, testDate)).thenReturn(Optional.of(travelLogEntityResponse));
		
		ParadoxException thrown = assertThrows(ParadoxException.class, () -> travellerService.doTravel(travelLogRequest));
		assertThat(thrown.getMessage()).isEqualTo("Other version of yourself is already there. Dont want to create Paradox");
	}

}
