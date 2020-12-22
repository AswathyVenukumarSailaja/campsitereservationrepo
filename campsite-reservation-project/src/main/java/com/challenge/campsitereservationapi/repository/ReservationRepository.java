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

	@Lock(LockModeType.PESSIMISTIC_READ)
	  @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="100")})
	  @Query("select r from Reservation r "
	      + "where ((r.arrivalDate < ?1 and ?2 < r.departureDate) "
	      + "or (?1 < r.departureDate and r.departureDate <= ?2) "
	      + "or (?1 <= r.arrivalDate and r.arrivalDate <=?2)) "
	      + "and r.status = 'ACTIVE' and r.members + ?3 >= ?4 "
	      + "order by r.arrivalDate asc")
	List<Reservation> findForReservedDateRanges(LocalDate arrivalDate, LocalDate departureDate, Integer members, Integer maxCampCapacity);

	Optional<Reservation> findById(Long bookingId);

}
