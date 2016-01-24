package net.notejam.spring.domain.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.notejam.spring.domain.account.security.PasswordEncodingService;
import net.notejam.spring.domain.account.security.PlainTextPassword;

/**
 * A service for changing a password of a user.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Service
public class ChangePasswordService {

    /**
     * The password encoding service.
     */
    private final PasswordEncodingService encodingService;

    /**
     * Builds this service.
     * 
     * @param encodingService
     *            password encoding service
     */
    @Autowired
    ChangePasswordService(final PasswordEncodingService encodingService) {
	this.encodingService = encodingService;
    }

    /**
     * Changes the password of a user.
     * 
     * This method needs the current password to verify the authenticity of the
     * request.
     * 
     * @param user
     *            user whose password will be changed
     * @param oldPassword
     *            current password of the user for verification
     * @param newPassword
     *            new password of the user
     * @throws WrongPasswordException
     *             if the current password does not match for the user
     */
    public void changePassword(final User user, final PlainTextPassword oldPassword,
	    final PlainTextPassword newPassword) throws WrongPasswordException {

	if (!encodingService.matches(oldPassword, user.getPassword())) {
	    throw new WrongPasswordException();
	}
	changePassword(user, newPassword);
    }

    /**
     * Changes the password of a user.
     * 
     * @param user
     *            user whose password will be changed
     * @param password
     *            new password of the user
     */
    public void changePassword(final User user, final PlainTextPassword password) {
	user.changePassword(encodingService.encode(password));
    }

}
