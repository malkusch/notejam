package net.notejam.spring.domain.account.recovery;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The token repository.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
public interface RecoveryTokenRepository extends JpaRepository<RecoveryToken, Integer> {

    /**
     * Deletes all tokens which are expired.
     *
     * @param date
     *            The time
     */
    void deleteByExpirationLessThan(Instant date);

}
