package ar.com.juani.melishows.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

import ar.com.juani.melishows.dao.model.Reservation;
import ar.com.juani.melishows.utils.FormattingConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReservationResponseDto {

    @NotBlank
    Long reservationId;
    @NotBlank
    String reservationName;
    @NotBlank
    String reservationUserId;
    
    @Singular
    List<SeatResponseDto> reservedSeats;
    
    public static ReservationResponseDto fromReservation(Reservation reservation) {
        
        ReservationResponseDtoBuilder dtoBuilder = ReservationResponseDto.builder().reservationId(reservation.getId()).reservationName(reservation.getName()).reservationUserId(reservation.getUserId());
        reservation.getSeats().forEach(seat -> dtoBuilder.reservedSeat(SeatResponseDto.builder().id(seat.getId()).price(FormattingConstants.DECIMAL_FORMAT.format(seat.getSector().getPrice())).seat(seat.getSeat().getName()).build()));
        return dtoBuilder.build();
    }
}