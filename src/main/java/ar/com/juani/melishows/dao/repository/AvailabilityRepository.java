package ar.com.juani.melishows.dao.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import ar.com.juani.melishows.dao.model.Availability;
import ar.com.juani.melishows.dao.model.Reservation;

public interface AvailabilityRepository extends PagingAndSortingRepository<Availability, Long>, JpaSpecificationExecutor<Availability> {

    @Query("SELECT COUNT(*) FROM Availability av WHERE av.reserved = true")
    int getReservedSeats();

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Availability av SET av.reserved = true, av.lastUpdated = NOW(), av.reservation = :reservation WHERE av.reserved = false and av.id in (:seats) ")
    int reserveSeats(@Param("reservation") Reservation reservation, @Param("seats") Set<Long> seatIdsToReserve);

    @Modifying
    @Query("UPDATE Availability av SET av.reserved = false, av.lastUpdated = null, av.reservation = null ")
	void resetAvailability();
}