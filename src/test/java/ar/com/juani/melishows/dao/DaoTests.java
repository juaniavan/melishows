package ar.com.juani.melishows.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import ar.com.juani.melishows.dao.model.Availability;
import ar.com.juani.melishows.dao.model.Reservation;
import ar.com.juani.melishows.dao.model.Show;
import ar.com.juani.melishows.dao.repository.AvailabilityRepository;
import ar.com.juani.melishows.dao.repository.ReservationRepository;
import ar.com.juani.melishows.dao.repository.ShowRepository;
import ar.com.juani.melishows.dao.repository.ShowingRepository;
import ar.com.juani.melishows.dao.specification.ShowOrderBy;
import ar.com.juani.melishows.dao.specification.ShowSpecification;
import ar.com.juani.melishows.dao.specification.ShowingSpecification;
import ar.com.juani.melishows.dto.ReservationDto;
import ar.com.juani.melishows.dto.SearchQueryDto;
import ar.com.juani.melishows.dto.ShowDto;
import ar.com.juani.melishows.service.CommandService;
import ar.com.juani.melishows.service.QueryService;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:dropSchema.sql")
class DaoTests {

	@Resource
	CommandService commandService;

	@Resource
	QueryService queryService;

	@Resource
	private AvailabilityRepository availabilityRepository;

	@Resource
	private ShowRepository showRepository;

	@Resource
	private ShowingRepository showingRepository;

	@Resource
	private ReservationRepository reservationRepository;

	@Test
	void givenThreeReservationsForTheSameTwoSeatsShouldAllowOnlyOneReservation() throws InterruptedException {

		Long seat1 = 1L;
		Long seat2 = 2L;

		ReservationDto reservation1 = ReservationDto.builder().userName("juan").userId("11111111").reservedSeat(seat1)
				.reservedSeat(seat2).build();
		ReservationDto reservation2 = ReservationDto.builder().userName("pedro").userId("22222222").reservedSeat(seat1)
				.reservedSeat(seat2).build();
		ReservationDto reservation3 = ReservationDto.builder().userName("pablo").userId("33333333").reservedSeat(seat1)
				.reservedSeat(seat2).build();

		ExecutorService threadPool = Executors.newFixedThreadPool(3);
		threadPool.execute(() -> commandService.createReservation(reservation1));
		threadPool.execute(() -> commandService.createReservation(reservation2));
		threadPool.execute(() -> commandService.createReservation(reservation3));
		threadPool.shutdown();
		threadPool.awaitTermination(5000, TimeUnit.MILLISECONDS);

		Availability availability = availabilityRepository.findById(1L).get();

		assertNotNull(availability);
		assertNotNull(availability.getLastUpdated());
		assertNotNull(availability.getReservation());
		assertTrue(availability.isReserved());

		Availability availability2 = availabilityRepository.findById(2L).get();

		assertNotNull(availability2);
		assertNotNull(availability2.getLastUpdated());
		assertNotNull(availability2.getReservation());
		assertTrue(availability2.isReserved());

		assertEquals(availability.getReservation().getId(), availability2.getReservation().getId());

		assertEquals(2, availabilityRepository.getReservedSeats());
		assertEquals(1, reservationRepository.getReservations());

	}

	@Test
	void givenTwoReservationsThatSharesASeatShouldAllowOnlyOneReservation() throws InterruptedException {

		Long seat1 = 1L;
		Long seat2 = 2L;
		Long seat3 = 3L;

		ReservationDto reservation1 = ReservationDto.builder().userName("juan").userId("11111111").reservedSeat(seat1)
				.reservedSeat(seat2).build();
		ReservationDto reservation2 = ReservationDto.builder().userName("pedro").userId("22222222").reservedSeat(seat1)
				.reservedSeat(seat3).build();

		Set<Long> seatIdsReservation1 = new HashSet<Long>();
		seatIdsReservation1.add(1L);
		seatIdsReservation1.add(2L);

		Set<Long> seatIdsReservation2 = new HashSet<Long>();
		seatIdsReservation2.add(1L);
		seatIdsReservation2.add(3L);

		ExecutorService threadPool = Executors.newFixedThreadPool(3);
		threadPool.execute(() -> commandService.createReservation(reservation1));
		threadPool.execute(() -> commandService.createReservation(reservation2));
		threadPool.shutdown();
		threadPool.awaitTermination(5000, TimeUnit.MILLISECONDS);

		assertEquals(2, availabilityRepository.getReservedSeats());
		assertEquals(1, reservationRepository.getReservations());
	}

