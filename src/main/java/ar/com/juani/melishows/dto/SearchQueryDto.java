package ar.com.juani.melishows.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class SearchQueryDto {

    @Builder.Default
    private Optional<LocalDateTime> showingFrom = Optional.empty();
    @Builder.Default
    private Optional<LocalDateTime> showingTo = Optional.empty();

    @Builder.Default
    private Optional<BigDecimal> priceFrom = Optional.empty();
    @Builder.Default
    private Optional<BigDecimal> priceTo = Optional.empty();

    @Builder.Default
    private Optional<String> theaterName = Optional.empty();
    @Builder.Default
    private Optional<String> locationName = Optional.empty();

    @Builder.Default
    private Optional<String> showName = Optional.empty();
    @Builder.Default
    private Optional<String> artistName = Optional.empty();
    
    @Builder.Default
    private Optional<String> orderBy = Optional.empty();
    @Builder.Default
    private boolean sortDesc = false;
    
    public boolean hasShowFilters() {
        return showName.isPresent() || artistName.isPresent() || hasTheaterFilters();
    }

    public boolean hasShowingFilters() {
        return showingFrom.isPresent() || showingTo.isPresent();
    }

    public boolean hasSectorFilters() {
        return priceFrom.isPresent() || priceTo.isPresent();
    }

    public boolean hasTheaterFilters() {
        return theaterName.isPresent() || locationName.isPresent();
    }
    
 }