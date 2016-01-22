package net.notejam.spring.pad.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import net.notejam.spring.URITemplates;
import net.notejam.spring.pad.Name;
import net.notejam.spring.pad.Pad;
import net.notejam.spring.pad.PadService;
import net.notejam.spring.pad.controller.PadsAdvice.Pads;

/**
 * The create pad controller.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Controller
@RequestMapping(URITemplates.CREATE_PAD)
@PreAuthorize("isAuthenticated()")
@Pads
public class CreatePadController {

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
    CreatePadController(final PadService service) {
	this.service = service;
    }

    /**
     * Shows the form for creating a pad.
     *
     * @param createPad
     *            command for the view model
     * @return view name
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showCreatePadForm(final CreatePad createPad) {
	return "pad/create";
    }

    /**
     * Creates a new pad.
     *
     * @param createPad
     *            command from the view
     * @param errors
     *            result of the form validation
     * @return view name
     */
    @RequestMapping(method = RequestMethod.POST)
    public String createPad(@Valid final CreatePad createPad, final Errors errors) {

	if (errors.hasErrors()) {
	    return showCreatePadForm(createPad);
	}

	Pad pad = service.createPad(new Name(createPad.getName()));

	return String.format("redirect:%s", buildCreatedPadUri(pad.getId()));
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
