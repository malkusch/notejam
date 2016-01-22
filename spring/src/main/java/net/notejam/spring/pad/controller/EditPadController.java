package net.notejam.spring.pad.controller;

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
import net.notejam.spring.pad.Name;
import net.notejam.spring.pad.Pad;
import net.notejam.spring.pad.PadService;
import net.notejam.spring.pad.controller.PadsAdvice.Pads;

/**
 * The edit pad controller.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Controller
@RequestMapping(URITemplates.EDIT_PAD)
@PreAuthorize("isAuthenticated()")
@Pads
public class EditPadController {

    /**
     * The pad service.
     */
    private final PadService service;

    /**
     * Builds the controller with its dependencies.
     * 
     * @param service
     *            pad service
     */
    @Autowired
    EditPadController(final PadService service) {
	this.service = service;
    }

    /**
     * Provides the view model attribute "pad".
     *
     * @param id
     *            pad id
     * @return pad to edit
     */
    @ModelAttribute
    public Pad pad(@PathVariable("id") final int id) {
	return service.getPad(id);
    }

    /**
     * Shows the form for creating a pad.
     *
     * @param pad
     *            pad
     * @param editPad
     *            Edit pad command
     *
     * @return view name
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showCreatePadForm(final Pad pad, final EditPad editPad) {
	editPad.setName(pad.getName().toString());
	return "pad/edit";
    }

    /**
     * Shows the form for creating a pad.
     *
     * @param pad
     *            pad
     * @param editPad
     *            command for editing the pad
     * @param errors
     *            result of the form validation
     * @return view name
     */
    @RequestMapping(method = RequestMethod.POST)
    public String editPad(final Pad pad, @Valid final EditPad editPad, final Errors errors) {

	if (errors.hasErrors()) {
	    return showCreatePadForm(pad, editPad);
	}

	service.editPad(pad, new Name(editPad.getName()));

	return String.format("redirect:%s", buildEditedPadUri(pad.getId()));
    }

    /**
     * Builds the URI for the edited pad.
     *
     * @param id
     *            pad id
     * @return uri
     */
    private static String buildEditedPadUri(final int id) {
	UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(URITemplates.EDIT_PAD);
	uriBuilder.queryParam("success");
	return uriBuilder.buildAndExpand(id).toUriString();
    }

}
