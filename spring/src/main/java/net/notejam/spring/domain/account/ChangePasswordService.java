package net.notejam.spring.domain.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.notejam.spring.domain.account.recovery.InvalidTokenException;
import net.notejam.spring.domain.account.recovery.RecoveryToken;
import net.notejam.spring.domain.account.recovery.RecoveryTokenRepository;
import net.notejam.spring.domain.account.security.PasswordEncodingService;
import net.notejam.spring.domain.account.security.PlainTextPassword;
import net.notejam.spring.infrastructure.security.RandomStringGenerator;

/**
 * A service for changing a password of a user.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Service
public class ChangePasswordService {

    /**
     * The token repository.
     */
    private final RecoveryTokenRepository tokenRepository;

    /**
     * The password generator.
     */
    private final RandomStringGenerator passwordGenerator;

    /**
     * The password encoding service.
     */
    private final PasswordEncodingService encodingService;

    /**
     * Builds this service.
     * 
     * @param encodingService
     *            password encoding service
     * @param tokenTepository
     *            token repository
     * @param passwordGenerator
     *            password generator
     */
    @Autowired
    ChangePasswordService(final PasswordEncodingService encodingService, final RecoveryTokenRepository tokenTepository,
	    final RandomStringGenerator passwordGenerator) {

	this.encodingService = encodingService;
	this.tokenRepository = tokenTepository;
	this.passwordGenerator = passwordGenerator;
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

	user.changePassword(encodingService.encode(newPassword));
    }

    /**
     * Changes the password of a user to a generated password in exchange for a
     * valid token.
     *
     * @param tokenId
     *            token id
     * @param token
     *            token
     * @return new generated plain text password
     * @throws InvalidTokenException
     *             if the token was not valid
     */
    public String changePassword(final int tokenId, final String token) throws InvalidTokenException {
	RecoveryToken recoveryToken = tokenRepository.findOne(tokenId);

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

	user.changePassword(encodingService.encode(new PlainTextPassword(password)));
	tokenRepository.delete(recoveryToken);

	return password;
    }

}
