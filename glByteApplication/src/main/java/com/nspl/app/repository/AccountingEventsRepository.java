package com.nspl.app.repository;

import com.nspl.app.domain.AccountingEvents;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AccountingEvents entity.
 */
@SuppressWarnings("unused")
public interface AccountingEventsRepository extends JpaRepository<AccountingEvents,Long> {

	AccountingEvents findOneByAcctRuleGroupIdAndDataViewIdAndRowId(Long id,
			Long viewId, Long rowId);

	List<AccountingEvents> findByAcctRuleGroupIdAndDataViewIdInAndRowIdInAndRuleIdIn(
			Long id, List<Long> viewIds, List<Long> rowIds, List<Long> ruleIds);

}
