package net.notejam.spring.presentation.web.account.recovery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import net.notejam.spring.application.account.recovery.RecoveryService;
import net.notejam.spring.domain.account.EmailAddress;
import net.notejam.spring.domain.account.recovery.InvalidPasswordRecoveryProcessException;
import net.notejam.spring.domain.account.recovery.PasswordRecoveryToken;
import net.notejam.spring.presentation.URITemplates;

/**
 * A forget password controller.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Controller
final class RecoveryController {

    /**
     * The password recovery service.
     */
    private final RecoveryService recoveryService;

    /**
     * Builds the controller with its dependencies.
     * 
     * @param recoveryService
     *            recovery service
     */
    @Autowired
    RecoveryController(final RecoveryService recoveryService) {
	this.recoveryService = recoveryService;
    }

    /**
     * Shows the form.
     *
     * @param forgotPassword
     *            command for the view model
     *
     * @return view name
     */
    @RequestMapping(method = RequestMethod.GET, path = URITemplates.FORGOT_PASSWORD)
    String showForm(final ForgetPassword forgotPassword) {
	return "user/forgot-password";
    }

    /**
     * Starts the recovery process.
     *
     * This will send an email to finalize the process.
     *
     * @param forgetPassword
     *            command from the view
     * @param errors
     *            result of the form validation
     * @param request
     *            HTTP request
     * @param locale
     *            locale
     * 
     * @return view name
     */
    @RequestMapping(method = RequestMethod.POST, path = URITemplates.FORGOT_PASSWORD)
    String startPasswordRecovery(@Valid final ForgetPassword forgetPassword, final Errors errors,
	    final HttpServletRequest request, final Locale locale) throws URISyntaxException {

	if (errors.hasErrors()) {
	    return showForm(forgetPassword);
	}

	URI baseUri = new URI(request.getRequestURL().toString());
	recoveryService.startPasswordRecovery(new EmailAddress(forgetPassword.getEmailAddress()), baseUri, locale);

	return String.format("redirect:%s?success", URITemplates.FORGOT_PASSWORD);
    }

    /**
     * Changes the password to a generated new one and shows it to the user.
     *
     * @param id
     *            process id
     * @param token
     *            token
     * @param password
     *            generated password for the model
     * @return view name
     * @throws InvalidPasswordRecoveryProcessException
     *             The token did not match.
     */
    @RequestMapping(URITemplates.RECOVER_PASSWORD)
    String changePassword(@PathVariable("id") final int id, @PathVariable("token") final String token,
	    @ModelAttribute("password") final StringBuilder password) throws InvalidPasswordRecoveryProcessException {

	password.append(recoveryService.changePassword(id, new PasswordRecoveryToken(token)));
	return "user/reveal-password";
    }

    /**
     * Handles non matching tokens.
     *
     * @return The view.
     */
    @ExceptionHandler(InvalidPasswordRecoveryProcessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String handleInvalidToken() {
	return "error";
    }

}
