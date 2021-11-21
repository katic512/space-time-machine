package com.gamesys.timetravel.spacetimemachine.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gamesys.timetravel.spacetimemachine.entity.TravellerEntity;

@Repository
public interface TravellerRepository extends CrudRepository<TravellerEntity,String>{

	Optional<TravellerEntity> findByEmailId(String emailId);

}
