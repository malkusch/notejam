package net.notejam.spring.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import net.notejam.spring.domain.account.User;

/**
 * The note repository.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
public interface NoteRepository extends JpaRepository<Note, Integer> {

    /**
     * Pages through all notes of a pad.
     *
     * @param pad
     *            The pad
     * @param pageable
     *            The paging parameters
     * @return The notes
     */
    Page<Note> findByPad(Pad pad, Pageable pageable);

    /**
     * Pages through all notes of a user.
     *
     * @param owner
     *            The owner
     * @param pageable
     *            The paging parameters
     * @return The notes
     */
    Page<Note> findByOwner(User owner, Pageable pageable);

    /**
     * Deletes all notes of a pad.
     *
     * @param pad
     *            The pad
     */
    void deleteByPad(Pad pad);

}
