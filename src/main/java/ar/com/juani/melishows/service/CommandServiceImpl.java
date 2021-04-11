package ar.com.juani.melishows.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.stereotype.Service;

import ar.com.juani.melishows.dao.model.Reservation;
import ar.com.juani.melishows.dao.repository.AvailabilityRepository;
import ar.com.juani.melishows.dao.repository.ReservationRepository;
import ar.com.juani.melishows.dto.ReservationDto;
import ar.com.juani.melishows.dto.ReservationResponseDto;
import ar.com.juani.melishows.exception.NotAvailableSeatsException;
import ar.com.juani.melishows.exception.NotAvailableSeatsLockException;

@Service
public class CommandServiceImpl implements CommandService {

	@Autowired
	ReservationRepository reservationRepository;

	@Autowired
	AvailabilityRepository availabilityRepository;

	// TODO when lock timeout exception is thrown H2 db releases the connection and
	// causes the rollback to fail. As a workaround Im catching the exception and
	// treating it as "dontRollBack" (the session was already released anyway).
	// For productive release a db implementation review must be done and this need to be fixed
	@Transactional(dontRollbackOn = NotAvailableSeatsLockException.class)
	public ReservationResponseDto createReservation(ReservationDto reservationDto) {

		//reserving seats
		Reservation reservation = reserveSeats(reservationDto);

		// refreshing reservation with reserved seats
		Optional<Reservation> refreshedReservation = reservationRepository.findById(reservation.getId());

		if (refreshedReservation.isPresent()) {
		return ReservationResponseDto.fromReservation(refreshedReservation.get());
		} else {
			throw new NotAvailableSeatsException("Reservation could not be created");
		}
	}

	private Reservation reserveSeats(ReservationDto reservationDto) {
		// saves the reservation
		Reservation reservation = Reservation.builder().name(reservationDto.getUserName())
				.userId(reservationDto.getUserId()).build();
		Set<Long> seatsToReserve = reservationDto.getReservedSeats().stream().collect(Collectors.toSet());

		//if a lock timeout from the db is received, exception is treated
		int result;
		try {
			reservationRepository.saveAndFlush(reservation);
			result = availabilityRepository.reserveSeats(reservation, seatsToReserve);
		} catch (PessimisticLockingFailureException e) {
			throw new NotAvailableSeatsLockException(
					"Could NOT reserve all the seats. Any/Some of them where NOT available.", e);
		}

		// if current reserved seats are less than provided we throw a custom exception
		if (result != seatsToReserve.size()) {
			throw new NotAvailableSeatsException(
					"Could NOT reserve all the seats. Any/Some of them where NOT available.");
		}
		return reservation;
	}

	@Transactional
	public void resetAvailability() {
		availabilityRepository.resetAvailability();
	}
}