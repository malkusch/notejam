package net.notejam.spring.note.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import net.notejam.spring.URITemplates;
import net.notejam.spring.error.ResourceNotFoundException;
import net.notejam.spring.note.Note;
import net.notejam.spring.note.NoteService;
import net.notejam.spring.pad.Name;
import net.notejam.spring.pad.Pad;
import net.notejam.spring.pad.PadService;
import net.notejam.spring.pad.controller.PadsAdvice.Pads;

/**
 * The edit note controller.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Controller
@RequestMapping(URITemplates.EDIT_NOTE)
@PreAuthorize("isAuthenticated()")
@Pads
public class EditNoteController {

    /**
     * The note service.
     */
    private final NoteService service;

    /**
     * The pad service.
     */
    private final PadService padService;

    /**
     * Builds the controller with its dependencies.
     *
     * @param service
     *            note service
     * @param padService
     *            pad service
     */
    @Autowired
    EditNoteController(final NoteService service, final PadService padService) {
	this.service = service;
	this.padService = padService;
    }

    /**
     * Provides the model attribute "name".
     *
     * @param note
     *            The currently edited note
     * @return note name
     */
    @ModelAttribute
    public Name name(@PathVariable("id") final Note note) {
	return note.getName();
    }

    /**
     * Shows the form for editing a note.
     *
     * @param note
     *            currently edited note
     * @param editNote
     *            note command
     *
     * @return The view
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showEditNoteForm(@PathVariable("id") final Note note, final NoteCommand editNote) {

	editNote.setName(note.getName().toString());
	editNote.setText(note.getText());

	if (note.getPad() != null) {
	    editNote.setPadId(Optional.of(note.getPad().getId()));
	} else {
	    editNote.setPadId(Optional.empty());
	}

	return "note/edit";
    }

    /**
     * Edits a new note.
     *
     * @param id
     *            id of the currently edited note
     * @param editNote
     *            note command
     * @param errors
     *            validation errors
     * 
     * @return The view
     */
    @RequestMapping(method = RequestMethod.POST)
    public String editNote(@PathVariable("id") final int id, @Valid final NoteCommand editNote, final Errors errors) {

	if (errors.hasErrors()) {
	    return "note/edit";
	}

	Note note = service.getNote(id).orElseThrow(() -> new ResourceNotFoundException());

	note.setName(new Name(editNote.getName()));
	note.setText(editNote.getText());

	Pad pad = null;
	if (editNote.getPadId().isPresent()) {
	    pad = padService.getPad(editNote.getPadId().get());
	}

	service.saveNote(note, pad);

	return String.format("redirect:%s", buildEditedNoteUri(note.getId()));
    }

    /**
     * Builds the URI for the edited note.
     *
     * @param id
     *            The note id
     * @return The URI
     */
    private static String buildEditedNoteUri(final int id) {
	UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(URITemplates.VIEW_NOTE);
	uriBuilder.queryParam("successful");
	return uriBuilder.buildAndExpand(id).toUriString();
    }

}
