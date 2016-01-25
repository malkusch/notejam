package net.notejam.spring.domain.account;

import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * A test for EmailAddress.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 * @see EmailAddress
 */
@RunWith(Theories.class)
public class EmailAddressTest {

    /**
     * Creating with null should fail.
     */
    @Test(expected = NullPointerException.class)
    public void nullShouldFail() {
	new EmailAddress(null);
    }

    @DataPoints("invalidAddresses")
    public static String[] invalidAddresses = { "", "foo", "<foo@test.de" };

    /**
     * Creating an invalid address should fail.
     */
    @Test(expected = IllegalArgumentException.class)
    @Theory
    public void invalidShouldFail(@FromDataPoints("invalidAddresses") String emailAddress) {
	new EmailAddress(emailAddress);
    }
    
    @DataPoints("validAddresses")
    public static String[] validAddresses = { "foo@example.org", "123@example.net" };
    
    /**
     * Creating an empty address should not fail.
     */
    @Test
    @Theory
    public void createValidAddress(@FromDataPoints("validAddresses") String emailAddress) {
	new EmailAddress(emailAddress);
    }

}
