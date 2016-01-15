package net.notejam.spring.pad;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.notejam.spring.error.ResourceNotFoundException;
import net.notejam.spring.note.NoteService;
import net.notejam.spring.security.owner.PermitOwner;
import net.notejam.spring.user.User;
import net.notejam.spring.user.UserService;

/**
 * The pad service.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Service
public class PadService {

    /**
     * The pad repository.
     */
    @Autowired
    private PadRepository padRepository;

    /**
     * The user service.
     */
    @Autowired
    private UserService userService;

    /**
     * The note service.
     */
    @Autowired
    private NoteService noteService;

    /**
     * Returns all pads for the currently authenticated user.
     *
     * @return The user's pads
     */
    @Transactional
    public List<Pad> getAllPads() {
        User user = userService.getAuthenticatedUser();
        return padRepository.findByUser(user);
    }

    /**
     * Loads a pad from the storage.
     *
     * @param id
     *            The pad id
     * @return The pad, not null
     * @throws ResourceNotFoundException
     *             If the the pad was not found.
     */
    @PermitOwner
    public Pad getPad(final int id) {
        return Optional.ofNullable(padRepository.findOne(id)).orElseThrow(() -> new ResourceNotFoundException());
    }

    /**
     * Deletes a pad and its notes.
     *
     * @param pad
     *            pad id
     */
    @Transactional
    public void deletePad(int id) {
        Pad pad = getPad(id);
        noteService.deleteNotes(pad);
        padRepository.delete(pad);
    }

    /**
     * Edits a pad.
     *
     * @param pad
     *            pad
     * @param name
     *            new pad name
     */
    @Transactional
    public void editPad(final Pad pad, final Name name) {
        pad.edit(name);
    }

    /**
     * Creates a new pad.
     *
     * @param createPad
     *            command for creating the pad
     */
    @Transactional
    public Pad createPad(final Name name) {
        Pad pad = new Pad(name, userService.getAuthenticatedUser());
        padRepository.save(pad);
        return pad;
    }

}
