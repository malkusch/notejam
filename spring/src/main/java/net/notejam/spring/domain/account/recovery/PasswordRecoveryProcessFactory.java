package net.notejam.spring.domain.account.recovery;

import java.time.Instant;
import java.time.Period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.notejam.spring.domain.account.EmailAddress;
import net.notejam.spring.domain.account.User;
import net.notejam.spring.domain.account.UserRepository;
import net.notejam.spring.infrastructure.security.RandomStringGenerator;

/**
 * A password recovery process factory.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Component
public final class PasswordRecoveryProcessFactory {

    /**
     * The random string generator.
     */
    private final RandomStringGenerator generator;

    /**
     * The process life time.
     */
    private final Period processLifetime;

    /**
     * The process repository.
     */
    private final PasswordRecoveryProcessRepository processRepository;

    /**
     * The user repository.
     */
    private final UserRepository userRepository;

    /**
     * Builds this factory with its dependencies.
     * 
     * @param generator
     *            random string generator
     * @param userRepository
     *            user repository
     * @param tokenLifetime
     *            process life time
     * @param processRepository
     *            process repository
     */
    @Autowired
    PasswordRecoveryProcessFactory(final RandomStringGenerator generator, final UserRepository userRepository,
	    @Value("${recovery.lifetime}") final Period tokenLifetime,
	    final PasswordRecoveryProcessRepository processRepository) {

	this.generator = generator;
	this.userRepository = userRepository;
	this.processLifetime = tokenLifetime;
	this.processRepository = processRepository;
    }

    /**
     * Builds a new password recovery process for a user.
     * 
     * @param emailAddress
     *            email address of the user
     * @return password recovery process
     * @throws IllegalArgumentException
     *             if no user is registered with the given email address
     */
    public PasswordRecoveryProcess buildProcess(final EmailAddress emailAddress) {

	User user = userRepository.findOneByEmailAddress(emailAddress)
		.orElseThrow(() -> new IllegalArgumentException());

	Instant expiration = Instant.now().plus(processLifetime);
	PasswordRecoveryToken token = new PasswordRecoveryToken(generator.generateAlphaNumericString());

	PasswordRecoveryProcess process = new PasswordRecoveryProcess(token, user, expiration);
	process = processRepository.save(process);

	return process;
    }

}
