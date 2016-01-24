package net.notejam.spring.presentation.web.account;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import de.malkusch.validation.constraints.EqualProperties;
import net.notejam.spring.presentation.web.constraints.Password;

/**
 * A command for signing up a user.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@EqualProperties(value = { "repeatedPassword", "password" }, violationOnPropery = true)
public interface SignupUser {

    /**
     * Returns the email address.
     *
     * @return The email address.
     */
    @NotEmpty
    @Email
    @Size(max = 75)
    String getEmailAddress();

    /**
     * Returns the plain text password.
     *
     * @return unencrypted password in plain text
     */
    @Password
    String getPassword();

    /**
     * Returns the repeated password.
     *
     * @return The repeated password.
     */
    @NotNull
    String getRepeatedPassword();

}
