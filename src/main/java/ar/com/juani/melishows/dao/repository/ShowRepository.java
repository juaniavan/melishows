package ar.com.juani.melishows.dao.repository;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;

import ar.com.juani.melishows.dao.model.Show;

public interface ShowRepository extends PagingAndSortingRepository<Show, Long>, JpaSpecificationExecutor<Show> {

	@QueryHints(value = {@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	List<Show> findAll(Specification<Show> spec);

	@QueryHints(value = {@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	List<Show> findAll(Specification<Show> spec, Sort sort);


}