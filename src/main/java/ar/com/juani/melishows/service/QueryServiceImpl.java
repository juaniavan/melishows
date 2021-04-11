package ar.com.juani.melishows.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ar.com.juani.melishows.dao.model.Show;
import ar.com.juani.melishows.dao.model.Showing;
import ar.com.juani.melishows.dao.repository.ShowRepository;
import ar.com.juani.melishows.dao.repository.ShowingRepository;
import ar.com.juani.melishows.dao.specification.ShowSpecification;
import ar.com.juani.melishows.dao.specification.ShowingSpecification;
import ar.com.juani.melishows.dto.SearchQueryDto;
import ar.com.juani.melishows.dto.ShowDto;
import ar.com.juani.melishows.dto.ShowingDetailDto;
import ar.com.juani.melishows.dto.ShowingDto;
import ar.com.juani.melishows.exception.ShowingNotFoundException;

@Service
public class QueryServiceImpl implements QueryService {

	@Autowired
	private ShowRepository showRepository;

	@Autowired
	private ShowingRepository showingRepository;

	@Override
	public List<ShowDto> findShows(SearchQueryDto showSearchQuery) {

		ShowSpecification showSpecification = new ShowSpecification(showSearchQuery);

		List<Show> shows = null;

		List<Sort> sort = new ArrayList<>();

		showSearchQuery.getOrderBy().ifPresent(orderByColumn -> {
			if (showSearchQuery.isSortDesc()) {
				sort.add(Sort.by(orderByColumn).descending());
			} else {
				sort.add(Sort.by(orderByColumn).ascending());
			}
		});

		if (sort.isEmpty()) {
			shows = showRepository.findAll(showSpecification);
		} else {
			shows = showRepository.findAll(showSpecification, sort.get(0));
		}

		return shows.stream()
				.map(show -> ShowDto.fromShow(show))
				.collect(Collectors.toList());
	}

	@Override
	public List<ShowingDto> findShowings(SearchQueryDto showSearchQuery) {

		ShowingSpecification showSpecification = new ShowingSpecification(showSearchQuery);

		List<Showing> showings = null;

		List<Sort> sort = new ArrayList<>();

		showSearchQuery.getOrderBy().ifPresent(orderByColumn -> {
			if (showSearchQuery.isSortDesc()) {
				sort.add(Sort.by(orderByColumn).descending());
			} else {
				sort.add(Sort.by(orderByColumn).ascending());
			}
		});

		if (sort.isEmpty()) {
			showings = showingRepository.findAll(showSpecification);
		} else {
			showings = showingRepository.findAll(showSpecification, sort.get(0));
		}

		return showings.stream()
				.map(showing -> ShowingDto.fromShowing(showing))
				.collect(Collectors.toList());
	}

	@Override
	public ShowingDetailDto findShowing(Long id) {

		Optional<Showing> showingResponse = showingRepository.findById(id);

		if (showingResponse.isPresent()) {
			Showing showing = showingResponse.get();
			return ShowingDetailDto.fromShowing(showing);
		}

		throw new ShowingNotFoundException(id);
	}
}