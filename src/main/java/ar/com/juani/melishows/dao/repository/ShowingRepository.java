package ar.com.juani.melishows.dao.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;

import ar.com.juani.melishows.dao.model.Showing;

public interface ShowingRepository
		extends PagingAndSortingRepository<Showing, Long>, JpaSpecificationExecutor<Showing> {

	@QueryHints(value = {@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	Optional<Showing> findById(Long id);

	@QueryHints(value = {@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	List<Showing> findAll(Specification<Showing> spec);

	@QueryHints(value = {@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	List<Showing> findAll(Specification<Showing> spec, Sort sort);
}