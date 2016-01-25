package net.notejam.spring.application.security;

import static net.notejam.spring.domain.account.TestUserFactory.buildUser;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
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
 * A test for AuthorizationService
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthorizationServiceTest {

    /**
     * The SUT
     */
    @InjectMocks
    private AuthorizationService service;

    private User authenticatedUser = buildUser();
    
    private User otherUser = buildUser();
    
    @Mock
    private AuthenticationService authenticationService;
    
    @Mock
    private Owned ownedEntity;
    
    @Mock
    private Owned notOwnedEntity;
    
    @Before
    public void setUpAuthenticatedContext() {
	when(authenticationService.authenticatedUser()).thenReturn(authenticatedUser);
	when(ownedEntity.getOwner()).thenReturn(authenticatedUser);
	when(notOwnedEntity.getOwner()).thenReturn(otherUser);
    }
    
    /**
     * Null as user should be authorized.
     */
    @Test
    public void authorizeNullUser() {
        service.authorize((User) null);
    }
    
    /**
     * Null as owned entity should be authorized.
     */
    @Test
    public void authorizeNullOwned() {
	service.authorize((Owned) null);
    }
    
    /**
     * Empty owned entity should be authorized.
     */
    @Test
    public void authorizeEmptyOptional() {
	service.authorize(Optional.empty());
    }
    
    /**
     * Anonymous access should be denied for a user.
     */
    @SuppressWarnings("unchecked")
    @Test(expected = AccessDeniedException.class)
    public void denyAnonymousAccessToUser() {
	when(authenticationService.authenticatedUser()).thenThrow(AccessDeniedException.class);
	service.authorize(authenticatedUser);
    }
    
    /**
     * Anonymous access should be denied for an owned entity.
     */
    @SuppressWarnings("unchecked")
    @Test(expected = AccessDeniedException.class)
    public void denyAnonymousAccessToOwned() {
	when(authenticationService.authenticatedUser()).thenThrow(AccessDeniedException.class);
	service.authorize(ownedEntity);
    }
    
    /**
     * Anonymous access should be denied for an optional owned entity.
     */
    @SuppressWarnings("unchecked")
    @Test(expected = AccessDeniedException.class)
    public void denyAnonymousAccessToOptionalOwned() {
	when(authenticationService.authenticatedUser()).thenThrow(AccessDeniedException.class);
	service.authorize(Optional.of(ownedEntity));
    }

    /**
     * Deny access to user for another user.
     */
    @Test(expected = AccessDeniedException.class)
    public void denyAccessToUserForAnotherUser() {
	service.authorize(otherUser);
    }
    
    /**
     * Deny access to an owned entity for another user.
     */
    @Test(expected = AccessDeniedException.class)
    public void denyAccessToOwnedForAnotherUser() {
	service.authorize(notOwnedEntity);
    }
    
    /**
     * Deny access to an optionally owned entity for another user.
     */
    @Test(expected = AccessDeniedException.class)
    public void denyAccessToOptionalOwnedForAnotherUser() {
	service.authorize(Optional.of(notOwnedEntity));
    }

    /**
     * Permit access to the user.
     */
    @Test
    public void permitAccessToUser() {
	service.authorize(authenticatedUser);
    }
    
    /**
     * Permit access to the owner.
     */
    @Test
    public void permitAccessToOwner() {
	service.authorize(ownedEntity);
    }
    
    /**
     * Permit access to the owner of an optional entity.
     */
    @Test
    public void permitAccessToOwnerOfOptional() {
	service.authorize(Optional.of(ownedEntity));
    }

}
