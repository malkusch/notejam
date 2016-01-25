package net.notejam.spring.infrastructure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import net.notejam.spring.domain.account.EmailAddress;
import net.notejam.spring.domain.account.User;
import net.notejam.spring.domain.account.UserRepository;

/**
 * UserDetailsService Implementation
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Service
final class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * The user repository.
     */
    private final UserRepository repository;

    /**
     * Builds the service with its dependencies.
     *
     * @param repository
     *            user repository
     */
    @Autowired
    UserDetailsServiceImpl(final UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) {
        User user = repository.findOneByEmailAddress(new EmailAddress(username))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("%s not found", username)));
        return new AuthenticatedUser(user);
    }

}
