package ar.com.juani.melishows.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModelProperty;
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
@JsonPropertyOrder({"userId", "userName", "reservedSeats"})
public class ReservationDto {

	@ApiModelProperty(position = 0, name = "Person id", example = "30444333")
    @NotBlank
    String userId;

	@ApiModelProperty(position = 1, name = "Person full name", example = "Juan Perez")
    @NotBlank
    String userName;

	@ApiModelProperty(position = 2, name = "List of reserved seat ids", example = "[1, 2, 3]")
    @Singular
    @NotEmpty
    List<Long> reservedSeats;
    
}