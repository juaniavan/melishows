package ar.com.juani.melishows.service;

import java.util.List;

import ar.com.juani.melishows.dto.SearchQueryDto;
import ar.com.juani.melishows.dto.ShowDto;
import ar.com.juani.melishows.dto.ShowingDetailDto;
import ar.com.juani.melishows.dto.ShowingDto;

public interface QueryService {

    List<ShowDto> findShows(SearchQueryDto showSearchQuery);

    List<ShowingDto> findShowings(SearchQueryDto showSearchQuery);

    ShowingDetailDto findShowing(Long id);

}
