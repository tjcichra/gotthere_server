package com.tim.gotthere_server.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

	@Query("SELECT l FROM Location l WHERE l.dateTime BETWEEN :startDateTime AND :endDateTime ORDER BY dateTime")
	public List<Location> findLocationsBetweenDates(@Param("startDateTime") Date startDateTime, @Param("endDateTime") Date endDateTime);
}
