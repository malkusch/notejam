package net.notejam.spring.domain;

import java.io.Serializable;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Embeddable;

/**
 * A name.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Immutable
@Embeddable
public final class Name implements Serializable {

    private static final long serialVersionUID = -6416062831522109818L;

    /**
     * The name.
     */
    private final String name;

    /**
     * Creates a new name.
     *
     * @param name
     *            name, not null
     * @throws IllegalArgumentException
     *             If the name is empty or contains more than 100 characters.
     */
    public Name(final String name) {
        assertValid(name);

        this.name = name;
    }

    /**
     * Asserts that the name is valid.
     *
     * @param name
     *            name, not null
     * @throws IllegalArgumentException
     *             If the name is empty or contains more than 100 characters.
     */
    private static void assertValid(final String name) {
        if (name == null) {
            throw new NullPointerException();
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("A name must not be empty.");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("A name must not contain more than 100 characters.");
        }
    }

    /**
     * Returns the string representation of this name.
     *
     * @return the name
     */
    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Name) {
            Name other = (Name) obj;
            return name.equals(other.name);

        } else {
            return false;
        }
    }

    /**
     * Builds an incomplete object.
     *
     * This is required by the persistence framework.
     */
    Name() {
        name = null;
    }

}
