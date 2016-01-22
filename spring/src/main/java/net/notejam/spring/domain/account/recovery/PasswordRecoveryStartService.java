package net.notejam.spring.domain.account.recovery;

import java.net.URI;
import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.notejam.spring.domain.account.EmailAddress;
import net.notejam.spring.domain.account.User;
import net.notejam.spring.domain.account.UserRepository;
import net.notejam.spring.presentation.URITemplates;
import net.notejam.spring.presentation.mail.PasswordRecoveryMailer;

/**
 * Service for starting the password recovery.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Service
public class PasswordRecoveryStartService {

    /**
     * The user repository.
     */
    private final UserRepository userRepository;

    /**
     * The token factory.
     */
    private final RecoveryTokenFactory tokenFactory;

    /**
     * The mailer.
     */
    private final PasswordRecoveryMailer mailer;

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordRecoveryStartService.class);

    /**
     * Builds the service
     * 
     * @param userRepository
     *            user repository
     * @param tokenFactory
     *            token factory
     * @param mailer
     *            password recovery mailer
     */
    @Autowired
    PasswordRecoveryStartService(final UserRepository userRepository, final RecoveryTokenFactory tokenFactory,
	    final PasswordRecoveryMailer mailer) {

	this.userRepository = userRepository;
	this.tokenFactory = tokenFactory;
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
	Optional<User> user = userRepository.findOneByEmailAddress(emailAddress);

	if (!user.isPresent()) {
	    LOGGER.info("Cancel password recovery for non existing user {}", emailAddress);
	    return;
	}

	RecoveryToken token = tokenFactory.generateToken(user.get());
	URI uri = URITemplates.buildPasswordRecoveryURI(token, baseUri);
	
	mailer.sendRecoveryMail(emailAddress, uri, locale);
    }

}
