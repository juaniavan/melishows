package ar.com.juani.melishows.dto;

import ar.com.juani.melishows.dao.model.Showing;
import ar.com.juani.melishows.utils.FormattingConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ShowingDto {
	
    Long id;
    String location;
    String theater;
    String show;
    String artist;
    String appointment;
    
    public static ShowingDto fromShowing(Showing showing) {
    	
    	return ShowingDto.builder()
    			.id(showing.getId())
    			.artist(showing.getShow().getArtist())
    			.show(showing.getShow().getName())
    			.location(showing.getShow().getTheater().getLocation().getCity())
    			.theater(showing.getShow().getTheater().getName())
    			.appointment(showing.getSchedule().format(FormattingConstants.DATE_TIME))
    			.build();
    }
}
