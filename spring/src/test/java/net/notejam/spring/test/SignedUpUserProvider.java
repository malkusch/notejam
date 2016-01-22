package net.notejam.spring.test;

import org.junit.rules.ExternalResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.notejam.spring.domain.account.EmailAddress;
import net.notejam.spring.domain.account.User;
import net.notejam.spring.domain.account.UserFactory;
import net.notejam.spring.domain.account.UserRepository;
import net.notejam.spring.domain.account.security.PlainTextPassword;
import net.notejam.spring.infrastructure.security.AuthenticatedUser;

/**
 * A rule for providing a signed up a user.
 *
 * The user will be deleted after each test.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Component
public class SignedUpUserProvider extends ExternalResource {

    /**
     * The user factory.
     */
    @Autowired
    private UserFactory factory;

    /**
     * The user repository.
     */
    @Autowired
    private UserRepository repository;

    /**
     * The signed up user.
     */
    private User user;
    
    /**
     * Another signed up user.
     */
    private User other;

    /**
     * The email address of the provided user.
     */
    public static final String EMAIL = "test@example.org";
    
    /**
     * The password of the provided user.
     */
    public static final String PASSWORD = "password";

    @Override
    protected void before() {
        user = factory.signUp(new EmailAddress(EMAIL), new PlainTextPassword(PASSWORD));
        other = factory.signUp(new EmailAddress("another@example.org"), new PlainTextPassword(PASSWORD));
    }

    /**
     * Returns another signed up authenticated user.
     * 
     * @return The signed up authenticated user.
     */
    public AuthenticatedUser getAnotherAuthenticatedUser() {
	return new AuthenticatedUser(other);
    }
    
    /**
     * Returns another signed up user.
     * 
     * @return The signed up user.
     */
    public User getAnotherUser() {
	return other;
    }
    
    /**
     * Returns the signed up user.
     * 
     * @return The signed up user.
     */
    public User getUser() {
        return user;
    }

    @Override
    protected void after() {
        repository.delete(user);
        repository.delete(other);
    }
}
