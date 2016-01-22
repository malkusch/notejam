package net.notejam.spring.infrastructure.security;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.notejam.spring.domain.account.EmailAddress;
import net.notejam.spring.test.IntegrationTest;
import net.notejam.spring.test.SignedUpUserProvider;

/**
 * An integration test for the AuthenticationService.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService service;
    
    @Rule
    @Autowired
    public SignedUpUserProvider userProvider;

    /**
     * Tests getAuthenticatedUser() returns the authenticated user.
     */
    @Test
    @WithUserDetails(SignedUpUserProvider.EMAIL)
    public void testGetAuthenticatedUser() {
        assertEquals(new EmailAddress(SignedUpUserProvider.EMAIL), service.getAuthenticatedUser().getEmailAddress());
    }

}
