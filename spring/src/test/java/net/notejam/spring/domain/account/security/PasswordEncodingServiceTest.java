package net.notejam.spring.domain.account.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * A test for PasswordEncodingService
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class PasswordEncodingServiceTest {

    @Mock
    private PasswordEncoder encoder;

    /**
     * The SUT
     */
    @InjectMocks
    private PasswordEncodingService service;

    /**
     * Tests encode()
     */
    @Test
    public void testEncode() {
        when(encoder.encode("plainPwd")).thenReturn("encodedPwd");
        assertEquals(new EncodedPassword("encodedPwd"), service.encode(new PlainTextPassword("plainPwd")));
    }

    /**
     * Tests isPasswordValid()
     */
    @Test
    public void testIsPasswordValid() {
        when(encoder.matches("plainPwd", "encodedPwd")).thenReturn(true);

        assertTrue(service.matches(new PlainTextPassword("plainPwd"), new EncodedPassword("encodedPwd")));
        assertFalse(service.matches(new PlainTextPassword("wrong12345"), new EncodedPassword("encodedPwd")));
    }
}
