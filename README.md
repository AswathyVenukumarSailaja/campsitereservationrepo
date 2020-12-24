# CAMPSITE RESERVATION API

RESTful webservice managing campsite reservations.

## Back-end Tech Challenge

To streamline the reservations a few constraints need to be in place

* The campsite will be free for all.
* The campsite can be reserved for max 3 days.
* The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance.
* Reservations can be cancelled anytime.
* For sake of simplicity assume the check-in & check-out time is 12:00 AM

## System Requirements

* The users will need to find out when the campsite is available. So the system should expose an API to provide information of the
availability of the campsite for a given date range with the default being 1 month.
* Provide an end point for reserving the campsite. The user will provide his/her email & full name at the time of reserving the campsite
along with intended arrival date and departure date. Return a unique booking identifier back to the caller if the reservation is successful.
* The unique booking identifier can be used to modify or cancel the reservation later on. Provide appropriate end point(s) to allow
modification/cancellation of an existing reservation
* Due to the popularity of the island, there is a high likelihood of multiple users attempting to reserve the campsite for the same/overlapping
date(s). Demonstrate with appropriate test cases that the system can gracefully handle concurrent requests to reserve the campsite.
* Provide appropriate error messages to the caller to indicate the error cases.
* In general, the system should be able to handle large volume of requests for getting the campsite availability.
* There are no restrictions on how reservations are stored as as long as system constraints are not violated.

## Tools and Technologies Used

* Spring Boot 2.4.1
* JDK-1.8
* Hibernate 5.4.25.Final
* JPA
* Maven 4.0.0
* IDE-Spring Tool Suite
* H2 Embedded Database
* Apache Tomcat Server
* Postman(For API testing)
* Apache JMeter(For Concurrency testing)

## Configuration properties 
Following properties are configured in appliation.properties file.

* campsite.max.capacity (Maximum capacity of people in the campsite at a time.Default value set as 10.)
* campsite.max.advanceBookingMonths (Advanced reservation possible in months.Also,the same value is taken while displaying the dates available for month.Default value set as 1.)
* campsite.max.reservedDays (Maximum days reserving the campsite.Default value set as 3.)

## Application Run steps
1. git clone https://github.com/AswathyVenukumarSailaja/campsitereservationrepo.git
2. cd campsitereservationrepo
3. mvn spring-boot:run

OR

1. Download the source code from repository and import in IDE.
2. Right click on project-->Run As-->Java Application

## URL's to Access
* http://localhost:8080/campsite/reservations/availabilityCheck/{members} 
* http://localhost:8080/campsite/reservations//checkReservationDetails/{id}
* http://localhost:8080/campsite/reservations/makeReservation
* http://localhost:8080/campsite/reservations/modifyReservation/{id}
* http://localhost:8080/campsite/reservations/cancelReservation/{id}

## Testing
Getting available dates,getting the reservation details,making reservation,updating reservation and cancelling reservations API's are tested using Postman
#### Concurrency Testing
Concurrency control is obtained by Pessimistic locking and Spring level transactional isolation annotation as Serializable.
Tested using Apache Jmeter
- Created a response body for posting a reservation detail with 3 members in it.
- Set the thread count as 10 in order to test the concurrent access for available dates.
- First 3 requests are processed and rest of the requests are failed.(since the maximum capacity is set as 10).
* Screenshots attached.
![Screenshot](https://github.com/AswathyVenukumarSailaja/campsitereservationrepo/issues/1#issue-774207994)
