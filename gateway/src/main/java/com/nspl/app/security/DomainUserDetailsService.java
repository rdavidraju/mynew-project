package com.nspl.app.security;

import com.nspl.app.domain.Functionality;
import com.nspl.app.domain.RoleFunctionAssignment;
import com.nspl.app.domain.User;
import com.nspl.app.repository.FunctionalityRepository;
import com.nspl.app.repository.RoleFunctionAssignmentRepository;
import com.nspl.app.repository.UserRepository;
import com.nspl.app.repository.UserRoleAssignmentRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;
    private final FunctionalityRepository functionalityRepository;
    private final RoleFunctionAssignmentRepository roleFunctionAssignmentRepository;
    private final UserRoleAssignmentRepository userRoleAssignmentRepository;

    public DomainUserDetailsService(UserRepository userRepository,
			FunctionalityRepository functionalityRepository,
			RoleFunctionAssignmentRepository roleFunctionAssignmentRepository,
			UserRoleAssignmentRepository userRoleAssignmentRepository) {
		this.userRepository = userRepository;
		this.functionalityRepository = functionalityRepository;
		this.roleFunctionAssignmentRepository = roleFunctionAssignmentRepository;
		this.userRoleAssignmentRepository = userRoleAssignmentRepository;
	}

	@Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
       /* Optional<User> userFromDatabase = userRepository.findOneWithAuthoritiesByLogin(lowercaseLogin);*/
        Optional<User> userFromDatabase = userRepository.findOneWithAuthoritiesByEmail(lowercaseLogin);
        
        Long userId = userFromDatabase.get().getId();
        Long tenantId = userFromDatabase.get().getTenantId();
        List<BigInteger> roleIds = userRoleAssignmentRepository.fetchRoleIdsByUserId(userId);
        List<Functionality> functionalities = functionalityRepository.findAll();
        HashMap<Long,String> functionalityIdName = new HashMap<Long, String>();
        for(Functionality func:functionalities)
        {
        	functionalityIdName.put(Long.valueOf(func.getId()), func.getFuncName());
        }
        List<Long> roleIdsL = new ArrayList<Long>();
        for(BigInteger id:roleIds)
        {
        	roleIdsL.add(Long.valueOf(id.longValue()));
        }
        List<RoleFunctionAssignment> userFunctions = roleFunctionAssignmentRepository.findByRoleIdIn(roleIdsL);
        return userFromDatabase.map(user -> {
            if (!user.getActivated()) {
                throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
            }
//            List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
//                    .map(authority -> new SimpleGrantedAuthority(authority.getName()))
//                .collect(Collectors.toList());
            List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
            /**/
            try
            {
	            for(RoleFunctionAssignment roleFuncAssignment:userFunctions)
	            {
	            	String roleName = "";
	            	if(functionalityIdName.containsKey(roleFuncAssignment.getFunctionId()))
	            	{
	            		roleName = functionalityIdName.get(roleFuncAssignment.getFunctionId());
	            		roleName = roleName.trim();
	            	}
	            	if(roleName.length()>0)
	            	{
	            		grantedAuthorities.add(new SimpleGrantedAuthority(roleName));
	            	}
	            }
            }
            catch(Exception exp)
            {
            	exp.printStackTrace();
            }
	        /**/
            return new org.springframework.security.core.userdetails.User(lowercaseLogin,
                user.getPassword(),
                grantedAuthorities);
        }).orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the " +
        "database"));
    }
}
