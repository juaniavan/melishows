package ar.com.juani.melishows.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import ar.com.juani.melishows.dao.repository.ReservationRepository;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:dropSchema.sql")
@Slf4j
class IntegrationTests {

	@Resource
	private MockMvc mockMvc;

	@Resource
	SessionFactory sessionFactory;

	@Resource
	ReservationRepository reservationRepository;

	@Test
	void givenTwoReservationsForTheSameSeatsShowAllowOnlyTheFirst() throws Exception {
		String jsonBody = "{\n" + "    \"userName\": \"jorge\",\n" + "    \"userId\": \"33222111\",\n"
				+ "    \"reservedSeats\": [1, 2]\n" + "}";
		mockMvc.perform(
				MockMvcRequestBuilders.post("/reserve").content(jsonBody).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200));

		mockMvc.perform(
				MockMvcRequestBuilders.post("/reserve").content(jsonBody).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(409));
	}

	@Test
	void givenShowSearchShouldReturnThreeResultsOrderedByArtistDesc() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/shows").param("orderBy", "artist").param("sortDir", "desc"))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].theater", is("Teatro Gran Rex")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].artist", is("Mateo Sujatovich")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].location", is("Capital Federal")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].show", is("Conociendo Rusia")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].id", is(3)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].id", is(2)));
	}

	@Test
	void givenShowingSearchShouldReturnOneResult() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/showings").param("artist", "luciano"))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(4)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].theater", is("Teatro Opera")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].artist", is("Luciano Pereyra")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].location", is("Capital Federal")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].show",
						is("Luciano Pereyra en Buenos Aires desde el Teatro Opera")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].appointment", is("30-04-2021 21:00")));
	}

	@Test
	void givenShowingSearchBetweenDatesReturnOneResult() throws Exception {

		mockMvc.perform(
				MockMvcRequestBuilders.get("/showings").param("dateFrom", "30-04-2021").param("dateTo", "30-04-2021"))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(4)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].theater", is("Teatro Opera")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].artist", is("Luciano Pereyra")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].location", is("Capital Federal")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].show",
						is("Luciano Pereyra en Buenos Aires desde el Teatro Opera")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].appointment", is("30-04-2021 21:00")));
	}

	@Test
	void givenShowingSearchShowReturn200() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/showings/1")).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", is(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.theater", is("Teatro Gran Rex")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.artist", is("Mateo Sujatovich")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.location", is("Capital Federal")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.show", is("Conociendo Rusia")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.appointment", is("24-04-2021 20:30")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.availability").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.availability[0].id", is(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.availability[0].sector", is("VIP M&G")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.availability[0].seat", is("A1")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.availability[0].price", is("10000,00")));
	}

	@Test
	void givenShowingSearchWithNotExistingIdShowReturn404() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/showings/1553")).andExpect(MockMvcResultMatchers.status().is(404));
	}

	@Test
	void givenThreeCallsToTheSameFindShowingShouldUseCacheForSecondAndThirdCall() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/showings/1")).andExpect(MockMvcResultMatchers.status().is(200));

		long hitCount1 = sessionFactory.getStatistics().getSecondLevelCacheHitCount();
		long putCount1 = sessionFactory.getStatistics().getSecondLevelCachePutCount();

		log.info("2nd level cache hit count: {}", hitCount1);
		log.info("2nd level cache put count: {}", putCount1);

		mockMvc.perform(MockMvcRequestBuilders.get("/showings/1")).andExpect(MockMvcResultMatchers.status().is(200));

		long hitCount2 = sessionFactory.getStatistics().getSecondLevelCacheHitCount();
		long putCount2 = sessionFactory.getStatistics().getSecondLevelCachePutCount();

		log.info("2nd level cache hit count: {}", hitCount2);
		log.info("2nd level cache put count: {}", putCount2);

		// second time should get data from cache and not adding anything new as its the
		// same query
		assertTrue(hitCount2 > 0);
		assertEquals(putCount1, putCount2);

		mockMvc.perform(MockMvcRequestBuilders.get("/showings/1")).andExpect(MockMvcResultMatchers.status().is(200));

		long hitCount3 = sessionFactory.getStatistics().getSecondLevelCacheHitCount();
		long putCount3 = sessionFactory.getStatistics().getSecondLevelCachePutCount();

		log.info("2nd level cache hit count: {}", hitCount3);
		log.info("2nd level cache put count: {}", putCount3);

		// third time should get data from cache and double hits from previous call with
		// no adding anything new
		assertEquals(hitCount2 * 2, hitCount3);
		assertEquals(putCount1, putCount3);
	}

	@Test
	void givenASearchAndAFindShowingShouldUseCacheForSecondCall() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/showings").param("artist", "luciano"));

		long hitCount1 = sessionFactory.getStatistics().getSecondLevelCacheHitCount();
		long putCount1 = sessionFactory.getStatistics().getSecondLevelCachePutCount();

		log.info("2nd level cache hit count: {}", hitCount1);
		log.info("2nd level cache put count: {}", putCount1);

		// first time should put all data in cache
		assertEquals(0, hitCount1);
		assertTrue(putCount1 > 0);

		mockMvc.perform(MockMvcRequestBuilders.get("/showings/4")).andExpect(MockMvcResultMatchers.status().is(200));

		long hitCount2 = sessionFactory.getStatistics().getSecondLevelCacheHitCount();
		long putCount2 = sessionFactory.getStatistics().getSecondLevelCachePutCount();

		log.info("2nd level cache hit count: {}", hitCount2);
		log.info("2nd level cache put count: {}", putCount2);

		// second time should get data from cache and could add something as it is a new
		// call
		assertTrue(hitCount2 > 0);
		assertTrue(putCount2 >= putCount1);

		mockMvc.perform(MockMvcRequestBuilders.get("/showings/4")).andExpect(MockMvcResultMatchers.status().is(200));

		long hitCount3 = sessionFactory.getStatistics().getSecondLevelCacheHitCount();
		long putCount3 = sessionFactory.getStatistics().getSecondLevelCachePutCount();

		log.info("2nd level cache hit count: {}", hitCount3);
		log.info("2nd level cache put count: {}", putCount3);

		// third time should get data from cache and increase hits from previous call
		// with no adding anything new
		assertTrue(hitCount3 > hitCount2);
		assertEquals(putCount3, putCount2);
	}

	@Test
	void givenThreeCallsToTheSameShowingQueryShouldUseCacheForSecondAndThirdCall() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/showings").param("artist", "luciano"));

		long hitCount1 = sessionFactory.getStatistics().getSecondLevelCacheHitCount();
		long putCount1 = sessionFactory.getStatistics().getSecondLevelCachePutCount();

		log.info("2nd level cache hit count: {}", hitCount1);
		log.info("2nd level cache put count: {}", putCount1);

		// first time should put all data in cache
		assertEquals(0, hitCount1);
		assertTrue(putCount1 > 0);

		mockMvc.perform(MockMvcRequestBuilders.get("/showings").param("artist", "luciano"));

		long hitCount2 = sessionFactory.getStatistics().getSecondLevelCacheHitCount();
		long putCount2 = sessionFactory.getStatistics().getSecondLevelCachePutCount();

		log.info("2nd level cache hit count: {}", hitCount2);
		log.info("2nd level cache put count: {}", putCount2);

		// second time should get data from cache and not adding anything new as its the
		// same query
		assertTrue(hitCount2 > 0);
		assertEquals(putCount1, putCount2);

		mockMvc.perform(MockMvcRequestBuilders.get("/showings").param("artist", "luciano"));

		long hitCount3 = sessionFactory.getStatistics().getSecondLevelCacheHitCount();
		long putCount3 = sessionFactory.getStatistics().getSecondLevelCachePutCount();

		log.info("2nd level cache hit count: {}", hitCount3);
		log.info("2nd level cache put count: {}", putCount3);

		// third time should get data from cache and double hits from previous call with
		// no adding anything new
		assertEquals(hitCount2 * 2, hitCount3);
		assertEquals(putCount1, putCount3);
	}

	@Test
	void givenThreeCallsToTheSameShowQueryShouldUseCacheForSecondAndThirdCall() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/shows").param("artist", "mateo"));

		long hitCount1 = sessionFactory.getStatistics().getSecondLevelCacheHitCount();
		long putCount1 = sessionFactory.getStatistics().getSecondLevelCachePutCount();

		log.info("2nd level cache hit count: {}", hitCount1);
		log.info("2nd level cache put count: {}", putCount1);

		// first time should put all data in cache
		assertEquals(0, hitCount1);
		assertTrue(putCount1 > 0);

		mockMvc.perform(MockMvcRequestBuilders.get("/shows").param("artist", "mateo"));

		long hitCount2 = sessionFactory.getStatistics().getSecondLevelCacheHitCount();
		long putCount2 = sessionFactory.getStatistics().getSecondLevelCachePutCount();

		log.info("2nd level cache hit count: {}", hitCount2);
		log.info("2nd level cache put count: {}", putCount2);

		// second time should get data from cache and not adding anything new as its the
		// same query
		assertTrue(hitCount2 > 0);
		assertEquals(putCount1, putCount2);

		mockMvc.perform(MockMvcRequestBuilders.get("/shows").param("artist", "mateo"));

		long hitCount3 = sessionFactory.getStatistics().getSecondLevelCacheHitCount();
		long putCount3 = sessionFactory.getStatistics().getSecondLevelCachePutCount();

		log.info("2nd level cache hit count: {}", hitCount3);
		log.info("2nd level cache put count: {}", putCount3);

		// third time should get data from cache and double hits from previous call with
		// no adding anything new
		assertEquals(hitCount2 * 2, hitCount3);
		assertEquals(putCount1, putCount3);
	}

	@Disabled("just for manual tests")
	@Test
	void testConcurrencyDifferentSeatsManually() throws Exception {
		String jsonBody1 = "{\n" + "    \"userName\": \"jorge\",\n" + "    \"userId\": \"33222111\",\n"
				+ "    \"reservedSeats\": [1, 2]\n" + "}";
		String jsonBody2 = "{\n" + "    \"userName\": \"jorge\",\n" + "    \"userId\": \"33222111\",\n"
				+ "    \"reservedSeats\": [3, 4]\n" + "}";

		ExecutorService threadPool = Executors.newFixedThreadPool(5);
		threadPool.execute(() -> {
			try {
				mockMvc.perform(MockMvcRequestBuilders.post("/reserve").content(jsonBody1)
						.contentType(MediaType.APPLICATION_JSON));
			} catch (Exception e) {
				fail("not expected result");
			}
		});
		threadPool
				.execute(() -> {
					try {
						mockMvc
								.perform(MockMvcRequestBuilders.post("/reserve").content(jsonBody2)
										.contentType(MediaType.APPLICATION_JSON));
					} catch (Exception e) {
						fail("not expected result");
					}
				});
		threadPool
		.execute(() -> {
			try {
				mockMvc
				.perform(MockMvcRequestBuilders.get("/showings/1"));
			} catch (Exception e) {
				fail("not expected result");
			}
		});
		threadPool.shutdown();
		threadPool.awaitTermination(1, TimeUnit.SECONDS);

		assertEquals(2, reservationRepository.count());
	}

	@Disabled("just for manual tests")
	@Test
	void testConcurrencySameSeatLockManually() throws Exception {
		String jsonBody1 = "{\n" + "    \"userName\": \"jorge\",\n" + "    \"userId\": \"33222111\",\n"
				+ "    \"reservedSeats\": [1, 2]\n" + "}";
		String jsonBody2 = "{\n" + "    \"userName\": \"jorge\",\n" + "    \"userId\": \"33222111\",\n"
				+ "    \"reservedSeats\": [2, 4]\n" + "}";
		
		ExecutorService threadPool = Executors.newFixedThreadPool(5);
		threadPool.execute(() -> {
			try {
				mockMvc.perform(MockMvcRequestBuilders.post("/reserve").content(jsonBody1)
						.contentType(MediaType.APPLICATION_JSON));
			} catch (Exception e) {
				fail("not expected result");
			}
		});
		threadPool
		.execute(() -> {
			try {
				mockMvc
				.perform(MockMvcRequestBuilders.post("/reserve").content(jsonBody2)
						.contentType(MediaType.APPLICATION_JSON));
			} catch (Exception e) {
				fail("not expected result");
			}
		});
		threadPool
		.execute(() -> {
			try {
				mockMvc
				.perform(MockMvcRequestBuilders.get("/showings/1"));
			} catch (Exception e) {
				fail("not expected result");
			}
		});
		threadPool.shutdown();
		threadPool.awaitTermination(1, TimeUnit.SECONDS);
		
		assertEquals(1, reservationRepository.count());
	}
}