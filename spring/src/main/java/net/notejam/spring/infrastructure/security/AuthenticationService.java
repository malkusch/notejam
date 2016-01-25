package net.notejam.spring.infrastructure.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;

import net.notejam.spring.domain.account.User;

/**
 * An authentication service.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Service
public class AuthenticationService {

    /**
     * The security context holder strategy.
     */
    private final SecurityContextHolderStrategy contextHolder;

    /**
     * Builds the service with the default spring security holder context
     * strategy.
     */
    AuthenticationService() {
        this(SecurityContextHolder.getContextHolderStrategy());
    }

    /**
     * Builds the service.
     *
     * @param contextHolder
     *            security context holder strategy
     */
    AuthenticationService(final SecurityContextHolderStrategy contextHolder) {
        this.contextHolder = contextHolder;
    }

    /**
     * Returns the currently authenticated user.
     *
     * @return authenticated user
     * @throws AccessDeniedException
     *             if currently no user is authenticated
     */
    public User authenticatedUser() {
        Authentication authentication = contextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AccessDeniedException("No user is authenticated.");
        }
        return ((AuthenticatedUser) authentication.getPrincipal()).getUser();
    }

}
