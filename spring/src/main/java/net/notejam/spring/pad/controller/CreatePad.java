package net.notejam.spring.pad.controller;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * A command for creating a pad.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
interface CreatePad {

    /**
     * Returns the name of the pad.
     *
     * @return The name of the pad.
     */
    @Size(max = 100)
    @NotEmpty
    String getName();

}
