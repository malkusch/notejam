package net.notejam.spring.application;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import net.notejam.spring.domain.Name;
import net.notejam.spring.domain.NoteRepository;
import net.notejam.spring.domain.Pad;
import net.notejam.spring.domain.PadRepository;
import net.notejam.spring.domain.account.User;
import net.notejam.spring.infrastructure.security.owner.PermitOwner;

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
     * Builds the service with its dependencies.
     * 
     * @param padRepository
     *            pad repository
     * @param noteRepository
     *            note repository
     */
    @Autowired
    PadService(final PadRepository padRepository, final NoteRepository noteRepository) {
	this.padRepository = padRepository;
	this.noteRepository = noteRepository;
    }

    /**
     * Deletes a pad and its notes.
     *
     * @param pad
     *            pad id
     */
    public void deletePad(final int padId) {
	Pad pad = findAuthorizedPad(padId);
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
	Pad pad = findAuthorizedPad(padId);
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
    @PermitOwner
    public Pad createPad(final Name name, final User owner) {
	Pad pad = new Pad(name, owner);
	padRepository.save(pad);
	return pad;
    }

    /**
     * Returns a pad for presentation.
     * 
     * @param padId
     *            pad
     * @return pad
     */
    @PermitOwner
    public Pad showPad(final int padId) {
	return findAuthorizedPad(padId);
    }

    /**
     * Finds a pad from the repository and checks the authorization.
     *
     * @param padId
     *            pad id
     * @return pad
     */
    @PermitOwner
    private Pad findAuthorizedPad(final int padId) {
	return padRepository.findOne(padId);
    }
    
}
