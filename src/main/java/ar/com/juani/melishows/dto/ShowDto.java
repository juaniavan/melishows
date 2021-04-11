package ar.com.juani.melishows.dto;

import ar.com.juani.melishows.dao.model.Show;
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
public class ShowDto {
    
    Long id;
    String location;
    String theater;
    String show;
    String artist;

    
    public static ShowDto fromShow(Show show) {
    	
    	return ShowDto.builder().id(show.getId()).artist(show.getArtist()).show(show.getName())
				.location(show.getTheater().getLocation().getCity()).theater(show.getTheater().getName())
				.build();
    }

}
