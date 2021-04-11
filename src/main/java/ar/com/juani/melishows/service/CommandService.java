package ar.com.juani.melishows.service;

import ar.com.juani.melishows.dto.ReservationDto;
import ar.com.juani.melishows.dto.ReservationResponseDto;

public interface CommandService {

    ReservationResponseDto createReservation(ReservationDto reservationDto);

	void resetAvailability();

}
