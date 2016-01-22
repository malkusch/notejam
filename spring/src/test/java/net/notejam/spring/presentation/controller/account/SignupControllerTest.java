package net.notejam.spring.presentation.controller.account;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;

import net.notejam.spring.domain.account.EmailAddress;
import net.notejam.spring.domain.account.UserFactory;
import net.notejam.spring.domain.account.UserRepository;
import net.notejam.spring.domain.account.security.PlainTextPassword;
import net.notejam.spring.presentation.URITemplates;
import net.notejam.spring.test.IntegrationTest;
import net.notejam.spring.test.MockMvcProvider;

/**
 * An integration test for the {@link SignupController}.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SignupControllerTest {

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserFactory factory;
    
    @Autowired
    private UserRepository userRepository;
    
    @Rule
    @Autowired
    public MockMvcProvider mockMvcProvider;

    /**
     * User can successfully sign up.
     */
    @Test
    public void userCanSuccessfullySignUp() throws Exception {
        final String email    = "test@example.net";
        final String password = "EHKBHHKe";
        
        mockMvcProvider.getMockMvc().perform(post(URITemplates.SIGNUP)
                .param("emailAddress", email)
                .param("password", password)
                .param("repeatedPassword", password)
                .with(csrf()))

            .andExpect(model().hasNoErrors())
            .andExpect(redirectedUrl("/signin?signup"))
            .andExpect((MvcResult result) -> {
                Authentication request = new UsernamePasswordAuthenticationToken(email, password);
                authenticationManager.authenticate(request);
            });
    }
    
    /**
     * User can't sign up if required fields are missing.
     */
    @Test
    public void userCannotSignUpIfFieldIsMissing() throws Exception {
        final String emailAddress    = "test@example.net";
        final String password = "EHKBHHKe";
        
        mockMvcProvider.getMockMvc().perform(post(URITemplates.SIGNUP)
                .param("emailAddress", emailAddress)
                .param("password", password)
                .with(csrf()))
        
            .andExpect(model().attributeHasFieldErrors("signupUser", "repeatedPassword"))
            .andExpect(view().name("user/signup"));

        assertTrue(userRepository.findAll().isEmpty());
    }
    
    /**
     * User can't sign up if email is invalid.
     */
    @Test
    public void userCannotSignUpIfEmailIsInvalid() throws Exception {
        final String emailAddress    = "invalid";
        final String password = "EHKBHHKe";
        
        mockMvcProvider.getMockMvc().perform(post(URITemplates.SIGNUP)
                .param("emailAddress", emailAddress)
                .param("password", password)
                .param("repeatedPassword", password)
                .with(csrf()))
        
            .andExpect(model().attributeHasFieldErrors("signupUser", "emailAddress"))
            .andExpect(view().name("user/signup"));
        
        assertTrue(userRepository.findAll().isEmpty());
    }
    
    /**
     * User can't sign up if email already exists.
     */
    @Test
    public void userCannotSignUpIfEmailExists() throws Exception {
        final String email    = "test@example.net";
        final String password = "EHKBHHKe";
        
        factory.signUp(new EmailAddress(email), new PlainTextPassword("QiXUzGS123"));
        
        mockMvcProvider.getMockMvc().perform(post(URITemplates.SIGNUP)
                .param("emailAddress", email)
                .param("password", password)
                .param("repeatedPassword", password)
                .with(csrf()))
        
            .andExpect(model().attributeHasFieldErrors("signupUser", "emailAddress"))
            .andExpect(view().name("user/signup"));
    }
    
    /**
     * User can't sign up if passwords do not match
     */
    @Test
    public void userCannotSignUpIfPasswordsDontMatch() throws Exception {
        final String emailAddress = "test@example.net";
        
        mockMvcProvider.getMockMvc().perform(post(URITemplates.SIGNUP)
                .param("emailAddress", emailAddress)
                .param("password", "EHKBHHKe123")
                .param("repeatedPassword", "QiXUzGS123")
                .with(csrf()))
        
            .andExpect(model().attributeHasFieldErrors("signupUser", "password"))
            .andExpect(view().name("user/signup"));
        
        assertFalse(userRepository.findOneByEmailAddress(new EmailAddress(emailAddress)).isPresent());
    }

}
