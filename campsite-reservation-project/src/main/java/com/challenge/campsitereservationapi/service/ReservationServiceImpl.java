package com.challenge.campsitereservationapi.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.challenge.campsitereservationapi.exception.DateNotAvailableException;
import com.challenge.campsitereservationapi.exception.InactiveBookingException;
import com.challenge.campsitereservationapi.exception.ReservationNotFoundException;
import com.challenge.campsitereservationapi.model.Reservation;
import com.challenge.campsitereservationapi.repository.ReservationRepository;

/**
 * @author Aswathy
 *
 */
@Service
@Transactional
public class ReservationServiceImpl implements ReservationService{
	

	@Value("${campsite.max.reservedDays}")
	private Integer maxReservdays;
	
	@Value("${campsite.max.advanceBookingMonths}")
	private Integer maxAdvanceBooking;
	
	@Value("${campsite.max.capacity}")
	private Long maxCampCapacity;

	@Autowired
	private ReservationRepository reservationRepository;
	
	/*
	 * returns the available dates between the date range entered.
	 */
	@Override
	public List<LocalDate> findAvailableDates(LocalDate arrivalDate, LocalDate departureDate,Long members) {
		
    List<LocalDate> availableDatesBetween = arrivalDate.datesUntil(departureDate.plusDays(1))
                                            .collect(Collectors.toList());    
    List<Object[]> reservations = reservationRepository.findForBookedDateRanges(arrivalDate, departureDate,members,maxCampCapacity);
    for (int i=0; i< reservations.size(); i++){
   	Object[] res=reservations.get(i);
   	LocalDate resArrivalDate=(LocalDate) res[0];
   	LocalDate resDepartureDate=(LocalDate) res[1];
    List<LocalDate> reservedDates=resArrivalDate.datesUntil(resDepartureDate).collect(Collectors.toList());
    	availableDatesBetween.removeAll(reservedDates);
    }
        return availableDatesBetween;
	}

	/**
	 * Returns the reservation record when the id is passed
	 */
	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Reservation findReservationByBookingIdentifier(Long bookingId) {
		Optional<Reservation> reservation = reservationRepository.findById(bookingId);
	    if (reservation.isEmpty()) {
	    	String message = String.format("Reservation Not Found with bookingId: %d",bookingId);
	      throw new ReservationNotFoundException(message);
	    }
	    return reservation.get();
	}

	/**
	 * Creates Reservation record when a booking is done and returns the created entity
	 */
	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Reservation createReservation(Reservation reservation) {
		 if(!validateDates(reservation.getArrivalDate(),reservation.getDepartureDate())) {
				return null;
		    }
		 List<LocalDate> availableDays = findAvailableDates(reservation.getArrivalDate(), reservation.getDepartureDate(),(long) reservation.getMembers());

		    if (!availableDays.containsAll(reservation.getReservedDates())) {
				
				 String message = String.format("Campsite not available from %s to %s",
				 reservation.getArrivalDate(), reservation.getDepartureDate());
				 
		      throw new DateNotAvailableException(message);
		    }
		    return reservationRepository.save(reservation);
	}

	/**
	 *Update an already existing reservation record when its id is passed.
	 *Returns the updated entity information.
	 */
	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Reservation updateReservation(Long id,Reservation reservation) {
		Reservation oldReservation=findReservationByBookingIdentifier(id);
		 if (!oldReservation.getStatus().equalsIgnoreCase("ACTIVE")) {
		      String message = String.format("Reservation with booking identifier=%s is either cancelled or inactive", id);
		      throw new InactiveBookingException(message);
		    }
		 if(!validateDates(reservation.getArrivalDate(),reservation.getDepartureDate())) {
				return null;
		    }
		 List<LocalDate> availableDays = findAvailableDates(reservation.getArrivalDate(), reservation.getDepartureDate(),(long) reservation.getMembers());

		    if (!availableDays.containsAll(reservation.getReservedDates())) {
		      String message = String.format("Campsite not available from %s to %s",
		    		  reservation.getArrivalDate(), reservation.getDepartureDate());
		      throw new DateNotAvailableException(message);
		    }
		    Reservation updatedReservation=oldReservation;
		    updatedReservation.setFullName(reservation.getFullName());
		    updatedReservation.setEmail(reservation.getEmail());
		    updatedReservation.setArrivalDate(reservation.getArrivalDate());
		    updatedReservation.setDepartureDate(reservation.getDepartureDate());
		    updatedReservation.setMembers(reservation.getMembers());
		    return reservationRepository.save(updatedReservation);
	}

	/**
	 * Cancel an existing reservation when its id is passed
	 */
	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void cancelReservation(Long bookingIdentifier) {
		Reservation reservation=findReservationByBookingIdentifier(bookingIdentifier);	
	    reservationRepository.delete(reservation);
	    }

	/**
	 * Validating the requested arrival and departure dates
	 * @param arrivaldate
	 * @param departureDate
	 * @return
	 */
	private boolean validateDates(LocalDate arrivaldate, LocalDate departureDate) {
		LocalDate currentDate = LocalDate.now();
		
		if(arrivaldate.isBefore(currentDate)) 
			throw new IllegalArgumentException("Arrival date must be after current date");
		if(departureDate.isBefore(currentDate))
			throw new IllegalArgumentException("Departure date must be after current date");
		if(arrivaldate.isAfter(departureDate))
		    throw new IllegalArgumentException( "Departure date must be equal to arrival date or greater than arrival date");

		if(ChronoUnit.DAYS.between(arrivaldate,departureDate)>maxReservdays) {
			String message = String.format("Campsite cannot be reserved for more than %d days",maxReservdays);				 
			      throw new DateNotAvailableException(message);
		}
		if(ChronoUnit.MONTHS.between(currentDate,arrivaldate)>maxAdvanceBooking) {
			String message = String.format("Campsite can be reserved only %d month in advance",maxAdvanceBooking);				 
			      throw new DateNotAvailableException(message);
		}		
	return true;
	}
}