	@Test
	void givenTwoReservationsForDifferentASeatsShouldAllowBothReservations() throws InterruptedException {

		Long seat1 = 1L;
		Long seat2 = 2L;
		Long seat3 = 3L;

		ReservationDto reservation1 = ReservationDto.builder().userName("juan").userId("11111111").reservedSeat(seat1)
				.build();
		ReservationDto reservation2 = ReservationDto.builder().userName("pedro").userId("22222222").reservedSeat(seat2)
				.reservedSeat(seat3).build();

		ExecutorService threadPool = Executors.newFixedThreadPool(3);
		threadPool.execute(() -> commandService.createReservation(reservation1));
		threadPool.execute(() -> commandService.createReservation(reservation2));
		threadPool.shutdown();
		threadPool.awaitTermination(5000, TimeUnit.MILLISECONDS);

		assertEquals(3, availabilityRepository.getReservedSeats());
		assertEquals(2, reservationRepository.getReservations());

		Availability availability = availabilityRepository.findById(1L).get();

		assertNotNull(availability);
		assertNotNull(availability.getLastUpdated());
		assertNotNull(availability.getReservation());
		assertTrue(availability.isReserved());

		Reservation reservationResponse = reservationRepository.findById(availability.getReservation().getId()).get();
		assertEquals("11111111", reservationResponse.getUserId());

		Availability availability2 = availabilityRepository.findById(2L).get();

		assertNotNull(availability2);
		assertNotNull(availability2.getLastUpdated());
		assertNotNull(availability2.getReservation());
		assertTrue(availability2.isReserved());

		reservationResponse = reservationRepository.findById(availability2.getReservation().getId()).get();
		assertEquals("22222222", reservationResponse.getUserId());

		availability2 = availabilityRepository.findById(3L).get();

		assertNotNull(availability2);
		assertNotNull(availability2.getLastUpdated());
		assertNotNull(availability2.getReservation());
		assertTrue(availability2.isReserved());

		reservationResponse = reservationRepository.findById(availability2.getReservation().getId()).get();
		assertEquals("22222222", reservationResponse.getUserId());
	}

	@Test
	void givenASearchWithNoFiltersShouldReturnAllShows() {

		ShowSpecification noFiltersSpec = new ShowSpecification(SearchQueryDto.builder().build());
		assertEquals(3, showRepository.count(noFiltersSpec));

	}

	@Test
	void givenASearchWithDatesFiltersShouldReturnOneShow() {

		LocalDateTime from = LocalDate.of(2021, 4, 24).atStartOfDay();
		LocalDateTime to = LocalDate.of(2021, 4, 25).atTime(23, 59);
		SearchQueryDto searchQuery = SearchQueryDto.builder()
				.showingFrom(Optional.of(from))
				.showingTo(Optional.of(to))
				.build();
		ShowSpecification betweenDatesSpec = new ShowSpecification(searchQuery);
		List<Show> shows = showRepository.findAll(betweenDatesSpec, Sort.by(Direction.ASC, "id"));
		assertEquals(1, shows.size());
		assertEquals(Long.valueOf(1), shows.get(0).getId());
	}

	@Test
	void givenASearchWithPriceFiltersShouldReturnOneShow() {

		SearchQueryDto searchQuery = SearchQueryDto.builder().priceFrom(Optional.of(BigDecimal.valueOf((8000))))
				.build();
		List<ShowDto> shows = queryService.findShows(searchQuery);

		assertEquals(1, shows.size());
		assertEquals(Long.valueOf(1), shows.get(0).getId());
	}

	@Test
	void givenASearchWithTheaterNameFilterShouldReturnTwoShows() {

		SearchQueryDto searchQuery = SearchQueryDto.builder().theaterName(Optional.of("per")).build();
		ShowSpecification theaterSpec = new ShowSpecification(searchQuery);
		List<Show> shows = showRepository.findAll(theaterSpec, Sort.by(Direction.ASC, "id"));
		assertEquals(2, shows.size());
		assertEquals(Long.valueOf(2), shows.get(0).getId());
		assertEquals(Long.valueOf(3), shows.get(1).getId());
	}

	@Test
	void givenASearchWithLocationNameFilterShouldReturnFourShowings() {

		SearchQueryDto searchQuery = SearchQueryDto.builder().locationName(Optional.of("caba")).build();
		ShowingSpecification theaterSpec = new ShowingSpecification(searchQuery);
		assertEquals(4, showingRepository.count(theaterSpec));
	}

	@Test
	void givenASearchWithLocationNameFilterShouldReturnThreeShows() {
		SearchQueryDto searchQuery = SearchQueryDto.builder().theaterName(Optional.of("rex"))
				.locationName(Optional.of("caba")).orderBy(Optional.of(ShowOrderBy.LOCATION.getShowAlias())).build();
		List<ShowDto> shows = queryService.findShows(searchQuery);
		assertEquals(1, shows.size());
	}

}