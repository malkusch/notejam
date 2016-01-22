package net.notejam.spring.presentation.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import net.notejam.spring.domain.account.security.EncodedPassword;
import net.notejam.spring.domain.account.security.PasswordEncodingService;
import net.notejam.spring.domain.account.security.PlainTextPassword;
import net.notejam.spring.infrastructure.security.AuthenticationService;

/**
 * A password validator. The validator matches the encoded password against the
 * currently authenticated user.
 * 
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Configurable
public class CurrentPasswordValidator implements ConstraintValidator<CurrentPassword, String> {

    /**
     * The user service.
     */
    @Autowired
    private AuthenticationService authenticationService;

    /**
     * The security service.
     */
    @Autowired
    private PasswordEncodingService securityService;

    @Override
    public void initialize(final CurrentPassword constraintAnnotation) {
	// Nothing to initialize.
    }

    @Override
    public boolean isValid(final String password, final ConstraintValidatorContext context) {
	try {
	    if (password == null) {
		return true;
	    }

	    PlainTextPassword plain = new PlainTextPassword(password);
	    EncodedPassword encoded = authenticationService.getAuthenticatedUser().getPassword();

	    return securityService.matches(plain, encoded);

	} catch (IllegalArgumentException e) {
	    // Invalid password are handled by another constrain.
	    return true;
	}
    }

}
