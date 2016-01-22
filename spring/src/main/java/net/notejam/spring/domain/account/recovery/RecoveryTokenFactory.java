package net.notejam.spring.domain.account.recovery;

import java.time.Instant;
import java.time.Period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.notejam.spring.domain.account.User;
import net.notejam.spring.infrastructure.security.RandomStringGenerator;

/**
 * A recovery token factory.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Component
public final class RecoveryTokenFactory {

    /**
     * The random string generator.
     */
    private final RandomStringGenerator generator;

    /**
     * The token life time.
     */
    private final Period tokenLifetime;

    /**
     * The token repository.
     */
    private final RecoveryTokenRepository repository;

    /**
     * Builds this factory with its dependencies.
     * 
     * @param generator
     *            random string generator
     * @param tokenLifetime
     *            token life time
     * @param repository
     *            token repository
     */
    @Autowired
    RecoveryTokenFactory(final RandomStringGenerator generator,
	    @Value("${recovery.lifetime}") final Period tokenLifetime, final RecoveryTokenRepository repository) {

	this.generator = generator;
	this.tokenLifetime = tokenLifetime;
	this.repository = repository;
    }

    /**
     * Generates a new password recovery token for a user.
     * 
     * @param user
     *            user
     * @return generated password recovery token
     */
    public RecoveryToken generateToken(final User user) {
	Instant expiration = Instant.now().plus(tokenLifetime);
	String token = generator.generateAlphaNumericString();

	RecoveryToken recoveryToken = new RecoveryToken(token, user, expiration);
	recoveryToken = repository.save(recoveryToken);

	return recoveryToken;
    }

}
