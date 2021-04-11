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

public class ShowingSpecification implements Specification<Showing> {

    private static final long serialVersionUID = -7524103681864684451L;
    private transient SearchQueryDto searchQuery = null;

    public ShowingSpecification(SearchQueryDto showingSearchQuery) {
        this.searchQuery = showingSearchQuery;
    }

    @Override
    public Predicate toPredicate(Root<Showing> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<>();

        Path<LocalDateTime> schedule = root.get(Showing_.schedule);
        searchQuery.getShowingFrom().ifPresent(from -> predicates.add(cb.greaterThanOrEqualTo(schedule, from)));
        searchQuery.getShowingTo().ifPresent(to -> predicates.add(cb.lessThanOrEqualTo(schedule, to)));
        
        //join just for queries that need to filter by show attributes
        if (searchQuery.hasShowFilters()) {
        	
            Join<Showing, Show> showRoot = applyShowFilters(root, cb, predicates);

            //join just for queries that need to filter by theater attributes
            if (searchQuery.hasTheaterFilters()) {
                Join<Show, Theater> theaterJoin = applyTheaterFilters(cb, predicates, showRoot);

                applyLocationFilters(cb, predicates, theaterJoin);
            }
        }
        
        applyPriceFilters(root, cb, predicates);

        query.distinct(true);
        return cb.and(predicates.stream().toArray(Predicate[]::new));
    }

    
	private void applyLocationFilters(CriteriaBuilder cb, List<Predicate> predicates, Join<Show, Theater> theaterJoin) {
		searchQuery.getLocationName().ifPresent(locationName -> {
		    String locationSearch = locationName.toLowerCase();
		    Path<Location> location = theaterJoin.join(Theater_.location);
		    Path<String> city = location.get(Location_.city);
		    predicates.add(
		            cb.or(cb.like(cb.lower(city), String.join("", "%", locationSearch, "%")), cb.like(cb.lower(location.get(Location_.province)), String.join("", "%", locationSearch, "%")),
		                    cb.like(cb.lower(location.get(Location_.country)), String.join("", "%", locationSearch, "%"))));
		});
	}

	private Join<Show, Theater> applyTheaterFilters(CriteriaBuilder cb, List<Predicate> predicates,
			Join<Showing, Show> showRoot) {
		Join<Show, Theater> theaterJoin = showRoot.join(Show_.theater);

		searchQuery.getTheaterName().ifPresent(theater -> {
		    Path<String> theaterName = theaterJoin.get(Theater_.name);
		    predicates.add(cb.like(cb.lower(theaterName), String.join("", "%", theater.toLowerCase(), "%")));
		});
		return theaterJoin;
	}

	private Join<Showing, Show> applyShowFilters(Root<Showing> root, CriteriaBuilder cb, List<Predicate> predicates) {
		Join<Showing, Show> showRoot = root.join(Showing_.show);
		Path<String> showName = showRoot.get(Show_.name);
		Path<String> artistName = showRoot.get(Show_.artist);

		searchQuery.getShowName().ifPresent(show -> predicates.add(cb.like(cb.lower(showName), String.join("", "%", show.toLowerCase(), "%"))));
		searchQuery.getArtistName().ifPresent(artist -> predicates.add(cb.like(cb.lower(artistName), String.join("", "%", artist.toLowerCase(), "%"))));
		return showRoot;
	}

	private void applyPriceFilters(Root<Showing> root, CriteriaBuilder cb, List<Predicate> predicates) {
		if (searchQuery.hasSectorFilters()) {
            ListJoin<Showing, Sector> sectors = root.join(Showing_.sectors);
            searchQuery.getPriceFrom().ifPresent(priceFrom -> predicates.add(cb.greaterThanOrEqualTo(sectors.get(Sector_.price), priceFrom)));
            searchQuery.getPriceTo().ifPresent(priceTo -> predicates.add(cb.lessThanOrEqualTo(sectors.get(Sector_.price), priceTo)));
        }
	}
}