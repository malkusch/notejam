package net.notejam.spring.pad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.notejam.spring.URITemplates;
import net.notejam.spring.pad.Pad;
import net.notejam.spring.pad.PadService;
import net.notejam.spring.pad.controller.PadsAdvice.Pads;

/**
 * The delete pad controller.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Controller
@RequestMapping(URITemplates.DELETE_PAD)
@PreAuthorize("isAuthenticated()")
@Pads
public class DeletePadController {

    /**
     * The pad service.
     */
    @Autowired
    private PadService service;

    /**
     * Provides the view model attribute "pad".
     *
     * @param id
     *            pad id
     * @return pad to delete
     */
    @ModelAttribute
    public Pad pad(@PathVariable("id") final int id) {
        return service.getPad(id);
    }

    /**
     * Shows the confirmation for deleting a pad.
     *
     * @return view name
     */
    @RequestMapping(method = RequestMethod.GET)
    public String confirmDeletePad() {
        return "pad/delete";
    }

    /**
     * Deletes a pad and its notes.
     *
     * @param id
     *            pad id
     * @return view name
     */
    @RequestMapping(method = RequestMethod.POST)
    public String deletePad(@PathVariable("id") final int id) {
        service.deletePad(id);
        return String.format("redirect:%s?deleted", URITemplates.CREATE_PAD);
    }

}
