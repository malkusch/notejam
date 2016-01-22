package net.notejam.spring.presentation.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import net.notejam.spring.domain.account.EmailAddress;
import net.notejam.spring.domain.account.UserRepository;

/**
 * A unique email validator. This validator checks if the given email address
 * was already registered.
 * 
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Configurable
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    /**
     * The user repository.
     */
    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(final UniqueEmail constraintAnnotation) {
        // Nothing to initialize.
    }

    @Override
    public boolean isValid(final String emailAddress, final ConstraintValidatorContext context) {
        try {
            if (emailAddress == null) {
                return true;
            }
            
            return !repository.findOneByEmailAddress(new EmailAddress(emailAddress)).isPresent();
            
        } catch(IllegalArgumentException e) {
            // This should be handled by another constraint.
            return true;
        }
    }

}
