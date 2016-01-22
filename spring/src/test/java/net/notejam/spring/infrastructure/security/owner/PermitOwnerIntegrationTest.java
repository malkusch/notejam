package net.notejam.spring.infrastructure.security.owner;

import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.notejam.spring.domain.account.security.Owned;
import net.notejam.spring.test.IntegrationTest;
import net.notejam.spring.test.SignedUpUserProvider;

/**
 * An integration test for the {@link PermitOwner}.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@WithUserDetails(SignedUpUserProvider.EMAIL)
public class PermitOwnerIntegrationTest {

    @Rule
    @Autowired
    public SignedUpUserProvider userProvider;

    @Mock
    private Owned owned;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    /**
     * Returning an owned object should be permitted.
     */
    @Test
    public void testPermitReturnOwned() {
        when(owned.getOwner()).thenReturn(userProvider.getUser());

        returnOwned(owned);
    }

    /**
     * Sending an owned object is permitted for the owner.
     */
    @Test
    public void testPermitSendOwned() {
        when(owned.getOwner()).thenReturn(userProvider.getUser());

        sendOwned(owned);
    }

    /**
     * Returning an owned Optional should be permitted.
     */
    @Test
    public void testPermitReturnOwnedOptional() {
        when(owned.getOwner()).thenReturn(userProvider.getUser());

        returnOwned(Optional.of(owned));
    }

    /**
     * Sending an owned Optional should be permitted.
     */
    @Test
    @Ignore
    public void testPermitSendOwnedOptional() {
        when(owned.getOwner()).thenReturn(userProvider.getUser());

        sendOwned(Optional.of(owned));
    }

    /**
     * Returning null should be permitted.
     */
    @Test
    public void testPermitReturnNull() {
        returnOwned(null);
    }

    /**
     * Sending null should be permitted.
     */
    @Test
    public void testPermitSendNull() {
        sendOwned(null);
    }

    /**
     * Returning an empty Optional should be permitted.
     */
    @Test
    public void testPermitReturnEmptyOptional() {
        returnOwned(Optional.ofNullable((Owned) null));
    }

    /**
     * Sending an empty Optional should be permitted.
     */
    @Test
    @Ignore
    public void testPermitSendEmptyOptional() {
        sendOwned(Optional.ofNullable((Owned) null));
    }

    /**
     * Returning an not owned object should be denied.
     */
    @Test(expected = AccessDeniedException.class)
    public void testPermitReturnNotOwned() {
        when(owned.getOwner()).thenReturn(userProvider.getAnotherUser());

        returnOwned(owned);
    }

    /**
     * Sending an not owned object should be denied.
     */
    @Test(expected = AccessDeniedException.class)
    public void testPermitSendNotOwned() {
        when(owned.getOwner()).thenReturn(userProvider.getAnotherUser());

        sendOwned(owned);
    }

    /**
     * Returning an not owned object should be denied.
     */
    @Test(expected = AccessDeniedException.class)
    public void testPermitReturnOptionalNotOwned() {
        when(owned.getOwner()).thenReturn(userProvider.getAnotherUser());
        
        returnOwned(Optional.of(owned));
    }

    /**
     * Sending an not owned object should be denied.
     */
    @Test(expected = AccessDeniedException.class)
    @Ignore
    public void testPermitSendOptionalNotOwned() {
        when(owned.getOwner()).thenReturn(userProvider.getAnotherUser());

        sendOwned(Optional.of(owned));
    }

    /**
     * Sends an owned object.
     * 
     * @param user
     *            The owned object.
     */
    private <T> void sendOwned(@PermitOwner T owned) {
    }

    /**
     * Returns an owned object.
     * 
     * @param user
     *            The owned object.
     * @return The owned object.
     */
    @PermitOwner
    private <T> T returnOwned(T owned) {
        return owned;
    }

}
