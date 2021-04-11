package ar.com.juani.melishows.dto;

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
public class AvailabilityDetailDto {
    
    Long id;
    String sector;
    String seat;
    String price;

}
