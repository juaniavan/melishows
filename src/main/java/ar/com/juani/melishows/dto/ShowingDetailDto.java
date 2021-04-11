package ar.com.juani.melishows.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import ar.com.juani.melishows.dao.model.Showing;
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
public class ShowingDetailDto {
    
    Long id;
    String location;
    String theater;
    String show;
    String artist;
    String appointment;
    
    @JsonProperty("availability")
    @Singular("availabilityItem")
    List<AvailabilityDetailDto> availabilities;

    
    public static ShowingDetailDto fromShowing(Showing showing) {

    	ShowingDetailDtoBuilder dtoBuilder = ShowingDetailDto.builder()
    			.id(showing.getId())
    			.artist(showing.getShow().getArtist())
    			.show(showing.getShow().getName())
    			.location(showing.getShow().getTheater().getLocation().getCity())
    			.theater(showing.getShow().getTheater().getName())
    			.appointment(FormattingConstants.DATE_TIME.format(showing.getSchedule()));
    	
        showing.getSectors().forEach(sector -> sector.getAvailabilities().stream().filter(a -> !a.isReserved()).forEach(
                a -> dtoBuilder.availabilityItem(AvailabilityDetailDto.builder().id(a.getId()).sector(sector.getName()).price(FormattingConstants.DECIMAL_FORMAT.format(sector.getPrice())).seat(a.getSeat().getName()).build())));

        return dtoBuilder.build();
    }
}
