package ar.com.juani.melishows.dao.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Sector {

      @Id
      @GeneratedValue(strategy=GenerationType.AUTO)
      private Long id;
      private String name;
      private String description;
      
      @Column(nullable = false)
      BigDecimal price;

      @ManyToOne
      private Showing showing;
      
      @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
      @OneToMany(mappedBy = "sector")
      private List<Availability> availabilities;

}