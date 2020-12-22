package com.challenge.campsitereservationapi.service;

import java.time.LocalDate;
import java.util.List;

import com.challenge.campsitereservationapi.model.Reservation;

public interface ReservationService {

	  List<LocalDate> findAvailableDates(LocalDate startDate, LocalDate endDate,Integer members);

	  Reservation findReservationByBookingIdentifier(Long bookingId);

	  Reservation createReservation(Reservation reservation);

	  Reservation updateReservation(Long id,Reservation reservation);

	   void cancelReservation(Long bookingId);
}
