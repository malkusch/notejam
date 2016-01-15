package net.notejam.spring.user.account;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.notejam.spring.URITemplates;
import net.notejam.spring.user.UserService;

/**
 * An account controller.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Controller
@RequestMapping(URITemplates.SETTINGS)
@PreAuthorize("isAuthenticated()")
public class AccountController {

    /**
     * The user service.
     */
    @Autowired
    private UserService userService;

    /**
     * Shows the form.
     *
     * @param changePassword
     *            command for the view model
     * @return view name
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showForm(final ChangePassword changePassword) {
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
    @RequestMapping(method = RequestMethod.POST)
    public String changePassword(@Valid final ChangePassword changePassword, final Errors errors) {

        if (errors.hasErrors()) {
            return showForm(changePassword);
        }

        userService.changePassword(changePassword.getNewPassword());

        return String.format("redirect:%s?success", URITemplates.SETTINGS);
    }

}
