package com.challenge.campsitereservationapi.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.campsitereservationapi.model.Reservation;
import com.challenge.campsitereservationapi.service.ReservationService;

@RestController
@RequestMapping("campsite/reservations")
public class ReservationController {
	
	@Value("${campsite.max.advanceBookingMonths}")
	private Integer maxAdvanceBooking;
	
	@Autowired
	private ReservationService reservationService;

	public ReservationController(ReservationService reservationService) {
		super();
		this.reservationService = reservationService;
	}
	
	@GetMapping(value = "/availabilityCheck/{members}", produces = MediaType.APPLICATION_JSON_VALUE)
	  public ResponseEntity<List<LocalDate>> getAvailableDates(
	      @RequestParam(name = "arrival_date", required = false)
	      @DateTimeFormat(iso = ISO.DATE) LocalDate arrivalDate,
	      @RequestParam(name = "departure_date", required = false)
	      @DateTimeFormat(iso = ISO.DATE) LocalDate departureDate,
	      @PathVariable() Long members) {

	    if (arrivalDate == null) {
	    	arrivalDate = LocalDate.now().plusDays(1);
	    }
	    if (departureDate == null) {
	    	departureDate = arrivalDate.plusMonths(maxAdvanceBooking);
	    }
	    List<LocalDate> availableDates = reservationService.findAvailableDates(arrivalDate, departureDate,members);
	    return new ResponseEntity<>(availableDates, HttpStatus.OK);
	  }

	 @GetMapping(value = "/checkReservationDetails/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	  public ResponseEntity<Object> getBooking(@PathVariable() Long id) {
         try {
		 Reservation reservation = reservationService.findReservationByBookingIdentifier(id);
	    return new ResponseEntity<>(reservation, HttpStatus.OK);
         }
         catch(Exception e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
         }
	  }
	 @PostMapping("/makeReservation")
		public ResponseEntity<Object> createBooking(@RequestBody Reservation reservation) {
			try {
				Reservation result=reservationService.createReservation(reservation);
				return ResponseEntity.status(HttpStatus.CREATED).body(result);			
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
			}
		}
	 @PutMapping(value = "/modifyReservation/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	  public ResponseEntity<Object> updateBooking(@PathVariable Long id, @RequestBody Reservation reservation) {

		 try {
			 Reservation result = reservationService.updateReservation(id,reservation);
		    return new ResponseEntity<>(result, HttpStatus.OK);
	         }
	         catch(Exception e) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	         }
	 	}
	 @DeleteMapping("/cancelReservation/{id}")
		public ResponseEntity<Object> cancelBooking(@PathVariable Long id) {
			try {
				reservationService.cancelReservation(id);
				return ResponseEntity.ok().build();
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
			}
		}	
}
