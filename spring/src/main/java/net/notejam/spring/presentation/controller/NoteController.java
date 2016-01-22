package net.notejam.spring.presentation.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import net.notejam.spring.application.NoteService;
import net.notejam.spring.domain.Name;
import net.notejam.spring.domain.Note;
import net.notejam.spring.domain.Pad;
import net.notejam.spring.infrastructure.error.ResourceNotFoundException;
import net.notejam.spring.infrastructure.security.AuthenticatedUser;
import net.notejam.spring.presentation.URITemplates;
import net.notejam.spring.presentation.controller.PadsAdvice.Pads;

/**
 * A note controller.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Controller
@Pads
final class NoteController {

    /**
     * The note service.
     */
    private final NoteService service;

    /**
     * Builds the controller with its dependencies.
     *
     * @param noteService
     *            note service
     */
    @Autowired
    NoteController(final NoteService noteService) {
	this.service = noteService;
    }

    /**
     * Shows the note
     *
     * @return The note view
     */
    @RequestMapping(URITemplates.VIEW_NOTE)
    String viewNote(@PathVariable("id") final int id, final Model model) {
	
	Note note = service.showNote(id).orElseThrow(() -> new ResourceNotFoundException());
	model.addAttribute(note);
	
	return "note/view";
    }
    
    /**
     * Shows the confirmation for deleting a note.
     *
     * @return The view
     */
    @RequestMapping(method = RequestMethod.GET, path = URITemplates.DELETE_NOTE)
    String confirmDeleteNote(@PathVariable("id") final int id, final Model model) {

	Note note = service.showNote(id).orElseThrow(() -> new ResourceNotFoundException());
	model.addAttribute(note);

	return "note/delete";
    }

    /**
     * Deletes a note.
     *
     * @param note
     *            The note.
     * @return The view
     */
    @RequestMapping(method = RequestMethod.POST, path = URITemplates.DELETE_NOTE)
    String deleteNote(@PathVariable("id") final int id) {
	service.deleteNote(id);
	return String.format("redirect:%s?deleted", URITemplates.CREATE_NOTE);
    }

    /**
     * Shows the form for writing a new note.
     *
     * @param createNote
     *            note command
     * @param padId
     *            preselected pad id
     *
     * @return The view
     */
    @RequestMapping(method = RequestMethod.GET, path = URITemplates.CREATE_NOTE)
    String showWriteNoteForm(final NoteCommand createNote,
	    @RequestParam(value = "pad", required = false) final Optional<Integer> padId) {

	createNote.setPadId(padId);

	return "note/create";
    }

    /**
     * Writes a new note.
     *
     * @param createNote
     *            note command
     * @param errors
     *            The validation result.
     *
     * @return The view
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = RequestMethod.POST, path = URITemplates.CREATE_NOTE)
    String writeNote(@AuthenticationPrincipal final AuthenticatedUser authenticatedUser,
	    final @Valid NoteCommand createNote, final Errors errors) {

	if (errors.hasErrors()) {
	    return showWriteNoteForm(createNote, createNote.getPadId());
	}

	Note note = service.writeNote(new Name(createNote.getName()), authenticatedUser.getUser(),
		createNote.getPadId(), createNote.getText());

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

    /**
     * Shows the pad notes
     *
     * @param pad
     *            pad
     * @return The pad notes view
     */
    @RequestMapping(URITemplates.VIEW_PAD)
    String browseNotes(@PathVariable("id") final Pad pad, @PageableDefault(10) final Pageable pageable,
	    final Model model) {

	Page<Note> notes = service.browseNotes(pad, pageable);
	model.addAttribute("notes", notes);
	model.addAttribute(pad);

	return "notes";
    }

    /**
     * Shows all notes.
     * 
     * @param authenticatedUser
     *            authenticated user
     * @param pageable
     *            The paging.
     * @param model
     *            view model
     *
     * @return The view.
     */
    @RequestMapping(URITemplates.VIEW_ALL_NOTES)
    @PreAuthorize("isAuthenticated()")
    String browseNotes(@AuthenticationPrincipal final AuthenticatedUser authenticatedUser,
	    @PageableDefault(10) final Pageable pageable, final Model model) {

	Page<Note> notes = service.browseNotes(authenticatedUser.getUser(), pageable);
	model.addAttribute("notes", notes);

	return "notes";
    }

}
