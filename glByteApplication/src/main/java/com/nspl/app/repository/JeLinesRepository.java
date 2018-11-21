package com.nspl.app.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nspl.app.domain.JeLines;

/**
 * Spring Data JPA repository for the JeLines entity.
 */
@SuppressWarnings("unused")
public interface JeLinesRepository extends JpaRepository<JeLines,Long> {
	
	
	
	@Query(value =  "SELECT distinct code_combination FROM t_je_lines where je_header_id = :jeBatchId",nativeQuery=true)
	List<String> fetchDistinctCodeCombinations(@Param("jeBatchId") Long jeBatchId);
	
	
	@Query(value =  "SELECT sum(debit_amount) from t_je_lines where je_header_id = :jeBatchId",nativeQuery=true)
	BigDecimal fetchDebitAmountJeHeaderId(@Param("jeBatchId") Long jeBatchId);
	
	@Query(value =  "SELECT sum(credit_amount) from t_je_lines where je_header_id = :jeBatchId",nativeQuery=true)
	BigDecimal fetchCreditAmountJeHeaderId(@Param("jeBatchId") Long jeBatchId);
	
	
	
	
	@Query(value =  "SELECT sum(accounted_debit) from t_je_lines where je_header_id = :jeBatchId and code_combination = :codeCombination",nativeQuery=true)
	BigDecimal fetchAccountedDebitByCodeCombinationAndJeHeaderId(@Param("jeBatchId") Long jeBatchId, @Param("codeCombination") String codeCombination);
	
	@Query(value =  "SELECT sum(accounted_credit) from t_je_lines where je_header_id = :jeBatchId and code_combination = :codeCombination",nativeQuery=true)
	BigDecimal fetchAccountedCreditByCodeCombinationAndJeHeaderId(@Param("jeBatchId") Long jeBatchId, @Param("codeCombination") String codeCombination);

	


	//List<JeLines> findByJeBatchId(Long jeBatchId);


	//Page<JeLines> findByJeBatchId(Long batchId, Pageable generatePageRequest2);
	
	@Query(value =  "SELECT row_id from t_je_lines where je_header_id = :jeBatchId",nativeQuery=true)
	List<BigInteger> findRowIdsByJeHeaderId(@Param("jeBatchId") Long jeBatchId);


	List<JeLines> findByJeHeaderId(Long batchId);


	Page<JeLines> findByJeHeaderId(Long batchId, Pageable generatePageRequest2);


	

	

}
