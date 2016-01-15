package net.notejam.spring.user.controller;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import de.malkusch.validation.constraints.EqualProperties;
import net.notejam.spring.user.constraints.Password;
import net.notejam.spring.user.constraints.UniqueEmail;

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
    @UniqueEmail
    @Size(max = 75)
    public String getEmailAddress();

    /**
     * Returns the plain text password.
     *
     * @return unencrypted password in plain text
     */
    @Password
    public String getPassword();

    /**
     * Returns the repeated password.
     *
     * @return The repeated password.
     */
    @NotNull
    public String getRepeatedPassword();

}
