package com.challenge.campsitereservationapi.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.challenge.campsitereservationapi.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{
	
	
	  @Query("select r.arrivalDate,r.departureDate from Reservation r "
	      + "where ((r.arrivalDate < ?1 and ?2 < r.departureDate) "
	      + "or (?1 < r.departureDate and r.departureDate <= ?2) "
	      + "or (?1 <= r.arrivalDate and r.arrivalDate <=?2)) "
	      + "and r.status = 'ACTIVE' "
	      + "group by r.arrivalDate,r.departureDate "
	      + "having (SUM(r.members) + ?3) > ?4 ")
	List<Object[]> findForBookedDateRanges(LocalDate arrivalDate, LocalDate departureDate, Long members, Long maxCampCapacity);

	@Lock(value = LockModeType.PESSIMISTIC_READ)
	Optional<Reservation> findById(Long bookingId);

}
