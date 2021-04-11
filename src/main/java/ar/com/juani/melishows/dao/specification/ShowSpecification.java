package ar.com.juani.melishows.dao.specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import ar.com.juani.melishows.dao.model.Location;
import ar.com.juani.melishows.dao.model.Location_;
import ar.com.juani.melishows.dao.model.Sector;
import ar.com.juani.melishows.dao.model.Sector_;
import ar.com.juani.melishows.dao.model.Show;
import ar.com.juani.melishows.dao.model.Show_;
import ar.com.juani.melishows.dao.model.Showing;
import ar.com.juani.melishows.dao.model.Showing_;
import ar.com.juani.melishows.dao.model.Theater;
import ar.com.juani.melishows.dao.model.Theater_;
import ar.com.juani.melishows.dto.SearchQueryDto;

public class ShowSpecification implements Specification<Show> {

    private static final long serialVersionUID = -7524103681864684451L;
    private transient SearchQueryDto showSearchQuery = null;

    public ShowSpecification(SearchQueryDto showSearchQuery) {
        this.showSearchQuery = showSearchQuery;
    }

    @Override
    public Predicate toPredicate(Root<Show> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<>();
        ListJoin<Show, Showing> showings = null;

        Path<String> showName = root.get(Show_.name);
        Path<String> artistName = root.get(Show_.artist);
        showSearchQuery.getShowName().ifPresent(show -> predicates.add(cb.like(cb.lower(showName), String.join("", "%", show.toLowerCase(), "%"))));
        showSearchQuery.getArtistName().ifPresent(artist -> predicates.add(cb.like(cb.lower(artistName), String.join("", "%", artist.toLowerCase(), "%"))));

        //if it has theater filters adds that join
        if (showSearchQuery.hasTheaterFilters()) {
            Join<Show, Theater> theaterJoin = applyTheaterFilters(root, cb, predicates);
            //if it has location filters adds that join
            applyLocationFilters(cb, predicates, theaterJoin);
        }

        //if it has showing filters adds that join
        showings = applyShowingFilters(root, cb, predicates, showings);
        
        //if it has price filters adds that join
        applyPriceFilters(root, cb, predicates, showings);

        //eliminate duplicated shows
        query.distinct(true);
        
        return cb.and(predicates.stream().toArray(Predicate[]::new));
    }

	private void applyPriceFilters(Root<Show> root, CriteriaBuilder cb, List<Predicate> predicates,
			ListJoin<Show, Showing> showings) {
		if (showSearchQuery.hasSectorFilters()) {
            if (showings == null) {
                showings = root.join(Show_.showings);
            }
            ListJoin<Showing, Sector> sectors = showings.join(Showing_.sectors);
            showSearchQuery.getPriceFrom().ifPresent(priceFrom -> predicates.add(cb.greaterThanOrEqualTo(sectors.get(Sector_.price), priceFrom)));
            showSearchQuery.getPriceTo().ifPresent(priceTo -> predicates.add(cb.lessThanOrEqualTo(sectors.get(Sector_.price), priceTo)));
        }
	}

	private ListJoin<Show, Showing> applyShowingFilters(Root<Show> root, CriteriaBuilder cb, List<Predicate> predicates,
			ListJoin<Show, Showing> showings) {
		if (showSearchQuery.hasShowingFilters()) {
            showings = root.join(Show_.showings);
            Path<LocalDateTime> schedule = showings.get(Showing_.schedule);
            showSearchQuery.getShowingFrom().ifPresent(from -> predicates.add(cb.greaterThanOrEqualTo(schedule, from)));
            showSearchQuery.getShowingTo().ifPresent(to -> predicates.add(cb.lessThanOrEqualTo(schedule, to)));
        }
		return showings;
	}

	private void applyLocationFilters(CriteriaBuilder cb, List<Predicate> predicates, Join<Show, Theater> theaterJoin) {
		showSearchQuery.getLocationName().ifPresent(locationName -> {
		    String locationSearch = locationName.toLowerCase();
		    Path<Location> location = theaterJoin.join(Theater_.location);
		    predicates.add(cb.or(cb.like(cb.lower(location.get(Location_.city)), String.join("", "%", locationSearch, "%")),
		            cb.like(cb.lower(location.get(Location_.province)), String.join("", "%", locationSearch, "%")),
		            cb.like(cb.lower(location.get(Location_.country)), String.join("", "%", locationSearch, "%"))));
		});
	}

	private Join<Show, Theater> applyTheaterFilters(Root<Show> root, CriteriaBuilder cb, List<Predicate> predicates) {
		Join<Show, Theater> theaterJoin = root.join(Show_.theater);

		showSearchQuery.getTheaterName().ifPresent(theater -> {
		    Path<String> theaterName = theaterJoin.get(Theater_.name);
		    predicates.add(cb.like(cb.lower(theaterName), String.join("", "%", theater.toLowerCase(), "%")));
		});
		return theaterJoin;
	}
}