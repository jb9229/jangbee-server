package com.jangbee.estimate;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by test on 2016-01-31.
 */
public interface EstimateRepository extends JpaRepository<Estimate, Long> {

    Page<Estimate> findAll(Specification<Estimate> spec, Pageable pageable);
}
