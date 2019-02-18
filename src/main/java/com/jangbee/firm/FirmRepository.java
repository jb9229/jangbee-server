package com.jangbee.firm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by test on 2019-01-20.
 */
public interface FirmRepository extends JpaRepository<Firm, Long> {
    Firm findByAccountId(String accountId);
    long countByEquiListStrLike(String equipment);

    @Query(value = "SELECT id, fname, ST_DISTANCE_SPHERE(POINT(?2, ?3), location) AS distance FROM firm WHERE equi_list_str like ?1 ORDER BY distance desc LIMIT ?4, ?5 ",
            countQuery = "SELECT count(*) FROM firm ",
            nativeQuery = true)
    List<Object> getNearFirm(String equipment, Double longitude, Double latitude, int startRowNum, int size);
}

// \n-- #pageable\n
// ?#{#pageable}