package net.notejam.spring.domain.account;

import java.io.Serializable;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Embeddable;

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;

/**
 * An email address.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Immutable
@Embeddable
public final class EmailAddress implements Serializable {

    private static final long serialVersionUID = -6416062831522109818L;

    /**
     * The email address.
     */
    private final String address;

    /**
     * Creates a new email address.
     *
     * @param emailAddress
     *            email address, not null
     * @throws IllegalArgumentException
     *             If the email address is invalid.
     */
    public EmailAddress(final String emailAddress) {
        assertValid(emailAddress);

        this.address = emailAddress;
    }

    /**
     * Asserts that the email address is valid.
     *
     * @param emailAddress
     *            email address, not null
     * @throws IllegalArgumentException
     *             If the email address is invalid.
     */
    private static void assertValid(final String emailAddress) {
        if (emailAddress == null) {
            throw new NullPointerException();
        }
        if (emailAddress.isEmpty()) {
            throw new IllegalArgumentException("An email address must not be empty.");
        }

        EmailValidator validator = new EmailValidator();
        if (!validator.isValid(emailAddress, null)) {
            throw new IllegalArgumentException("An email address must be valid.");
        }
    }

    /**
     * Returns the string representation of this email address.
     *
     * @return the email address
     */
    @Override
    public String toString() {
        return address;
    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof EmailAddress) {
            EmailAddress other = (EmailAddress) obj;
            return address.equals(other.address);

        } else {
            return false;
        }
    }

    /**
     * Builds an incomplete object.
     *
     * This is required by the persistence framework.
     */
    EmailAddress() {
        address = null;
    }

}
