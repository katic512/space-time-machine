package com.gamesys.timetravel.spacetimemachine.service;

import java.util.List;

import com.gamesys.timetravel.spacetimemachine.model.TravelLog;
import com.gamesys.timetravel.spacetimemachine.model.Traveller;

public interface TravellerService {
	Traveller getTraveller(String pgi);

	Traveller registerNewTraveller(Traveller newTraveller);

	TravelLog doTravel(TravelLog travelRequest);

	List<TravelLog> getTravelLogs(String pgi);

}
