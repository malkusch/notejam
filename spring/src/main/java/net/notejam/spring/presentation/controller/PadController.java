package net.notejam.spring.presentation.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import net.notejam.spring.application.PadService;
import net.notejam.spring.domain.Name;
import net.notejam.spring.domain.Pad;
import net.notejam.spring.infrastructure.security.AuthenticatedUser;
import net.notejam.spring.presentation.URITemplates;
import net.notejam.spring.presentation.controller.PadsAdvice.Pads;

/**
 * A pad controller.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Controller
@Pads
final class PadController {

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
    PadController(final PadService service) {
	this.service = service;
    }

    /**
     * Shows the form for creating a pad.
     *
     * @param createPad
     *            command for the view model
     * @return view name
     */
    @RequestMapping(method = RequestMethod.GET, path = URITemplates.CREATE_PAD)
    String showCreatePad(final CreatePad createPad) {
	return "pad/create";
    }

    /**
     * Creates a new pad.
     *
     * @param createPad
     *            command from the view
     * @param errors
     *            result of the form validation
     * @param authenticatedUser
     *            authenticated user
     * @return view name
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = RequestMethod.POST, path = URITemplates.CREATE_PAD)
    String createPad(@Valid final CreatePad createPad, final Errors errors,
	    @AuthenticationPrincipal final AuthenticatedUser authenticatedUser) {

	if (errors.hasErrors()) {
	    return showCreatePad(createPad);
	}

	Pad pad = service.createPad(new Name(createPad.getName()), authenticatedUser.getUser());

	return String.format("redirect:%s", buildCreatedPadUri(pad.getId()));
    }
    
    /**
     * Shows the confirmation for deleting a pad.
     *
     * @return view name
     */
    @RequestMapping(method = RequestMethod.GET, path = URITemplates.DELETE_PAD)
    String showDeletePad(@PathVariable("id") final int id, final Model model) {
	model.addAttribute(service.showPad(id));
	return "pad/delete";
    }

    /**
     * Deletes a pad and its notes.
     *
     * @param id
     *            pad id
     * @return view name
     */
    @RequestMapping(method = RequestMethod.POST, path = URITemplates.DELETE_PAD)
    String deletePad(@PathVariable("id") final int id) {
	service.deletePad(id);
	return String.format("redirect:%s?deleted", URITemplates.CREATE_PAD);
    }

    /**
     * Builds the URI for the created pad.
     *
     * @param id
     *            The pad id
     * @return The URI
     */
    private static String buildCreatedPadUri(final int id) {
	UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(URITemplates.VIEW_PAD);
	uriBuilder.queryParam("createdSuccessfully");
	return uriBuilder.buildAndExpand(id).toUriString();
    }

}
