package com.nspl.app.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nspl.app.domain.UserRoleAssignment;


/**
 * Spring Data JPA repository for the UserRoleAssignment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserRoleAssignmentRepository extends JpaRepository<UserRoleAssignment,Long> {

	List<UserRoleAssignment> findByRoleId(Long id);
	
	@Query(value =  "select * from user_role_assignment where user_id =:user_id and (delete_flag is false or delete_flag is null) and ((active_flag =1 AND (DATEDIFF(CURDATE(),start_date)) >= 0) and if(end_date is null, sysdate()+1, (DATEDIFF(end_date,CURDATE()) >= 0)))",nativeQuery=true)
	List<UserRoleAssignment> findByuserId(@Param("user_id")Long user_id);
	
	@Query(value =  "select distinct(role_id) from user_role_assignment where user_id =:userId and (delete_flag is false or delete_flag is null) and ((active_flag =1 AND (DATEDIFF(CURDATE(),start_date)) >= 0) and if(end_date is null, sysdate()+1, (DATEDIFF(end_date,CURDATE()) >= 0)))",nativeQuery=true)
	List<BigInteger> fetchRoleIdsByUserId(@Param("userId")Long userId);
	
	
	@Query(value =  "select distinct(user_id) from user_role_assignment where role_id =:roleId and (delete_flag is false or delete_flag is null) and ((active_flag =1 AND (DATEDIFF(CURDATE(),start_date)) >= 0) and if(end_date is null, sysdate()+1, (DATEDIFF(end_date,CURDATE()) >= 0)))",nativeQuery=true)
	List<BigInteger> fetchUserIdsByRoleIds(@Param("roleId") Long roleId);
	
	UserRoleAssignment findByUserIdAndRoleId(Long userId, Long roleId);

    
}
