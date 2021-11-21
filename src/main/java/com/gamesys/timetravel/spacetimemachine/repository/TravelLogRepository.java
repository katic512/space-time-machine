package com.gamesys.timetravel.spacetimemachine.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gamesys.timetravel.spacetimemachine.entity.TravelLogEntity;

@Repository
public interface TravelLogRepository extends CrudRepository<TravelLogEntity, Integer>{

	List<TravelLogEntity> findByPgi(String pgi);
	
	Optional<TravelLogEntity> findByPgiAndPlaceAndDate(String pgi, String place, Date date);

}
