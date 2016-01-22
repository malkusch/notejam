package net.notejam.spring.infrastructure.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import net.notejam.spring.domain.account.User;

/**
 * An authentication service.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Component
public class AuthenticationService {

    /**
     * Returns the currently authenticated user.
     * 
     * @return authenticated user
     * @throws AccessDeniedException
     *             if currently no user is authenticated
     */
    public User getAuthenticatedUser() {
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	if (authentication == null) {
	    throw new AccessDeniedException("No user is authenticated.");
	}
	return ((AuthenticatedUser) authentication.getPrincipal()).getUser();
    }

}
