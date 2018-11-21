package com.nspl.app.repository;

import com.nspl.app.domain.AcctRuleDerivations;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AcctRuleDerivations entity.
 */
@SuppressWarnings("unused")
public interface AcctRuleDerivationsRepository extends JpaRepository<AcctRuleDerivations,Long> {

	

	List<AcctRuleDerivations> findByAcctRuleActionId(Long id);

	List<AcctRuleDerivations> findByAcctRuleActionIdIsNullAndRuleId(Long ruleId);

}
