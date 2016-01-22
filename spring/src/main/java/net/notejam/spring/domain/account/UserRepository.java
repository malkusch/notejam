package net.notejam.spring.domain.account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The user repository.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds one user by their email address.
     *
     * @param emailAddress
     *            email address
     * @return The user or null
     */
    Optional<User> findOneByEmailAddress(EmailAddress emailAddress);

}
