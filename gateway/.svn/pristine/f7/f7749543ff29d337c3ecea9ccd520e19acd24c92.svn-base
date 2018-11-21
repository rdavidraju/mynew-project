package com.nspl.app.repository;

import com.nspl.app.domain.User;
import com.nspl.app.service.dto.UserDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.math.BigInteger;
import java.time.Instant;

/**
 * Spring Data JPA repository for the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findOneByActivationKey(String activationKey);

	List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);

	Optional<User> findOneByResetKey(String resetKey);

	Optional<User> findOneByEmail(String email);

	Optional<User> findOneByLogin(String login);

	@EntityGraph(attributePaths = "authorities")
	User findOneWithAuthoritiesById(Long id);

	@EntityGraph(attributePaths = "authorities")
	Optional<User> findOneWithAuthoritiesByLogin(String login);
	
	@EntityGraph(attributePaths = "authorities")
	Optional<User> findOneWithAuthoritiesByEmail(String email);

	Page<User> findAllByLoginNot(Pageable pageable, String login);
	
	List<User> findAllByTenantIdOrderByCreatedDateDesc(Long tenantId);
	Page<User> findAllByTenantIdOrderByCreatedDateDesc(Long tenantId, Pageable pageable);
	
	Optional<User> findOneByLoginAndActivatedIsTrue(String login);
	
	Optional<User> findOneByEmailAndActivatedIsTrue(String login);
	User findOne(Long id);
	
	@Query(value =  "SELECT * FROM jhi_user where tenant_id = :tenantId and activated is true order by login asc",nativeQuery=true)
	List<User> fetchByTenantId(@Param("tenantId") Long tenantId);

	@Query(value =  "SELECT * FROM jhi_user where id in (:userIds)",nativeQuery=true)
	List<User> fetchByIds(@Param("userIds") List<BigInteger> userIds);

	List<User> findByEmail(String email);

	@Query(value =  "select * from jhi_user where tenant_id=:tenant_id and ((activated is true AND (DATEDIFF(CURDATE(),start_date)) >= 0) and if(end_date is null, sysdate()+1,(DATEDIFF(end_date,CURDATE())) >= 0)) ORDER BY id desc",nativeQuery=true)
	List<User> findByTenantIdAndIsActive(@Param("tenant_id") Long tenant_id);

	
	@Query(value =  "SELECT id FROM jhi_user where tenant_id = :tenantId",nativeQuery=true)
	List<BigInteger> fetchUserIdsByTenantId(@Param("tenantId") Long tenantId);

	@Query(value =  "SELECT * FROM jhi_user where id in (:userIds) and tenant_id = :tenantId",nativeQuery=true)
	List<User> fetchByIdsAndTenantId(@Param("userIds") List<BigInteger> userIds, @Param("tenantId") Long tenantId);

	List<User> findByTenantIdAndEmailIn(Long tenantId, List<String> loginList);
	
	User findByIdAndEmail(Long id, String email);
	
}
