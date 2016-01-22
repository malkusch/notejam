package net.notejam.spring.domain.account.security;

import java.io.Serializable;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Embeddable;

/**
 * An encoded password suitable for persistence.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Immutable
@Embeddable
public final class EncodedPassword implements Serializable {

    private static final long serialVersionUID = -6416062831522109818L;

    /**
     * The encoded password.
     */
    private final String password;

    /**
     * Creates a new encoded password.
     *
     * @param encodedPassword
     *            encoded password, not null
     */
    EncodedPassword(final String encodedPassword) {
	if (encodedPassword == null) {
	    throw new NullPointerException();
	}
	if (encodedPassword.isEmpty()) {
	    throw new IllegalArgumentException("The encoded password must not be empty!");
	}

        this.password = encodedPassword;
    }

    /**
     * Returns the string representation of this password.
     *
     * @return encoded password
     */
    @Override
    public String toString() {
        return password;
    }

    @Override
    public int hashCode() {
        return password.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EncodedPassword) {
            EncodedPassword other = (EncodedPassword) obj;
            return password.equals(other.password);

        } else {
            return false;
        }
    }

    /**
     * Builds an incomplete object.
     *
     * This is required by the persistence framework.
     */
    EncodedPassword() {
        password = null;
    }

}
