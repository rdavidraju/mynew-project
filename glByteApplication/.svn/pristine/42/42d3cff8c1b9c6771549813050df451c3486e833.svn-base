package com.nspl.app.repository;

import com.nspl.app.domain.DataChild;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data JPA repository for the DataChild entity.
 */
@SuppressWarnings("unused")
public interface DataChildRepository extends JpaRepository<DataChild,Long> {

	@Modifying
	@Transactional
	@Query(value = "delete from t_data_child where id in ( :varianceIds )",nativeQuery=true)
	void deleteByIds(@Param("varianceIds") List<Long> varianceIds);

}
