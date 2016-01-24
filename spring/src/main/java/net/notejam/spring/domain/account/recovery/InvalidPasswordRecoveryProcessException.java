package net.notejam.spring.domain.account.recovery;

/**
 * The process was not valid.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
public final class InvalidPasswordRecoveryProcessException extends Exception {

    private static final long serialVersionUID = 6300207371386698201L;

    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @param message
     *            the detail message. The detail message is saved for later
     *            retrieval by the {@link #getMessage()} method.
     */
    public InvalidPasswordRecoveryProcessException(final String message) {
	super(message);
    }

}
