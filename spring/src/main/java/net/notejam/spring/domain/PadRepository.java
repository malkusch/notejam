package net.notejam.spring.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.notejam.spring.domain.account.User;

/**
 * The pad repository.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
public interface PadRepository extends JpaRepository<Pad, Integer> {

    /**
     * Finds all pads of an user.
     *
     * @param owner
     *            The owner
     * @return The user's pads
     */
    List<Pad> findByOwner(User owner);

}
