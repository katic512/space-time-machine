package com.gamesys.timetravel.spacetimemachine.service;

import static com.gamesys.timetravel.spacetimemachine.constant.AppConstants.DEFAULT_PGI_PREFIX;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.gamesys.timetravel.spacetimemachine.controller.TravellerController;
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
/**
 * 
 * @author Karthick Narasimhan
 *
 */
@Service
public class TravellerServiceImpl implements TravellerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TravellerController.class);

	@Autowired
	TravellerRepository travellerRepository;

	@Autowired
	TravelLogRepository travelLogRepository;

	/**
	 * Returns a traveller object for the given pgi.
	 *
	 * @param pgi
	 * @return traveller object
	 * @throws DataNotFoundException if traveller not found for the given pgi
	 */
	@Override
	public Traveller getTraveller(String pgi) {
		String strLogPrefix = "Method : getTraveller()";
		LOGGER.info("{} | Entry ", strLogPrefix);
		Optional<TravellerEntity> travellerEntity = travellerRepository.findById(pgi);
		if (travellerEntity.isEmpty())
			throw new DataNotFoundException("For PGI - " + pgi + " not present");
		LOGGER.info("{} | Exit ", strLogPrefix);
		return travellerEntity.get().convertToTraveller();
	}

	/**
	 * Generates PGI for the given name. PGI will be in alphanumeric format. If the
	 * name length is greater than or equal to 4, then prefix is set to first four
	 * characters of the given name. If the length of the name is lesser than 4,
	 * then the default prefix - TVLR is set. Random number is generated in the
	 * range of 10 to 999999 and set as suffix. This combination of prefix and
	 * suffix is concatenated and returned as PGI.
	 *
	 * @param name
	 * @return pgi
	 */
	private String generatePGI(String name) {
		String strLogPrefix = "Method : generatePGI()";
		LOGGER.info("{} | Entry ", strLogPrefix);
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
		LOGGER.info("{} | Exit ", strLogPrefix);
		return newPGIStringBuilder.toString();
	}

	/**
	 * Registers a new traveller.
	 *
	 * @param new traveller object which has name and email id
	 * @return persisted traveller object with generated pgi
	 */
	@Override
	public Traveller registerNewTraveller(Traveller newTraveller) {
		String strLogPrefix = "Method : registerNewTraveller()";
		LOGGER.info("{} | Entry ", strLogPrefix);
		travellerRepository.findByEmailId(newTraveller.getEmailId()).ifPresent((t) -> {
			throw new DuplicateDataException("Already a person registered with this email id");
		});
		TravellerEntity travellerEntity = new TravellerEntity(generatePGI(newTraveller.getName()),
				newTraveller.getName(), newTraveller.getEmailId());
		LOGGER.info("{} | Exit ", strLogPrefix);
		return travellerRepository.save(travellerEntity).convertToTraveller();
	}

	/**
	 * Performs travel.
	 *
	 * @param travel request
	 * @return travel log
	 * @throws ParadoxException if a traveller tries to travel to same place at the same time
	 */
	@Override
	public TravelLog doTravel(TravelLog travelRequest) {
		String strLogPrefix = "Method : doTravel()";
		LOGGER.info("{} | Entry ", strLogPrefix);
		getTraveller(travelRequest.getPgi());
		travelLogRepository
				.findByPgiAndPlaceAndDate(travelRequest.getPgi(), travelRequest.getPlace(), travelRequest.getDate())
				.ifPresent((t) -> {
					throw new ParadoxException(
							"Other version of yourself is already there. Dont want to create Paradox");
				});
		TravelLogEntity travelLogEntity = travelLogRepository
				.save(new TravelLogEntity(travelRequest.getPgi(), travelRequest.getPlace(), travelRequest.getDate()));
		LOGGER.info("{} | Exit ", strLogPrefix);
		return travelLogEntity.convertToTravelLog();
	}

	/**
	 * Retrieves the travel logs for the given pgi.
	 *
	 * @param pgi
	 * @return list of travel log for the given pgi
	 * @throws DataNotFoundException if travel log is not present for the given pgi
	 */
	@Override
	public List<TravelLog> getTravelLogs(String pgi) {
		String strLogPrefix = "Method : getTravelLogs()";
		LOGGER.info("{} | Entry ", strLogPrefix);
		List<TravelLogEntity> travelLogEntities = travelLogRepository.findByPgi(pgi);
		if (ObjectUtils.isEmpty(travelLogEntities))
			throw new DataNotFoundException("For PGI - " + pgi + " travel log not present");
		List<TravelLog> travelLogs = new ArrayList<TravelLog>();
		travelLogEntities.forEach(t -> travelLogs.add(t.convertToTravelLog()));
		LOGGER.info("{} | Exit ", strLogPrefix);
		return travelLogs;
	}

}