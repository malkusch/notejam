package net.notejam.spring.infrastructure.security;

import java.util.ArrayList;

import net.notejam.spring.domain.account.User;

/**
 * The Spring Security user.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
public final class AuthenticatedUser extends org.springframework.security.core.userdetails.User {

    private static final long serialVersionUID = 4600571779027862739L;

    /**
     * The user.
     */
    private final User user;

    /**
     * Builds a new user details object with its user.
     *
     * @param user
     *            user
     */
    public AuthenticatedUser(final User user) {
	super(user.getEmailAddress().toString(), user.getPassword().toString(), new ArrayList<>());
	this.user = user;
    }

    /**
     * Returns the user.
     *
     * @return user
     */
    public User getUser() {
	return user;
    }

}
