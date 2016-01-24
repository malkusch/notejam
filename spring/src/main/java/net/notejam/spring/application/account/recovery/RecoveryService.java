package net.notejam.spring.application.account.recovery;

import java.net.URI;
import java.time.Instant;
import java.util.Locale;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import net.notejam.spring.domain.account.ChangePasswordService;
import net.notejam.spring.domain.account.EmailAddress;
import net.notejam.spring.domain.account.recovery.InvalidPasswordRecoveryProcessException;
import net.notejam.spring.domain.account.recovery.PasswordRecoveryProcess;
import net.notejam.spring.domain.account.recovery.PasswordRecoveryProcessFactory;
import net.notejam.spring.domain.account.recovery.PasswordRecoveryProcessRepository;
import net.notejam.spring.domain.account.recovery.PasswordRecoveryToken;
import net.notejam.spring.presentation.URITemplates;
import net.notejam.spring.presentation.mail.PasswordRecoveryMailer;

/**
 * The password recovery service.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Service
@Transactional
public class RecoveryService {

    /**
     * The password recovery process factory.
     */
    final PasswordRecoveryProcessFactory processFactory;

    /**
     * The password change password service.
     */
    private final ChangePasswordService changePasswordService;

    /**
     * The token repository.
     */
    private final PasswordRecoveryProcessRepository tokenRepository;

    /**
     * The mailer.
     */
    final PasswordRecoveryMailer mailer;

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RecoveryService.class);

    /**
     * Builds the service with its dependencies.
     * 
     * @param processFactory
     *            password recovery process factory
     * @param tokenRepository
     *            password recovery token repository
     * @param changePasswordService
     *            password change password service
     * @param mailer
     *            password recovery mailer
     */
    @Autowired
    RecoveryService(final PasswordRecoveryProcessFactory processFactory,
	    final PasswordRecoveryProcessRepository tokenRepository, final ChangePasswordService changePasswordService,
	    final PasswordRecoveryMailer mailer) {

	this.processFactory = processFactory;
	this.tokenRepository = tokenRepository;
	this.changePasswordService = changePasswordService;
	this.mailer = mailer;
    }

    /**
     * Starts the password recovery process.
     * 
     * @param emailAddress
     *            Email address of the account
     * @param baseUri
     *            base URI. It is used for building links in emails.
     * @param locale
     *            locale in which the process should happen
     */
    public void startPasswordRecovery(final EmailAddress emailAddress, final URI baseUri, final Locale locale) {
	try {
	    PasswordRecoveryProcess process = processFactory.buildProcess(emailAddress);
	    URI uri = URITemplates.buildPasswordRecoveryURI(process, baseUri);
	    mailer.sendRecoveryMail(emailAddress, uri, locale);

	} catch (IllegalArgumentException e) {
	    LOGGER.info("Cancel password recovery for non existing user {}", emailAddress);
	}
    }

    /**
     * Changes the password of a user to a new generated password.
     *
     * @param processId
     *            password recovery process id
     * @param token
     *            password recovery token
     * @return The new generated password
     * @throws InvalidPasswordRecoveryProcessException
     *             The token was not valid
     */
    public String changePassword(final int processId, final PasswordRecoveryToken token)
	    throws InvalidPasswordRecoveryProcessException {

	return changePasswordService.changePassword(processId, token);
    }

    /**
     * Deletes expired tokens from the storage.
     */
    @Scheduled(cron = "59 59 3 * * *")
    public void purgeExpired() {
	LOGGER.info("Purge expired recovery tokens");
	tokenRepository.deleteByExpirationLessThan(Instant.now());
    }

}
