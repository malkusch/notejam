package net.notejam.spring.domain.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.notejam.spring.domain.account.recovery.InvalidPasswordRecoveryProcessException;
import net.notejam.spring.domain.account.recovery.PasswordRecoveryProcess;
import net.notejam.spring.domain.account.recovery.PasswordRecoveryProcessRepository;
import net.notejam.spring.domain.account.recovery.PasswordRecoveryToken;
import net.notejam.spring.domain.account.security.EncodedPassword;
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
     * The recovery process repository.
     */
    private final PasswordRecoveryProcessRepository processRepository;

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
     * @param processRepository
     *            recovery process repository
     * @param passwordGenerator
     *            password generator
     */
    @Autowired
    ChangePasswordService(final PasswordEncodingService encodingService,
            final PasswordRecoveryProcessRepository processRepository, final RandomStringGenerator passwordGenerator) {

        this.encodingService = encodingService;
        this.processRepository = processRepository;
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
     * Changes the password of a user to a generated password.
     *
     * This change needs a valid {@link PasswordRecoveryProcess}.
     *
     * @param processId
     *            password recovery process id
     * @param token
     *            password recovery token
     * @return new generated plain text password
     * @throws InvalidPasswordRecoveryProcessException
     *             if the password recovery process was not valid
     */
    public String changePassword(final int processId, final PasswordRecoveryToken token)
            throws InvalidPasswordRecoveryProcessException {

        PasswordRecoveryProcess recoveryProcess = processRepository.findOne(processId);

        if (recoveryProcess == null) {
            throw new InvalidPasswordRecoveryProcessException("Process id is invalid.");
        }
        if (recoveryProcess.isExpired()) {
            throw new InvalidPasswordRecoveryProcessException("Process is expired.");
        }
        if (!recoveryProcess.token().equals(token)) {
            throw new InvalidPasswordRecoveryProcessException("Token doesn't match.");
        }

        String password = passwordGenerator.generatePassword();
        EncodedPassword encodedPassword = encodingService.encode(new PlainTextPassword(password));

        User user = recoveryProcess.user();
        user.changePassword(encodedPassword);

        processRepository.delete(recoveryProcess);

        return password;
    }

}
