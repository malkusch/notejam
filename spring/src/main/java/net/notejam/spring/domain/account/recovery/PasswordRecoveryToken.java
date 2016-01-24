package net.notejam.spring.domain.account.recovery;

import java.io.Serializable;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Embeddable;

/**
 * A password recovery token.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Immutable
@Embeddable
public final class PasswordRecoveryToken implements Serializable {

    private static final long serialVersionUID = -8964039678048204360L;

    /**
     * The token.
     */
    private final String token;

    /**
     * Builds the token.
     * 
     * @param token
     *            token
     */
    public PasswordRecoveryToken(final String token) {
	if (token == null) {
	    throw new NullPointerException();
	}
	if (token.isEmpty()) {
	    throw new IllegalArgumentException("The token must not be empty");
	}

	this.token = token;
    }

    /**
     * Returns the string representation of this token.
     *
     * @return token
     */
    @Override
    public String toString() {
	return token;
    }

    @Override
    public int hashCode() {
	return token.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof PasswordRecoveryToken) {
	    PasswordRecoveryToken other = (PasswordRecoveryToken) obj;
	    return token.equals(other.token);

	} else {
	    return false;
	}
    }

    /**
     * Builds an incomplete object.
     *
     * This is required by the persistence framework.
     */
    PasswordRecoveryToken() {
	token = null;
    }

}
