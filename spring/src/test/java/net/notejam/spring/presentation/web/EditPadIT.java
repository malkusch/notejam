package net.notejam.spring.presentation.web;

import static net.notejam.spring.test.UriUtil.buildUri;
import static net.notejam.spring.test.UriUtil.getPathVariable;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;

import net.notejam.spring.application.PadService;
import net.notejam.spring.domain.Name;
import net.notejam.spring.domain.Pad;
import net.notejam.spring.domain.PadRepository;
import net.notejam.spring.presentation.URITemplates;
import net.notejam.spring.test.IntegrationTest;
import net.notejam.spring.test.MockMvcProvider;
import net.notejam.spring.test.SignedUpUserProvider;

/**
 * An integration test for editing a pad.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@WithUserDetails(SignedUpUserProvider.EMAIL)
public class EditPadIT {

    @Rule
    @Autowired
    public MockMvcProvider mockMvcProvider;
    
    @Rule
    @Autowired
    public SignedUpUserProvider userProvider;
    
    @Autowired
    private PadRepository repository;
    
    @Autowired
    private PadService padService;
    
    private Pad pad;
    
    /**
     * The provided pad name.
     */
    private final Name NAME = new Name("name");
    
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
        
        uri = buildUri(URITemplates.EDIT_PAD, pad.getId());
    }
    
    /**
     * Pad can be edited by its owner.
     */
    @Test
    public void padCanBeEdited() throws Exception {
        final Name name = new Name("name2");
        
        mockMvcProvider.getMockMvc().perform(post(uri)
                .param("name", name.toString())
                .with(csrf()))
        
            .andExpect(model().hasNoErrors())
            .andExpect((MvcResult result) -> {
                int id = Integer.parseInt(getPathVariable("id", URITemplates.EDIT_PAD, result.getResponse().getRedirectedUrl())); 
                Pad pad = repository.findOne(id);

                assertEquals(name, pad.getName());
            });
    }
    
    /**
     * Pad can't be edited if required fields are missing.
     */
    @Test
    public void padCannotBeEditedIfFieldIsMissing() throws Exception {
        mockMvcProvider.getMockMvc().perform(post(uri)
                .param("name", "")
                .with(csrf()))
        
            .andExpect(model().attributeHasFieldErrors("editPad", "name"))
            .andExpect(view().name("pad/edit"));
    }
    
    /**
     * Pad can't be edited by not an owner.
     */
    @Test
    public void padCannotBeEditedByOtherUser() throws Exception {
        mockMvcProvider.getMockMvc().perform(post(uri)
                .param("name", "name2")
                .with(user(userProvider.getAnotherAuthenticatedUser()))
                .with(csrf()))
        
            .andExpect(status().is(403));
        
        assertEquals(NAME, repository.getOne(pad.getId()).getName());
    }
    
}
