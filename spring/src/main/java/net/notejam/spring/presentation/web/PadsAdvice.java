package net.notejam.spring.presentation.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import net.notejam.spring.domain.Pad;
import net.notejam.spring.domain.PadRepository;
import net.notejam.spring.infrastructure.security.AuthenticatedUser;
import net.notejam.spring.presentation.web.PadsAdvice.Pads;

/**
 * Pads controller advice.
 *
 * This controller advice provides all pads of the authenticated user for the
 * view as the model attribute pads.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@ControllerAdvice(annotations = Pads.class)
@PreAuthorize("isAuthenticated()")
class PadsAdvice {

    /**
     * Provide all pads of the authenticated user as the model attribute pads.
     *
     * @author markus@malkusch.de
     * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE })
    static @interface Pads {

    }

    /**
     * The pad repository.
     */
    private final PadRepository repository;

    /**
     * Builds the advice with its dependencies.
     *
     * @param repository
     *            pad repository
     */
    @Autowired
    PadsAdvice(final PadRepository repository) {
        this.repository = repository;
    }

    /**
     * Provides the model attribute "pads". I.e. all pads of the currently
     * authenticated user.
     *
     * @param authenticatedUser
     *            authenticated user
     *
     * @return The model attribute "pads".
     */
    @ModelAttribute("pads")
    public List<Pad> pads(@AuthenticationPrincipal final AuthenticatedUser authenticatedUser) {
        return repository.findByOwner(authenticatedUser.getUser());
    }

}
