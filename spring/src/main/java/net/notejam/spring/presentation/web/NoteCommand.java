package net.notejam.spring.presentation.web;

import java.util.Optional;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * A note command.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
final class NoteCommand {

    /**
     * Note name.
     */
    private String name;

    /**
     * Note text.
     */
    private String text;

    /**
     * Pad id.
     */
    private Optional<Integer> padId;

    /**
     * Returns the name of the note.
     *
     * @return name of the note
     */
    @Size(max = 100)
    @NotEmpty
    public String getName() {
        return name;
    }

    /**
     * Sets the name
     * 
     * @param name
     *            note name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Returns the text of a note.
     * 
     * @return note text
     */
    @Size(max = 10000)
    @NotEmpty
    public String getText() {
        return text;
    }

    /**
     * Sets the text.
     * 
     * @param text
     *            note text
     */
    public void setText(final String text) {
        this.text = text;
    }

    /**
     * Returns the optional pad id.
     *
     * @return pad id
     */
    public Optional<Integer> getPadId() {
        return padId;
    }

    /**
     * Sets the pad id.
     *
     * @param padId
     *            pad id
     */
    public void setPadId(final Optional<Integer> padId) {
        this.padId = padId;
    }

}
