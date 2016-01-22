package net.notejam.spring.infrastructure.security.owner;

import static net.notejam.spring.domain.account.TestUserFactory.buildUser;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.access.AccessDeniedException;

import net.notejam.spring.domain.account.User;
import net.notejam.spring.domain.account.security.Owned;
import net.notejam.spring.infrastructure.security.AuthenticationService;

/**
 * A test for PermitOwnerAspect
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class PermitOwnerAspectTest {

    @Mock
    private AuthenticationService service;

    @Mock
    private Owned owned;
    
    /**
     * The SUT
     */
    @InjectMocks
    private PermitOwnerAspect aspect;

    /**
     * Tests authorizeReturn() permits Null
     */
    @Test
    public void testAuthorizeReturnShouldPermitNull() {
        aspect.authorizeReturn(null);
        aspect.authorizeReturn(Optional.ofNullable(null));
    }

    /**
     * Tests authorizeReturn() denies anonymous access.
     */
    @SuppressWarnings("unchecked")
    @Test(expected = AccessDeniedException.class)
    public void testAuthorizeReturnDeniesAnonymous() {
        when(owned.getOwner()).thenReturn(buildUser());
        when(service.getAuthenticatedUser()).thenThrow(AccessDeniedException.class);

        aspect.authorizeReturn(owned);
    }

    /**
     * Tests authorizeReturn() denies access to other users.
     */
    @Test(expected = AccessDeniedException.class)
    public void testAuthorizeReturnDeniesOtherUser() {
        when(owned.getOwner()).thenReturn(buildUser());
        when(service.getAuthenticatedUser()).thenReturn(buildUser());

        aspect.authorizeReturn(owned);
    }

    /**
     * Tests authorizeReturn() permits access to the owner.
     */
    @Test
    public void testAuthorizeReturnPermitsOwner() {
        User owner = buildUser();
        when(owned.getOwner()).thenReturn(owner);
        when(service.getAuthenticatedUser()).thenReturn(owner);

        aspect.authorizeReturn(owned);
    }

}
