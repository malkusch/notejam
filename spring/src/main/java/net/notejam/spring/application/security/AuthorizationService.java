package net.notejam.spring.application.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import net.notejam.spring.domain.account.User;
import net.notejam.spring.domain.account.security.Owned;
import net.notejam.spring.infrastructure.security.AuthenticationService;

/**
 * An authorization service.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Service
public class AuthorizationService {

    /**
     * The authentication service.
     */
    private final AuthenticationService authenticationService;

    /**
     * Builds the service
     * 
     * @param authenticationService
     *            authentication service
     */
    @Autowired
    AuthorizationService(final AuthenticationService authenticationService) {
	this.authenticationService = authenticationService;
    }

    /**
     * Authorizes an owned entity.
     *
     * @param owned
     *            entity which belongs to a user
     * @throws AccessDeniedException
     *             if entity is not owned by the authenticated user.
     */
    public void authorize(final Owned owned) {
	authorize(Optional.ofNullable(owned));
    }

    /**
     * Authorizes an owned entity.
     *
     * @param owned
     *            entity which belongs to a user
     * @throws AccessDeniedException
     *             if entity is not owned by the authenticated user.
     */
    public void authorize(final Optional<Owned> owned) {
	owned.ifPresent((Owned o) -> authorize(o.getOwner()));
    }

    /**
     * Authorizes access to the user entity.
     *
     * @param user
     *            user
     * @throws AccessDeniedException
     *             if user is not the currently authenticated user.
     */
    public void authorize(final User user) {
	if (user == null) {
	    return;
	}
	if (!authenticationService.authenticatedUser().equals(user)) {
	    throw new AccessDeniedException("Authenticated user is not authorized to access the entity.");
	}
    }

}
