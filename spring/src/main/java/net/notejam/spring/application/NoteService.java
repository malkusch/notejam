package net.notejam.spring.application;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import net.notejam.spring.application.security.AuthorizationService;
import net.notejam.spring.domain.Name;
import net.notejam.spring.domain.Note;
import net.notejam.spring.domain.NoteRepository;
import net.notejam.spring.domain.Pad;
import net.notejam.spring.domain.PadRepository;
import net.notejam.spring.domain.account.User;

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
     * The authorization service.
     */
    private final AuthorizationService authorizationService;

    /**
     * Builds the service with its dependencies.
     * 
     * @param repository
     *            note repository
     * @param padRepository
     *            pad repository
     * @param authorizationService
     *            authorization service
     */
    @Autowired
    NoteService(final NoteRepository repository, final PadRepository padRepository,
	    final AuthorizationService authorizationService) {

	this.repository = repository;
	this.padRepository = padRepository;
	this.authorizationService = authorizationService;
    }

    /**
     * Loads a note from the storage.
     *
     * @param noteId
     *            note id
     * @return note
     */
    public Optional<Note> showNote(final int noteId) {
	Note note = repository.findOne(noteId);
	authorizationService.authorize(note);
	
	return Optional.ofNullable(note);
    }

    /**
     * Browses in all notes of a user.
     *
     * @param pageable
     *            paging parameters
     * @return notes
     */
    public Page<Note> browseNotes(final User owner, final Pageable pageable) {
	authorizationService.authorize(owner);

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
    public Page<Note> browseNotes(final Pad pad, final Pageable pageable) {
	authorizationService.authorize(pad);

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
	Note note = repository.findOne(noteId);
	authorizationService.authorize(note);
	
	Optional<Pad> pad = findPad(padId);
	authorizationService.authorize(pad);

	note.edit(name, pad, text);
    }

    /**
     * Deletes a note.
     *
     * @param noteId
     *            note id
     */
    public void deleteNote(final int noteId) {
	Note note = repository.findOne(noteId);
	authorizationService.authorize(note);

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
    public Note writeNote(final Name name, final User owner, final Optional<Integer> padId, final String text) {
	Optional<Pad> pad = findPad(padId);
	authorizationService.authorize(pad);

	Note note = new Note(name, owner, pad, text);
	authorizationService.authorize(note);

	note = repository.save(note);
	return note;
    }

    /**
     * Finds a pad from the repository.
     *
     * @param padId
     *            pad id
     * @return pad
     */
    private Optional<Pad> findPad(final Optional<Integer> padId) {
	return padId.map((Integer id) -> padRepository.findOne(id));
    }

}
