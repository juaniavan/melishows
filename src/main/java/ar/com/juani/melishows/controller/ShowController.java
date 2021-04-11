package ar.com.juani.melishows.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.com.juani.melishows.dao.specification.ShowOrderBy;
import ar.com.juani.melishows.dao.specification.SortDirection;
import ar.com.juani.melishows.dto.ReservationDto;
import ar.com.juani.melishows.dto.ReservationResponseDto;
import ar.com.juani.melishows.dto.SearchQueryDto;
import ar.com.juani.melishows.dto.SearchQueryDto.SearchQueryDtoBuilder;
import ar.com.juani.melishows.dto.ShowDto;
import ar.com.juani.melishows.dto.ShowingDetailDto;
import ar.com.juani.melishows.dto.ShowingDto;
import ar.com.juani.melishows.service.CommandService;
import ar.com.juani.melishows.service.QueryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ShowController {

	@Autowired
	CommandService commandService;

	@Autowired
	QueryService queryService;

	@ApiOperation(value = "Search for shows")
	@ApiResponses({ @ApiResponse(code = 200, message = "List of shows that match the filters"), @ApiResponse(code = 400, message = "For invalid search parameters") })
	@GetMapping(value = "/shows")
	public ResponseEntity<List<ShowDto>> findShows(@ApiParam(value = "Location", example = "Capital Federal") @RequestParam(required = false) Optional<String> location,
			@ApiParam(value = "Theater") @RequestParam(required = false) Optional<String> theater, @ApiParam(value = "Show name") @RequestParam(required = false) Optional<String> show,
			@ApiParam(value = "Artist name") @RequestParam(required = false) Optional<String> artist,
			@ApiParam(value = "From date (dd-MM-yyyy)", example = "01-01-2021") @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") @Valid Optional<LocalDate> dateFrom,
			@ApiParam(value = "To date (dd-MM-yyyy)", example = "02-01-2021") @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") @Valid Optional<LocalDate> dateTo,
			@ApiParam("Price from") @RequestParam(required = false) @Min(0) Optional<Long> priceFrom, @ApiParam("Price up to") @RequestParam(required = false) @Min(0) Optional<Long> priceTo,
			@ApiParam("Order by") @RequestParam(required = false) Optional<ShowOrderBy> orderBy, @ApiParam("Sort Direction") @RequestParam(required = false) SortDirection sortDir) {

		SearchQueryDto showSearchQuery = buildSearchQuery(location, theater, show, artist, dateFrom, dateTo, priceFrom, priceTo, orderBy, sortDir, true);

		log.debug("find shows with params: {}", showSearchQuery);

		List<ShowDto> findShows = queryService.findShows(showSearchQuery);

		log.debug("find shows executed successfully");
		return ResponseEntity.status(HttpStatus.OK).body(findShows);
	}

	@ApiOperation(value = "Search for showings")
	@ApiResponses({ @ApiResponse(code = 200, message = "List of showings that match the filters"), @ApiResponse(code = 400, message = "For invalid search parameters") })
	@GetMapping(value = "/showings")
	public ResponseEntity<List<ShowingDto>> findShowings(@ApiParam(value = "Location", example = "Capital Federal") @RequestParam(required = false) Optional<String> location,
			@ApiParam(value = "Theater") @RequestParam(required = false) Optional<String> theater, @ApiParam(value = "Show name") @RequestParam(required = false) Optional<String> show,
			@ApiParam(value = "Artist name") @RequestParam(required = false) Optional<String> artist,
			@ApiParam(value = "From date (dd-MM-yyyy)", example = "01-01-2021") @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") @Valid Optional<LocalDate> dateFrom,
			@ApiParam(value = "To date (dd-MM-yyyy)", example = "02-01-2021") @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") @Valid Optional<LocalDate> dateTo,
			@ApiParam("Price from") @RequestParam(required = false) @Min(0) Optional<Long> priceFrom, @ApiParam("Price up to") @RequestParam(required = false) @Min(0) Optional<Long> priceTo,
			@ApiParam("Order by") @RequestParam(required = false) Optional<ShowOrderBy> orderBy, @ApiParam("Sort Direction") @RequestParam(required = false) SortDirection sortDir) {

		SearchQueryDto showSearchQuery = buildSearchQuery(location, theater, show, artist, dateFrom, dateTo, priceFrom, priceTo, orderBy, sortDir, false);

		log.debug("find showings with params: {}", showSearchQuery);

		List<ShowingDto> findShows = queryService.findShowings(showSearchQuery);

		log.debug("find showings executed successfully");
		return ResponseEntity.status(HttpStatus.OK).body(findShows);
	}

	@ApiOperation(value = "Look for a showing")
	@ApiResponses({ @ApiResponse(code = 200, message = "The showing detail"), @ApiResponse(code = 400, message = "For invalid path id"), @ApiResponse(code = 404, message = "If the showing does not exist") })
	@GetMapping(value = "/showings/{id}")
	public ResponseEntity<ShowingDetailDto> findShowing(@PathVariable Long id) {

		log.debug("find showing with id: {}", id);

		ShowingDetailDto showingDto = queryService.findShowing(id);

		log.debug("find showings executed with result {}", showingDto);

		return ResponseEntity.status(HttpStatus.OK).body(showingDto);
	}

	@ApiOperation(value = "Reserve seats for a showing")
	@ApiResponses({ @ApiResponse(code = 200, message = "The reservation could be done"), @ApiResponse(code = 400, message = "For invalid body request"),
			@ApiResponse(code = 409, message = "If the reservation could not be done") })
	@PostMapping(value = "/reserve", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ReservationResponseDto> reserve(@Valid @RequestBody final ReservationDto reservationDto) {
		log.debug("reservation request body: {}", reservationDto);

		ReservationResponseDto reservationResponse = commandService.createReservation(reservationDto);

		log.debug("reservation generated successfully: {}", reservationResponse);
		return ResponseEntity.status(HttpStatus.OK).body(reservationResponse);
	}

	@ApiOperation(value = "Reset database for testing porpouses")
	@ApiResponse(code = 200, message = "The reset could be done")
	@PutMapping(value = "/reset")
	public ResponseEntity<Void> resetAvailability() {
		log.debug("reseting availability");

		commandService.resetAvailability();

		log.debug("reseting availability executed succesfully");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	private SearchQueryDto buildSearchQuery(Optional<String> location, Optional<String> theater, Optional<String> show, Optional<String> artist, Optional<LocalDate> dateFrom, Optional<LocalDate> dateTo,
			Optional<Long> priceFrom, Optional<Long> priceTo, Optional<ShowOrderBy> orderBy, SortDirection sortDir, boolean isShow) {

		SearchQueryDtoBuilder builder = SearchQueryDto.builder().locationName(location).theaterName(theater).showName(show).artistName(artist).sortDesc(SortDirection.DESC.equals(sortDir));

		dateFrom.ifPresent(from -> 
			builder.showingFrom(Optional.of(from.atStartOfDay()))
		);
		dateTo.ifPresent(to -> 
			builder.showingTo(Optional.of(to.atTime(23, 59)))
		);
		if (isShow) {
			orderBy.ifPresent(order -> builder.orderBy(Optional.ofNullable(order.getShowAlias())));
		} else {
			orderBy.ifPresent(order -> builder.orderBy(Optional.ofNullable(order.getShowingAlias())));

		}
		priceFrom.ifPresent(price -> builder.priceFrom(Optional.ofNullable(BigDecimal.valueOf(price))));
		priceTo.ifPresent(price -> builder.priceTo(Optional.ofNullable(BigDecimal.valueOf(price))));

		return builder.build();
	}

}