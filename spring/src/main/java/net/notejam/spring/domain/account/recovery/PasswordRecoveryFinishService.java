package net.notejam.spring.domain.account.recovery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.notejam.spring.domain.account.ChangePasswordService;
import net.notejam.spring.domain.account.User;
import net.notejam.spring.domain.account.security.PlainTextPassword;
import net.notejam.spring.infrastructure.security.RandomStringGenerator;

/**
 * Service for recovering the password.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Service
public class PasswordRecoveryFinishService {

    /**
     * The token repository.
     */
    private final RecoveryTokenRepository repository;

    /**
     * The password generator.
     */
    private final RandomStringGenerator passwordGenerator;

    /**
     * The change password service.
     */
    private final ChangePasswordService changePasswordService;

    /**
     * Builds the service
     * 
     * @param repository
     *            token repository
     * @param passwordGenerator
     *            password generator
     * @param changePasswordService
     *            change password service
     */
    @Autowired
    PasswordRecoveryFinishService(final RecoveryTokenRepository repository,
	    final RandomStringGenerator passwordGenerator, final ChangePasswordService changePasswordService) {

	this.repository = repository;
	this.passwordGenerator = passwordGenerator;
	this.changePasswordService = changePasswordService;
    }

    /**
     * Recovers the password in exchange for a valid token.
     *
     * @param tokenId
     *            The token id
     * @param token
     *            The token string
     * @return The new password
     * @throws InvalidTokenException
     *             The token was not valid
     */
    public String finishPasswordRecovery(final int tokenId, final String token) throws InvalidTokenException {
	RecoveryToken recoveryToken = repository.findOne(tokenId);

	if (recoveryToken == null) {
	    throw new InvalidTokenException("Token id is invalid.");
	}
	if (recoveryToken.isExpired()) {
	    throw new InvalidTokenException("Token is expired.");
	}
	if (!recoveryToken.getToken().equals(token)) {
	    throw new InvalidTokenException("Token doesn't match.");
	}

	String password = passwordGenerator.generatePassword();
	User user = recoveryToken.getUser();

	changePasswordService.changePassword(user, new PlainTextPassword(password));
	repository.delete(recoveryToken);

	return password;
    }

}
