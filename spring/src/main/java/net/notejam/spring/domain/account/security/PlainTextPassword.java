package net.notejam.spring.domain.account.security;

import javax.annotation.concurrent.Immutable;

/**
 * A plain text password.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Immutable
public final class PlainTextPassword {

    /**
     * The plain text password.
     */
    final String text;

    /**
     * Creates a new plain text password.
     *
     * @param password
     *            plain text password, not null
     * @throws IllegalArgumentException
     *             if the password is invalid
     */
    public PlainTextPassword(final String password) {
	assertValid(password);
	this.text = password;
    }

    /**
     * Validates the plain text password
     * 
     * @param password
     *            plain text password
     * 
     * @throws IllegalArgumentException
     *             if the password is invalid
     */
    private static void assertValid(final String password) {
	if (password == null) {
	    throw new NullPointerException();
	}
	if (password.isEmpty()) {
	    throw new IllegalArgumentException("The password must not be empty!");
	}
	if (password.length() < 8) {
	    throw new IllegalArgumentException("The password must have at least 8 characters!");
	}
	if (password.length() > 128) {
	    throw new IllegalArgumentException("The password must not have more than 128 characters!");
	}
    }

    /**
     * Returns a string representation of this password.
     * 
     * This is not the password itself. There's no way to reveal that password.
     * Use a {@link EncodedPassword} instead. You can build a encoded password
     * with the {@link PasswordEncodingService}.
     *
     * @return password representation
     */
    @Override
    public String toString() {
	return "******";
    }

    @Override
    public int hashCode() {
	// This bad hash function should help not to expose the plain text password.
	return -1;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof PlainTextPassword) {
	    PlainTextPassword other = (PlainTextPassword) obj;
	    return text.equals(other.text);

	} else {
	    return false;
	}
    }

}
