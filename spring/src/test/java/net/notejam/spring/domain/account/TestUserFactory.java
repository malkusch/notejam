package net.notejam.spring.domain.account;

/**
 * A user factory for tests.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
public final class TestUserFactory {

    /**
     * Builds a new user.
     * 
     * @return new user
     */
    public static User buildUser() {
	return new User();
    }

}
