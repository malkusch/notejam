package net.notejam.spring.infrastructure.security;

import java.math.BigInteger;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service to generate a random string.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Service
public class RandomStringGenerator {

    /**
     * A random instance.
     */
    private final Random random;

    /**
     * The length of the generated password.
     */
    private final int passwordLength;

    /**
     * Builds this service.
     *
     * @param random
     *            source of random
     * @param passwordLength
     *            length for generated passwords
     */
    @Autowired
    RandomStringGenerator(final Random random, @Value("${recovery.length}") final int passwordLength) {
	this.random = random;
	this.passwordLength = passwordLength;
    }

    /**
     * Generates a random password.
     *
     * @return The generated password
     */
    public String generatePassword() {
	return RandomStringUtils.randomAlphanumeric(passwordLength);
    }

    /**
     * Generates a secure random string.
     *
     * @return A random string
     * @see <a href=
     *      "http://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string">
     *      How to generate a random alpha-numeric string?</a>
     */
    public String generateAlphaNumericString() {
	return new BigInteger(130, random).toString(32);
    }

}
