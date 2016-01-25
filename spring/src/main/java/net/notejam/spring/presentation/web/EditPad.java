package net.notejam.spring.presentation.web;

/**
 * A command for editing a pad.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
final class EditPad implements CreatePad {

    /**
     * The pad name.
     */
    private String name;

    /**
     * Sets the name.
     *
     * @param name
     *            Pad name
     */
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

}
