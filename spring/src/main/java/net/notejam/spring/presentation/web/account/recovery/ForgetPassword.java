package net.notejam.spring.presentation.web.account.recovery;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * A command for forgetting the password.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
interface ForgetPassword {

    /**
     * Returns the email address.
     *
     * @return email address
     */
    @Email
    @NotEmpty
    String getEmailAddress();

}
