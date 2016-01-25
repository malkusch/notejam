package net.notejam.spring.presentation.web;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.notejam.spring.application.NoteService;
import net.notejam.spring.domain.Name;
import net.notejam.spring.domain.Note;
import net.notejam.spring.domain.Pad;
import net.notejam.spring.infrastructure.error.ResourceNotFoundException;
import net.notejam.spring.presentation.URITemplates;
import net.notejam.spring.presentation.web.PadsAdvice.Pads;

/**
 * The edit note controller.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Controller
@RequestMapping(URITemplates.EDIT_NOTE)
@Pads
final class EditNoteController {

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
    EditNoteController(final NoteService service) {
        this.service = service;
    }

    /**
     * Provides the model attribute "name".
     *
     * @param id
     *            id of the currently edited note
     * @return note name
     */
    @ModelAttribute
    Name name(@PathVariable("id") final int id) {
        Note note = service.showNote(id).orElseThrow(() -> new ResourceNotFoundException());
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
    String showEditNoteForm(@PathVariable("id") final Note note, final NoteCommand editNote) {

        editNote.setName(note.getName().toString());
        editNote.setText(note.getText());
        editNote.setPadId(note.getPad().flatMap((Pad pad) -> Optional.of(pad.getId())));

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
    String editNote(@PathVariable("id") final int id, @Valid final NoteCommand editNote, final Errors errors) {

        if (errors.hasErrors()) {
            return "note/edit";
        }

        service.editNote(id, new Name(editNote.getName()), editNote.getPadId(), editNote.getText());

        return String.format("redirect:%s", URITemplates.buildEditedNoteURI(id));
    }

}
