package net.notejam.spring.application.account;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import net.notejam.spring.domain.account.EmailAddress;
import net.notejam.spring.domain.account.EmailAddressExistsException;
import net.notejam.spring.domain.account.User;
import net.notejam.spring.domain.account.UserFactory;
import net.notejam.spring.domain.account.UserRepository;
import net.notejam.spring.domain.account.security.PasswordEncodingService;
import net.notejam.spring.domain.account.security.PlainTextPassword;
import net.notejam.spring.infrastructure.security.AuthenticationService;

/**
 * The user service.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Service
@Transactional
public class UserService {

    /**
     * The password encoding service.
     */
    private final PasswordEncodingService encodingService;

    /**
     * The user repository.
     */
    private final UserRepository repository;

    /**
     * The user factory.
     */
    private final UserFactory factory;

    /**
     * The authentication service.
     */
    private final AuthenticationService authenticationService;

    /**
     * Builds the service with its dependencies.
     * 
     * @param encodingService
     *            password encoding service
     * @param factory
     *            user factory
     * @param authenticationService
     *            authentication service
     * @param repository
     *            user repository
     */
    @Autowired
    UserService(final PasswordEncodingService encodingService, final UserFactory factory,
	    final AuthenticationService authenticationService, final UserRepository repository) {

	this.encodingService = encodingService;
	this.factory = factory;
	this.authenticationService = authenticationService;
	this.repository = repository;
    }

    /**
     * Sets a new password.
     *
     * @param password
     *            The new password
     */
    @PreAuthorize("isAuthenticated()")
    public void changePassword(final PlainTextPassword password) {
	User user = authenticationService.authenticatedUser();
	user.changePassword(encodingService.encode(password));
	repository.save(user);
    }

    /**
     * Signs up a new user.
     *
     * @param emailAddress
     *            Email address
     * @param password
     *            plain text password
     * @return signed up user
     * @throws EmailAddressExistsException
     *             if an user with the given email address was already
     *             registered
     */
    public User signUp(final EmailAddress emailAddress, final PlainTextPassword password)
	    throws EmailAddressExistsException {

	User user = factory.signUp(emailAddress, password);
	return user;
    }

}
