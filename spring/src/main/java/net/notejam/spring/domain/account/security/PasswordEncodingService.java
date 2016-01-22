package net.notejam.spring.domain.account.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Password encoding service.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Service
public final class PasswordEncodingService {

    /**
     * The password encoder.
     */
    private final PasswordEncoder encoder;

    /**
     * Builds the encoder with its dependencies.
     *
     * @param encoder
     *            password encoder
     */
    @Autowired
    PasswordEncodingService(final PasswordEncoder encoder) {
	this.encoder = encoder;
    }

    /**
     * Returns true if a plain text password matches an encoded password.
     *
     * @param password
     *            plain text password
     * @param encodedPassword
     *            encoded password
     * @return True if the passwords match
     */
    public boolean matches(final PlainTextPassword password, final EncodedPassword encodedPassword) {
	return encoder.matches(password.text, encodedPassword.toString());
    }

    /**
     * Encodes a plain text password.
     *
     * @param password
     *            plain text password
     * @return encoded password
     */
    public EncodedPassword encode(final PlainTextPassword password) {
	return new EncodedPassword(encoder.encode(password.text));
    }

}
