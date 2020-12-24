package com.challenge.campsitereservationapi.model;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.sun.istack.NotNull;


@Entity
@Table(name = "reservation")
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
	private String email;
	
	@NotNull
	private String fullName;
	
	@NotNull
	private LocalDate arrivalDate;
	
	@NotNull
	private LocalDate departureDate;
	
	@NotNull
	private int members;
	
	private String status="ACTIVE";
	
	@CreationTimestamp
	private LocalDate createdAt;
	
	@CreationTimestamp
	private LocalDate updatedAt; 

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public LocalDate getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(LocalDate arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public LocalDate getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(LocalDate departureDate) {
		this.departureDate = departureDate;
	}

	public int getMembers() {
		return members;
	}

	public void setMembers(int members) {
		this.members = members;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDate getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDate updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * returns the list of dates in between the start date and end date
	 * 
	 */
	public List<LocalDate> getReservedDates() {
	    return this.arrivalDate.datesUntil(this.departureDate).collect(Collectors.toList());
	  }

	@Override
	public String toString() {
		return "Reservation [id=" + id + ", email=" + email + ", fullName="
				+ fullName + ", arrivalDate=" + arrivalDate + ", departureDate=" + departureDate + ", members="
				+ members + ", status=" + status + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
}
