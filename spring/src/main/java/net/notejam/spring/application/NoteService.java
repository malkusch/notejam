package net.notejam.spring.application;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import net.notejam.spring.domain.Name;
import net.notejam.spring.domain.Note;
import net.notejam.spring.domain.NoteRepository;
import net.notejam.spring.domain.Pad;
import net.notejam.spring.domain.PadRepository;
import net.notejam.spring.domain.account.User;
import net.notejam.spring.infrastructure.security.owner.PermitOwner;

/**
 * An application service for note use cases.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Service
@Transactional
@PreAuthorize("isAuthenticated()")
public class NoteService {

    /**
     * The note repository.
     */
    private final NoteRepository repository;

    /**
     * The pad repository.
     */
    private final PadRepository padRepository;

    /**
     * Builds the service with its dependencies.
     * 
     * @param repository
     *            note repository
     * @param padRepository
     *            pad repository
     */
    @Autowired
    NoteService(final NoteRepository repository, final PadRepository padRepository) {
	this.repository = repository;
	this.padRepository = padRepository;
    }

    /**
     * Loads a note from the storage.
     *
     * @param noteId
     *            note id
     * @return note
     */
    @PermitOwner
    public Optional<Note> showNote(final int noteId) {
	return Optional.ofNullable(findAuthorizedNote(noteId));
    }

    /**
     * Browses in all notes of a user.
     *
     * @param pageable
     *            paging parameters
     * @return notes
     */
    public Page<Note> browseNotes(final User owner, final Pageable pageable) {
	return repository.findByOwner(owner, pageable);
    }

    /**
     * Browses in the notes of a pad.
     *
     * @param pad
     *            pad
     * @param pageable
     *            paging parameters
     * @return notes
     */
    public Page<Note> browseNotes(@PermitOwner final Pad pad, final Pageable pageable) {
	return repository.findByPad(pad, pageable);
    }

    /**
     * Edits an existing note.
     *
     * @param noteId
     *            note id
     * @param name
     *            name for the new pad
     * @param owner
     *            user who creates the new pad
     * @param padId
     *            optional pad id
     * @param text
     *            text
     */
    public void editNote(final int noteId, final Name name, final Optional<Integer> padId, final String text) {
	Note note = findAuthorizedNote(noteId);
	Optional<Pad> pad = findAuthorizedPad(padId);
	note.edit(name, pad, text);
    }

    /**
     * Deletes a note.
     *
     * @param noteId
     *            note id
     */
    public void deleteNote(final int noteId) {
	Note note = findAuthorizedNote(noteId);
	repository.delete(note);
    }

    /**
     * Writes a new note.
     *
     * @param name
     *            name for the new pad
     * @param owner
     *            user who creates the new pad
     * @param padId
     *            optional pad id
     * @param text
     *            text
     */
    @PermitOwner
    public Note writeNote(final Name name, final User owner, final Optional<Integer> padId, final String text) {
	Optional<Pad> pad = findAuthorizedPad(padId);
	Note note = new Note(name, owner, pad, text);
	
	note = repository.save(note);

	return note;
    }

    /**
     * Finds a note from the repository and checks the authorization.
     *
     * @param noteId
     *            note id
     * @return note
     */
    @PermitOwner
    private Note findAuthorizedNote(final int noteId) {
	return repository.findOne(noteId);
    }

    /**
     * Finds a pad from the repository and checks the authorization.
     *
     * @param padId
     *            pad id
     * @return pad
     */
    @PermitOwner
    private Optional<Pad> findAuthorizedPad(final Optional<Integer> padId) {
	return padId.map((Integer id) -> padRepository.findOne(id));
    }

}
