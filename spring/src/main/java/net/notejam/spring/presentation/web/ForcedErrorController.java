package net.notejam.spring.presentation.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Forces an error.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Controller
@RequestMapping("/error")
final class ForcedErrorController {

    /**
     * Throws an exception on purpose to demonstrate the error handler.
     */
    @RequestMapping("/unhandled")
    void forceUnexpectedException() {
        throw new RuntimeException("This exception was forced.");
    }

}
