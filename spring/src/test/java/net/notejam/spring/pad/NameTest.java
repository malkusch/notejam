package net.notejam.spring.pad;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.junit.runner.RunWith;

/**
 * A test for Name.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 * @see Name
 */
@RunWith(Theories.class)
public class NameTest {

    /**
     * Creating with null should fail.
     */
    @Test(expected = NullPointerException.class)
    public void nullNameShouldFail()
    {
        new Name(null);
    }
    
    /**
     * Creating with an empty name should fail.
     */
    @Test(expected = IllegalArgumentException.class)
    public void emptyNameShouldFail()
    {
        new Name("");
    }
    
    /**
     * Creating with a too long name should fail.
     */
    @Test(expected = IllegalArgumentException.class)
    @Theory
    public void tooLongNameShouldFail(@TestedOn(ints = {101, 102}) int count)
    {
        new Name(StringUtils.repeat("x", count));
    }
    
    /**
     * Creating should succeed.
     */
    @Theory
    public void validNameShouldSucceed(@TestedOn(ints = {1, 100}) int count)
    {
        String input = StringUtils.repeat("x", count);
        
        Name name = new Name(input);
        assertEquals(input, name.toString());
    }
    
}
