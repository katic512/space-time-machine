package com.gamesys.timetravel.spacetimemachine.service;

import static com.gamesys.timetravel.spacetimemachine.constant.AppConstants.DEFAULT_PGI_PREFIX;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.gamesys.timetravel.spacetimemachine.entity.TravelLogEntity;
import com.gamesys.timetravel.spacetimemachine.entity.TravellerEntity;
import com.gamesys.timetravel.spacetimemachine.exception.DataNotFoundException;
import com.gamesys.timetravel.spacetimemachine.exception.DuplicateDataException;
import com.gamesys.timetravel.spacetimemachine.exception.ParadoxException;
import com.gamesys.timetravel.spacetimemachine.model.TravelLog;
import com.gamesys.timetravel.spacetimemachine.model.Traveller;
import com.gamesys.timetravel.spacetimemachine.repository.TravelLogRepository;
import com.gamesys.timetravel.spacetimemachine.repository.TravellerRepository;
import com.gamesys.timetravel.spacetimemachine.util.AppUtils;

@Service
public class TravellerServiceImpl implements TravellerService {

	@Autowired
	TravellerRepository travellerRepository;
	
	@Autowired
	TravelLogRepository travelLogRepository;
	
	@Override
	public Traveller getTraveller(String pgi) {
		Optional<TravellerEntity> travellerEntity = travellerRepository.findById(pgi);
		if(travellerEntity.isEmpty())
			throw new DataNotFoundException("For PGI - "+ pgi + " not present");
		return travellerEntity.get().convertToTraveller();
	}

	private String generatePGI(String name) {
		StringBuilder newPGIStringBuilder = new StringBuilder();
		if (name.length() >= 4) {
			newPGIStringBuilder.append(name.substring(0, 4).toUpperCase());
		} else {
			newPGIStringBuilder.append(DEFAULT_PGI_PREFIX);
		}
		newPGIStringBuilder.append(String.valueOf(AppUtils.getRandomNumInRange()));
		Optional<TravellerEntity> travellerEntity = travellerRepository.findById(newPGIStringBuilder.toString());
		if (travellerEntity.isPresent())
			generatePGI(name);
		return newPGIStringBuilder.toString();
	}

	@Override
	public Traveller registerNewTraveller(Traveller newTraveller) {
		travellerRepository.findByEmailId(newTraveller.getEmailId()).ifPresent((t) -> {
			throw new DuplicateDataException("Already a person registered with this email id");
		});
		TravellerEntity travellerEntity = new TravellerEntity(generatePGI(newTraveller.getName()),
				newTraveller.getName(), newTraveller.getEmailId());
		return travellerRepository.save(travellerEntity).convertToTraveller();
	}


	@Override
	public TravelLog doTravel(TravelLog travelRequest) {
		getTraveller(travelRequest.getPgi());
		travelLogRepository.findByPgiAndPlaceAndDate(travelRequest.getPgi(), travelRequest.getPlace(), travelRequest.getDate()).ifPresent((t) -> {
			throw new ParadoxException("Other version of yourself is already there. Dont want to create Paradox");
		});
		TravelLogEntity travelLogEntity = travelLogRepository
				.save(new TravelLogEntity(travelRequest.getPgi(), travelRequest.getPlace(), travelRequest.getDate()));
		return travelLogEntity.convertToTravelLog();
	}

	@Override
	public List<TravelLog> getTravelLogs(String pgi) {
		List<TravelLogEntity> travelLogEntities = travelLogRepository.findByPgi(pgi);
		if (ObjectUtils.isEmpty(travelLogEntities))
			throw new DataNotFoundException("For PGI - " + pgi + " travel log not present");
		List<TravelLog> travelLogs = new ArrayList<TravelLog>();
		travelLogEntities.forEach(t->travelLogs.add(t.convertToTravelLog()));
		return travelLogs;
	}

}