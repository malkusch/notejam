package net.notejam.spring.infrastructure.security.owner;

import java.util.Optional;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import net.notejam.spring.application.security.AuthorizationService;
import net.notejam.spring.domain.account.security.Owned;
import net.notejam.spring.infrastructure.reflection.Annotated;
import net.notejam.spring.infrastructure.reflection.ReflectionUtils;

/**
 * Grant access only to the authenticated owner of an object.
 *
 * @author markus@malkusch.de
 *
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 * @see Owned
 * @see PermitOwner
 */
@Aspect
@Configurable
public class PermitOwnerAspect {

    /**
     * The authorization service.
     */
    @Autowired
    private AuthorizationService authorizationService;

    /**
     * Sets the authorization service.
     *
     * @param authorizationService
     *            authorization service
     */
    void setAuthenticationService(final AuthorizationService authorizationService) {
	this.authorizationService = authorizationService;
    }

    /**
     * The point cut for method arguments.
     */
    @Pointcut("execution(* *(.., @PermitOwner (*), ..))")
    private static void restrictOwnedEntities() {
	// This is a pointcut.
    }

    /**
     * Checks method calls with owned arguments.
     *
     * @param joinPoint
     *            The joint point.
     */
    @Before("net.notejam.spring.infrastructure.security.owner.PermitOwnerAspect.restrictOwnedEntities()")
    public void authorizeCall(final JoinPoint joinPoint) {
	for (Annotated<PermitOwner, Owned> annotated : ReflectionUtils
		.<PermitOwner, Owned>getAnnotatedArguments(PermitOwner.class, joinPoint)) {
	    if (annotated.getObject() == null) {
		continue;

	    }
	    authorizationService.authorize(annotated.getObject());

	}
    }

    /**
     * The point cut for return values.
     */
    @Pointcut("execution(@PermitOwner * *(..))")
    private static void restrictOwnedResults() {
	// This is a pointcut.
    }

    /**
     * Checks return owned return values.
     *
     * @param entity
     *            The owned entity.
     */
    @SuppressWarnings("unchecked")
    @AfterReturning(pointcut = "net.notejam.spring.infrastructure.security.owner.PermitOwnerAspect.restrictOwnedResults()", returning = "entity")
    public void authorizeReturn(final Object entity) {
	if (entity instanceof Owned) {
	    authorizationService.authorize((Owned) entity);

	} else if (entity instanceof Optional) {
	    authorizationService.authorize((Optional<Owned>) entity);
	}
    }

}
