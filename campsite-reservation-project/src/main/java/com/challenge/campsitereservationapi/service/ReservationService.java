package com.challenge.campsitereservationapi.service;

import java.time.LocalDate;
import java.util.List;

import com.challenge.campsitereservationapi.model.Reservation;

public interface ReservationService {

	  List<LocalDate> findAvailableDates(LocalDate startDate, LocalDate endDate);

	  Reservation findReservationByBookingIdentifier(Long bookingId);

	  Reservation createReservation(Reservation booking);

	  Reservation updateReservation(Reservation booking);

	   void cancelReservation(Long bookingId);
}
