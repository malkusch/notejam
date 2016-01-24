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
import net.notejam.spring.domain.account.recovery.InvalidTokenException;
import net.notejam.spring.domain.account.recovery.PasswordRecoveryStartService;
import net.notejam.spring.domain.account.recovery.RecoveryTokenRepository;

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
     * The password recovery start service.
     */
    private final PasswordRecoveryStartService startRecoveryService;

    /**
     * The password change password service.
     */
    private final ChangePasswordService changePasswordService;

    /**
     * The token repository.
     */
    private final RecoveryTokenRepository tokenRepository;

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RecoveryService.class);

    /**
     * Builds the service with its dependencies.
     * 
     * @param startRecoveryService
     *            password recovery start service
     * @param tokenRepository
     *            password recovery token repository
     * @param changePasswordService
     *            password change password service
     */
    @Autowired
    RecoveryService(final PasswordRecoveryStartService startRecoveryService,
	    final RecoveryTokenRepository tokenRepository, final ChangePasswordService changePasswordService) {

	this.startRecoveryService = startRecoveryService;
	this.tokenRepository = tokenRepository;
	this.changePasswordService = changePasswordService;
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
	startRecoveryService.startPasswordRecovery(emailAddress, baseUri, locale);
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
	return changePasswordService.changePassword(tokenId, token);
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
