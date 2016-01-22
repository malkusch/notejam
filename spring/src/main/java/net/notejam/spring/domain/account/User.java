package net.notejam.spring.domain.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.AbstractPersistable;

import net.notejam.spring.domain.account.security.EncodedPassword;

/**
 * A user.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Entity
public final class User extends AbstractPersistable<Integer> {

    private static final long serialVersionUID = -7874055769861590146L;

    /**
     * The email address.
     */
    @NotNull
    @Column(unique = true)
    private EmailAddress emailAddress;

    /**
     * The password.
     */
    @NotNull
    private EncodedPassword password;

    /**
     * Builds a new user.
     * 
     * @param emailAddress
     *            email address
     * @param password
     *            encoded password
     */
    User(final EmailAddress emailAddress, final EncodedPassword password) {
	if (emailAddress == null) {
	    throw new NullPointerException();
	}
	if (password == null) {
	    throw new NullPointerException();
	}

	this.emailAddress = emailAddress;
	this.password = password;
    }

    /**
     * Returns the encoded password.
     *
     * @return The encoded password.
     */
    public EncodedPassword getPassword() {
	return password;
    }

    /**
     * Returns the email address.
     *
     * @return The email address.
     */
    public EmailAddress getEmailAddress() {
	return emailAddress;
    }

    /**
     * Changes the password
     * 
     * @param password
     *            new encoded password
     */
    public void changePassword(EncodedPassword password) {
	if (password == null) {
	    throw new NullPointerException();
	}
	this.password = password;
    }

    /**
     * Builds an incomplete note for the persistence framework.
     */
    User() {
	emailAddress = null;
	password = null;
    }

}
