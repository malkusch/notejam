package net.notejam.spring.note.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import net.notejam.spring.URITemplates;
import net.notejam.spring.note.Note;
import net.notejam.spring.note.NoteService;
import net.notejam.spring.pad.Name;
import net.notejam.spring.pad.controller.PadsAdvice.Pads;

/**
 * The create note controller.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Controller
@RequestMapping(URITemplates.CREATE_NOTE)
@PreAuthorize("isAuthenticated()")
@Pads
public class CreateNoteController {

    /**
     * The note service.
     */
    private final NoteService service;

    /**
     * Builds the controller with its dependencies.
     *
     * @param service
     *            note service
     */
    @Autowired
    CreateNoteController(final NoteService service) {
	this.service = service;
    }

    /**
     * Shows the form for creating a note.
     *
     * @param createNote
     *            note command
     * @param padId
     *            preselected pad id
     *
     * @return The view
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showCreateNoteForm(final NoteCommand createNote,
	    @RequestParam(value = "pad", required = false) final Optional<Integer> padId) {

	createNote.setPadId(padId);

	return "note/create";
    }

    /**
     * Creates a new note.
     *
     *
     * @param createNote
     *            note command
     * @param errors
     *            The validation result.
     *
     * @return The view
     */
    @RequestMapping(method = RequestMethod.POST)
    public String createNote(final @Valid NoteCommand createNote, final Errors errors) {

	if (errors.hasErrors()) {
	    return showCreateNoteForm(createNote, createNote.getPadId());
	}

	Note note = service.buildNote(createNote.getPadId().orElse(null));
	note.setName(new Name(createNote.getName()));
	note.setText(createNote.getText());
	service.saveNote(note, note.getPad());

	return String.format("redirect:%s", buildCreatedNoteUri(note.getId()));
    }

    /**
     * Builds the URI for the created note.
     *
     * @param id
     *            The note id
     * @return The URI
     */
    private static String buildCreatedNoteUri(final int id) {
	UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(URITemplates.VIEW_NOTE);
	uriBuilder.queryParam("successful");
	return uriBuilder.buildAndExpand(id).toUriString();
    }

}
