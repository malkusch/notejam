package net.notejam.spring.presentation.web.account;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.notejam.spring.application.account.UserService;
import net.notejam.spring.domain.account.EmailAddress;
import net.notejam.spring.domain.account.EmailAddressExistsException;
import net.notejam.spring.domain.account.WrongPasswordException;
import net.notejam.spring.domain.account.security.PlainTextPassword;
import net.notejam.spring.presentation.URITemplates;

/**
 * An user controller.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Controller
final class UserController {

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
    UserController(final UserService userService) {
	this.userService = userService;
    }

    /**
     * Shows the form.
     *
     * @param changePassword
     *            command for the view model
     * @return view name
     */
    @RequestMapping(method = RequestMethod.GET, path = URITemplates.SETTINGS)
    String showChangePasswordForm(final ChangePassword changePassword) {
        return "user/account";
    }

    /**
     * Changes the password.
     *
     * @param changePassword
     *            command from the view
     * @param errors
     *            form validation errors
     * @return view name
     */
    @RequestMapping(method = RequestMethod.POST, path = URITemplates.SETTINGS)
    String changePassword(@Valid final ChangePassword changePassword, final Errors errors) {

        if (errors.hasErrors()) {
            return showChangePasswordForm(changePassword);
        }

        try {
            PlainTextPassword newPassword = new PlainTextPassword(changePassword.getNewPassword());
            PlainTextPassword oldPassword = new PlainTextPassword(changePassword.getCurrentPassword());
            userService.changePassword(oldPassword, newPassword);
    
            return String.format("redirect:%s?success", URITemplates.SETTINGS);
            
        } catch (WrongPasswordException e) {
            errors.rejectValue("currentPassword", "CurrentPassword");
            return showChangePasswordForm(changePassword);
        }
    }
    
    /**
     * Shows the sign up form.
     *
     * @param signupUser
     *            command for the view model
     * @return view name
     */
    @RequestMapping(method = RequestMethod.GET, path = URITemplates.SIGNUP)
    String showSignUpForm(final SignupUser signupUser) {
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
    @RequestMapping(method = RequestMethod.POST, path = URITemplates.SIGNUP)
    String signUp(@Valid final SignupUser signupUser, final Errors errors) {

	if (errors.hasErrors()) {
	    return showSignUpForm(signupUser);
	}

	try {
	    userService.signUp(new EmailAddress(signupUser.getEmailAddress()), new PlainTextPassword(signupUser.getPassword()));
	    return String.format("redirect:%s?signup", URITemplates.SIGNIN);
	    
	} catch (EmailAddressExistsException e) {
	    errors.rejectValue("emailAddress", "UniqueEmail");
	    return showSignUpForm(signupUser);
	}
    }

}
