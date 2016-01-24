package net.notejam.spring.application;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import net.notejam.spring.application.security.AuthorizationService;
import net.notejam.spring.domain.Name;
import net.notejam.spring.domain.NoteRepository;
import net.notejam.spring.domain.Pad;
import net.notejam.spring.domain.PadRepository;
import net.notejam.spring.domain.account.User;

/**
 * An application service for pad use cases.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Service
@Transactional
@PreAuthorize("isAuthenticated()")
public class PadService {

    /**
     * The pad repository.
     */
    private final PadRepository padRepository;

    /**
     * The note repository.
     */
    private final NoteRepository noteRepository;

    /**
     * The authorization service.
     */
    private final AuthorizationService authorizationService;

    /**
     * Builds the service with its dependencies.
     * 
     * @param padRepository
     *            pad repository
     * @param noteRepository
     *            note repository
     * @param authorizationService
     *            authorization service
     */
    @Autowired
    PadService(final PadRepository padRepository, final NoteRepository noteRepository,
	    final AuthorizationService authorizationService) {

	this.padRepository = padRepository;
	this.noteRepository = noteRepository;
	this.authorizationService = authorizationService;
    }

    /**
     * Deletes a pad and its notes.
     *
     * @param pad
     *            pad id
     */
    public void deletePad(final int padId) {
	Pad pad = padRepository.findOne(padId);
	authorizationService.authorize(pad);
	
	noteRepository.deleteByPad(pad);
	padRepository.delete(pad);
    }

    /**
     * Edits a pad.
     *
     * @param padId
     *            pad id
     * @param name
     *            new pad name
     */
    public void editPad(final int padId, final Name name) {
	Pad pad = padRepository.findOne(padId);
	authorizationService.authorize(pad);

	pad.edit(name);
    }

    /**
     * Creates a new pad.
     *
     * @param name
     *            name for the new pad
     * @param owner
     *            user who creates the new pad
     */
    public Pad createPad(final Name name, final User owner) {
	Pad pad = new Pad(name, owner);
	authorizationService.authorize(pad);

	pad = padRepository.save(pad);

	return pad;
    }

    /**
     * Returns a pad for presentation.
     * 
     * @param padId
     *            pad
     * @return pad
     */
    public Pad showPad(final int padId) {
	Pad pad = padRepository.findOne(padId);
	authorizationService.authorize(pad);

	return pad;
    }

}
