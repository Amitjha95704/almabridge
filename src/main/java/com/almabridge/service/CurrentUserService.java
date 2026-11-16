////
////package com.almabridge.service;
////
////import com.almabridge.security.CustomUserDetails;
////import com.almabridge.enums.UserRole;
////
////import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
////import org.springframework.security.core.Authentication;
////import org.springframework.security.core.context.SecurityContextHolder;
////import org.springframework.stereotype.Service;
////
////@Service
////public class CurrentUserService {
////
////    private CustomUserDetails getPrincipal() {
////
////        Authentication authentication =
////                SecurityContextHolder.getContext().getAuthentication();
////
////        if (authentication == null ||
////                !authentication.isAuthenticated() ||
////                !(authentication.getPrincipal()
////                        instanceof CustomUserDetails userDetails)) {
////
////            throw new AuthenticationCredentialsNotFoundException(
////                    "Authenticated user not found"
////            );
////        }
////
////        return userDetails;
////    }
////
////    public Long getUserId() {
////        return getPrincipal().getUserId();
////    }
////
////    public Long getInstitutionId() {
////        return getPrincipal().getInstitutionId();
////    }
////
////    public UserRole getRole() {
////        return getPrincipal().getRole();
////    }
////
////    public String getEmail() {
////        return getPrincipal().getUsername();
////    }
////}
//
//
//package com.almabridge.service;
//
//import com.almabridge.enums.UserRole;
//import com.almabridge.security.CustomUserDetails;
//
//import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CurrentUserService {
//
//    private CustomUserDetails getPrincipal() {
//
//        Authentication authentication =
//                SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null ||
//                !authentication.isAuthenticated() ||
//                !(authentication.getPrincipal()
//                        instanceof CustomUserDetails userDetails)) {
//
//            throw new AuthenticationCredentialsNotFoundException(
//                    "Authenticated user not found"
//            );
//        }
//
//        return userDetails;
//    }
//
//    public Long getUserId() {
//        return getPrincipal().getUserId();
//    }
//
//    public Long getInstitutionId() {
//        return getPrincipal().getInstitutionId();
//    }
//
//    public UserRole getRole() {
//        return getPrincipal().getRole();
//    }
//
//    public String getEmail() {
//        return getPrincipal().getEmail();
//    }
//}



package com.almabridge.service;

import com.almabridge.enums.UserRole;
import com.almabridge.security.CustomUserDetails;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private CustomUserDetails getPrincipal() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                !(authentication.getPrincipal()
                        instanceof CustomUserDetails userDetails)) {

            throw new AuthenticationCredentialsNotFoundException(
                    "Authenticated user not found"
            );
        }

        return userDetails;
    }

    public Long getUserId() {
        return getPrincipal().getUserId();
    }

    // May return null for SUPER_ADMIN
    public Long getInstitutionId() {
        return getPrincipal().getInstitutionId();
    }

    // Use this method for institution-specific operations
    public Long getRequiredInstitutionId() {

        CustomUserDetails user = getPrincipal();

        if (user.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException(
                    "Only institution administrators can perform this operation."
            );
        }

        if (user.getInstitutionId() == null) {
            throw new AccessDeniedException(
                    "Institution ID is required for this operation."
            );
        }

        return user.getInstitutionId();
    }

    public UserRole getRole() {
        return getPrincipal().getRole();
    }

    public String getEmail() {
        return getPrincipal().getEmail();
    }
}
