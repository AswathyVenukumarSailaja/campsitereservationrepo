package com.challenge.campsitereservationapi.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.challenge.campsitereservationapi.exception.DateNotAvailableException;
import com.challenge.campsitereservationapi.exception.InactiveBookingException;
import com.challenge.campsitereservationapi.exception.ReservationNotFoundException;
import com.challenge.campsitereservationapi.model.Reservation;
import com.challenge.campsitereservationapi.repository.ReservationRepository;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService{

	@Autowired
	private ReservationRepository reservationRepository;
	
	@Override
	public List<LocalDate> findAvailableDates(LocalDate startDate, LocalDate endDate) {

    if(!validateDates(startDate,endDate)) {
		return null;
    }
    List<LocalDate> availableDatesBetween = startDate.datesUntil(endDate.plusDays(1))
                                            .collect(Collectors.toList());
        List<Reservation> reservations = reservationRepository.findForReservedDateRanges(startDate, endDate);
        reservations.forEach(r -> availableDatesBetween.removeAll(r.getReservedDates()));
        return availableDatesBetween;
	
	}

	@Override
	public Reservation findReservationByBookingIdentifier(Long bookingId) {
		Optional<Reservation> reservation = reservationRepository.findByBookingIdentifier(bookingId);
	    if (reservation.isEmpty()) {
	      throw new ReservationNotFoundException("Reservation Not Found");
	    }
	    return reservation.get();
	}

	@Override
	public Reservation createReservation(Reservation reservation) {
		 List<LocalDate> availableDays = findAvailableDates(reservation.getArrivalDate(), reservation.getDepartureDate());

		    if (!availableDays.containsAll(reservation.getReservedDates())) {
		      String message = String.format("Dates not available from %s to %s",
		    		  reservation.getArrivalDate(), reservation.getDepartureDate());
		      throw new DateNotAvailableException(message);
		    }
		    return reservationRepository.save(reservation);
	}

	@Override
	public Reservation updateReservation(Reservation reservation) {
		Reservation oldReservation=findReservationByBookingIdentifier(reservation.getBookingIdentifier());
		 if (!oldReservation.getStatus().equalsIgnoreCase("ACTIVE")) {
		      String message = String.format("Reservation with booking identifier=%s is either cancelled or inactive", reservation.getBookingIdentifier());
		      throw new InactiveBookingException(message);
		    }
		 List<LocalDate> availableDays = findAvailableDates(reservation.getArrivalDate(), reservation.getDepartureDate());

		    if (!availableDays.containsAll(reservation.getReservedDates())) {
		      String message = String.format("Dates not available from %s to %s",
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

	@Override
	public void cancelReservation(Long bookingIdentifier) {
		Reservation reservation=findReservationByBookingIdentifier(bookingIdentifier);
	    reservationRepository.delete(reservation);
	    }

	private boolean validateDates(LocalDate startDate, LocalDate endDate) {
		
	LocalDate currentDate = LocalDate.now();
	if(!startDate.isAfter(currentDate)) 
		throw new IllegalArgumentException("Start Date must be after current date");
	if(!endDate.isAfter(currentDate))
		throw new IllegalArgumentException("End Date must be after current date");
	if(!startDate.isEqual(endDate) || !startDate.isBefore(endDate))
	    throw new IllegalArgumentException( "End date must be equal to start date or greater than start date");
		
	return true;
	}
}
