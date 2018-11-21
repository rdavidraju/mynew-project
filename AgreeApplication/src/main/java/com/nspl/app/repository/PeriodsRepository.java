package com.nspl.app.repository;

import com.nspl.app.domain.Periods;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring Data JPA repository for the Periods entity.
 */
@SuppressWarnings("unused")
public interface PeriodsRepository extends JpaRepository<Periods,Long> {

	List<Periods> findByCalId(Long id);

	List<Periods> findByCalIdAndYear(Long typeId, int year);

	List<Periods> findByCalIdAndYearOrderByFromDateDesc(Long typeId, int year);
	
	@Query(value="SELECT distinct periods.cal_id, periods.period_name, periods.year, periods.period_num FROM t_periods periods, t_ledger_definition ldgrDef where ldgrDef.name=:ldgrNames and periods.status<>'NEVER_OPENED' and ldgrDef.tenant_id=:tenantId and ldgrDef.calendar_id=periods.cal_id order by periods.year desc, periods.period_num desc;", nativeQuery=true)
	ArrayList<Object[]> getPeriodsByLedgerName(@Param("tenantId") Long tenantId,@Param("ldgrNames") String ldgrNames);
	
	@Query(value="SELECT * FROM t_periods where cal_id=:calId and status <> 'NEVER_OPENED' order by year desc, period_num desc;", nativeQuery=true)
	ArrayList<Periods> getPeriodsByCalIdOrderByDesc(@Param("calId") Long calId);

}
