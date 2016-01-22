package net.notejam.spring.domain.account.recovery;

/**
 * The token was not valid.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
public class InvalidTokenException extends Exception {

    private static final long serialVersionUID = 6300207371386698201L;

    /**
     * Constructs a new exception with the specified detail message. The cause
     * is not initialized, and may subsequently be initialized by a call to
     * {@link #initCause}.
     *
     * @param message
     *            the detail message. The detail message is saved for later
     *            retrieval by the {@link #getMessage()} method.
     */
    public InvalidTokenException(String message) {
	super(message);
    }

}
