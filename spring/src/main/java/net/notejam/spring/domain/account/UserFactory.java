package net.notejam.spring.domain.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.notejam.spring.domain.account.security.EncodedPassword;
import net.notejam.spring.domain.account.security.PasswordEncodingService;
import net.notejam.spring.domain.account.security.PlainTextPassword;

/**
 * A user factory.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Service
public final class UserFactory {

    /**
     * The password encoding service.
     */
    private final PasswordEncodingService encodingService;

    /**
     * The user repository.
     */
    private final UserRepository repository;

    /**
     * Builds the user factory and its dependencies.
     * 
     * @param encodingService
     *            password encoding service
     * @param repository
     *            user repository
     */
    @Autowired
    UserFactory(final PasswordEncodingService encodingService, final UserRepository repository) {
	this.encodingService = encodingService;
	this.repository = repository;
    }

    /**
     * Signs up a new user.
     *
     * @param emailAddress
     *            Email address
     * @param password
     *            plain text password
     * @return new signed up user
     */
    public User signUp(final EmailAddress emailAddress, final PlainTextPassword password) {
	EncodedPassword encodedPassword = encodingService.encode(password);

	User user = new User(emailAddress, encodedPassword);
	user = repository.save(user);

	return user;
    }

}
