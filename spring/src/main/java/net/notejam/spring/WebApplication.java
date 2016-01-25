package net.notejam.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The Notejam web application.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@SpringBootApplication
public class WebApplication {

    /**
     * Starts the application.
     *
     * @param args
     *            The commandline arguments.
     */
    public static void main(final String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

}
