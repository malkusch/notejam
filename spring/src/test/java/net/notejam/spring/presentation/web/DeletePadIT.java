package net.notejam.spring.presentation.web;

import static net.notejam.spring.test.UriUtil.buildUri;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.notejam.spring.application.PadService;
import net.notejam.spring.domain.Name;
import net.notejam.spring.domain.Pad;
import net.notejam.spring.domain.PadRepository;
import net.notejam.spring.presentation.URITemplates;
import net.notejam.spring.test.IntegrationTest;
import net.notejam.spring.test.MockMvcProvider;
import net.notejam.spring.test.SignedUpUserProvider;

/**
 * An integration test for the {@link DeletePadController}.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@WithUserDetails(SignedUpUserProvider.EMAIL)
public class DeletePadIT {

    @Rule
    @Autowired
    public MockMvcProvider mockMvcProvider;
    
    @Rule
    @Autowired
    public SignedUpUserProvider userProvider;
    
    @Autowired
    private PadService padService;
    
    @Autowired
    private PadRepository repository;
    
    private Pad pad;
    
    /**
     * The edit note uri.
     */
    private String uri;
    
    private void setPad() {
        pad = padService.createPad(new Name("name"), userProvider.getUser());
    }
    
    @Before
    public void setUri() {
        setPad();
        
        uri = buildUri(URITemplates.DELETE_PAD, pad.getId());
    }
    
    /**
     * Pad can be deleted by its owner.
     */
    @Test
    public void padCanBeDeleted() throws Exception {
        mockMvcProvider.getMockMvc().perform(post(uri)
                .with(csrf()))

            .andExpect(status().is3xxRedirection());
        
        assertNull(repository.findOne(pad.getId()));
    }
    
    /**
     * Pad can't be deleted by not an owner.
     */
    @Test
    public void padCannotBeDeletedByOtherUser() throws Exception {
        mockMvcProvider.getMockMvc().perform(post(uri)
                .with(csrf())
                .with(user(userProvider.getAnotherAuthenticatedUser())))

            .andExpect(status().is(403));
        
        assertNotNull(repository.findOne(pad.getId()));
    }
    
}
