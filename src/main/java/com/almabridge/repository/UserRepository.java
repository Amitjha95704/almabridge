//package com.almabridge.repository;
//
//import com.almabridge.entity.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.Optional;
//
//public interface UserRepository
//        extends JpaRepository<User, Long> {
//
//    Optional<User> findByEmail(String email);
//
//    boolean existsByEmail(String email);
//
//    boolean existsByEmailAndInstitutionId(
//            String email, Long institutionId
//    );
//}


package com.almabridge.repository;

import com.almabridge.entity.User;
import com.almabridge.enums.UserRole;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import com.almabridge.dto.admin.analytics.AccountStatusDistributionResponse;
//import com.almabridge.entity.UserRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Collection;
//import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndInstitutionId(
            String email,
            Long institutionId
    );

    // Institution-level filtering

    List<User> findAllByInstitution_Id(Long institutionId);

    List<User> findAllByInstitution_IdAndRole(
            Long institutionId,
            UserRole role
    );

    Optional<User> findByIdAndInstitution_Id(
            Long userId,
            Long institutionId
    );
    
    long countByInstitution_IdAndRole(
            Long institutionId,
            UserRole role
    );

    long countByInstitution_IdAndRoleAndEnabled(
            Long institutionId,
            UserRole role,
            Boolean enabled
    );

    long countByInstitution_IdAndEnabled(
            Long institutionId,
            Boolean enabled
    );
    
    @Query("""
    	    SELECT new com.almabridge.dto.admin.analytics.AccountStatusDistributionResponse(
    	        u.enabled,
    	        COUNT(u)
    	    )
    	    FROM User u
    	    WHERE u.institution.id = :institutionId
    	      AND u.role IN :roles
    	    GROUP BY u.enabled
    	""")
    	List<AccountStatusDistributionResponse> getAccountStatusDistribution(
    	        @Param("institutionId") Long institutionId,
    	        @Param("roles") Collection<UserRole> roles
    	);
}
