package net.notejam.spring.domain.account.recovery;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.AbstractPersistable;

import net.notejam.spring.domain.account.User;

/**
 * A password recovery process.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Entity
public final class PasswordRecoveryProcess extends AbstractPersistable<Integer> {

    private static final long serialVersionUID = 5923083445165411558L;

    /**
     * The user.
     */
    @NotNull
    @ManyToOne
    private final User user;

    /**
     * The token.
     */
    @NotNull
    private final PasswordRecoveryToken token;

    /**
     * The expiration date.
     */
    @NotNull
    private final Instant expiration;

    /**
     * Builds a token.
     *
     * @param token
     *            token
     * @param user
     *            user
     * @param expiration
     *            expiration date
     */
    PasswordRecoveryProcess(final PasswordRecoveryToken token, final User user, final Instant expiration) {
        assertValid(token, user, expiration);

        this.token = token;
        this.user = user;
        this.expiration = expiration;
    }

    /**
     * Checks the invariants.
     *
     * @param token
     *            token
     * @param user
     *            owner
     * @param expiration
     *            expiration date
     */
    private static void assertValid(final PasswordRecoveryToken token, final User user, final Instant expiration) {
        if (token == null) {
            throw new NullPointerException();
        }
        if (user == null) {
            throw new NullPointerException();
        }
        if (expiration == null) {
            throw new NullPointerException();
        }
        if (expiration.isBefore(Instant.now())) {
            throw new IllegalArgumentException("The expiration date must be in the future.");
        }
    }

    /**
     * Returns if this process is expired.
     *
     * @return true if this process is expired
     */
    public boolean isExpired() {
        return Instant.now().isAfter(expiration);
    }

    /**
     * Returns the user.
     *
     * @return The user.
     */
    public User user() {
        return user;
    }

    /**
     * Returns the token.
     *
     * @return The token.
     */
    public PasswordRecoveryToken token() {
        return token;
    }

    /**
     * Builds an incomplete object.
     *
     * This is required by the persistence framework.
     */
    PasswordRecoveryProcess() {
        expiration = null;
        user = null;
        token = null;
    }

}
