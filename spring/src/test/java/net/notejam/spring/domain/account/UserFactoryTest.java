package net.notejam.spring.domain.account;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.notejam.spring.domain.account.security.PlainTextPassword;
import net.notejam.spring.test.IntegrationTest;

/**
 * An integration test for the UserFactory.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class UserFactoryTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserFactory factory;

    /**
     * Tests signUp().
     */
    @Test
    public void testSignUp() {
	EmailAddress emailAddress = new EmailAddress("test@example.net");
	
	factory.signUp(emailAddress, new PlainTextPassword("password"));
	
	assertTrue(repository.findOneByEmailAddress(emailAddress).isPresent());
    }

}
