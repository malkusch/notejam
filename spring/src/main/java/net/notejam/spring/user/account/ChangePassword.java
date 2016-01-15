package net.notejam.spring.user.account;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import de.malkusch.validation.constraints.EqualProperties;
import net.notejam.spring.user.account.constraints.CurrentPassword;
import net.notejam.spring.user.constraints.Password;

/**
 * A command for changing the password.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@EqualProperties(value = { "repeatedPassword", "newPassword" }, violationOnPropery = true)
public interface ChangePassword {

    /**
     * Returns the current password.
     *
     * @return The current password.
     */
    @NotEmpty
    @CurrentPassword
    public String getCurrentPassword();

    /**
     * Returns the new password.
     *
     * @return The new password.
     */
    @Password
    public String getNewPassword();

    /**
     * Returns the new and repeated password.
     *
     * @return The repeated password.
     */
    @NotNull
    public String getRepeatedPassword();

}
