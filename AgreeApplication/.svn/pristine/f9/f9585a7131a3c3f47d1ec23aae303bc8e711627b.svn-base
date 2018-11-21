package com.nspl.app.repository;

import java.util.List;

import com.nspl.app.domain.RowConditions;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the RowConditions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RowConditionsRepository extends JpaRepository<RowConditions,Long> {

	@Query(value = "select * from t_row_conditions where jhi_type =:jhi_type and template_line_id in (select id from t_file_template_lines where template_id =(select id from t_file_templates where id_for_display =:id) )",nativeQuery=true)
	List<com.nspl.app.domain.RowConditions> findConditionsByTemplateIdAndType(@Param("id") String id, @Param("jhi_type") String jhi_type);
    
}
