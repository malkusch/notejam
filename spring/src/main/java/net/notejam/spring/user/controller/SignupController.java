package net.notejam.spring.user.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.notejam.spring.URITemplates;
import net.notejam.spring.user.UserService;

/**
 * A sign up controller.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Controller
@RequestMapping(URITemplates.SIGNUP)
public class SignupController {

    /**
     * The user service.
     */
    private final UserService userService;

    /**
     * Builds the controller with its dependencies.
     * 
     * @param userService
     *            user service
     */
    @Autowired
    SignupController(final UserService userService) {
	this.userService = userService;
    }

    /**
     * Shows the sign up form.
     *
     * @param signupUser
     *            command for the view model
     * @return view name
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showForm(final SignupUser signupUser) {
	return "user/signup";
    }

    /**
     * Signs up a user.
     *
     * @param signupUser
     *            command from the view
     * @param errors
     *            result of the form validation
     * @return view name
     */
    @RequestMapping(method = RequestMethod.POST)
    public String signup(@Valid final SignupUser signupUser, final Errors errors) {

	if (errors.hasErrors()) {
	    return showForm(signupUser);
	}

	userService.signUp(signupUser.getEmailAddress(), signupUser.getPassword());

	return String.format("redirect:%s?signup", URITemplates.SIGNIN);
    }

}
