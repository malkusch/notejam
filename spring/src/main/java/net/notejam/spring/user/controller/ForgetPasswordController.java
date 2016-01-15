package net.notejam.spring.user.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.notejam.spring.URITemplates;
import net.notejam.spring.user.recovery.PasswordRecoveryService;

/**
 * A forget password controller.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Controller
@RequestMapping(URITemplates.FORGOT_PASSWORD)
public class ForgetPasswordController {

    /**
     * The password recovery service.
     */
    @Autowired
    private PasswordRecoveryService recoveryService;

    /**
     * Shows the form.
     *
     * @param forgotPassword
     *            command for the view model
     *
     * @return view name
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showForm(final ForgetPassword forgotPassword) {
	return "user/forgot-password";
    }

    /**
     * Starts the recovery process.
     *
     * This will create a token and send an email to finalize the process with
     * the token.
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
    @RequestMapping(method = RequestMethod.POST)
    public String startPasswordRecoveryProcess(@Valid final ForgetPassword forgetPassword, final Errors errors,
	    final HttpServletRequest request, final Locale locale) throws URISyntaxException {

	if (errors.hasErrors()) {
	    return showForm(forgetPassword);
	}

	URI baseUri = new URI(request.getRequestURL().toString());
	recoveryService.startRecoveryProcess(forgetPassword.getEmailAddress(), baseUri, locale);

	return String.format("redirect:%s?success", URITemplates.FORGOT_PASSWORD);
    }

}
