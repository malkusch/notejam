package net.notejam.spring.presentation.controller;

import static net.notejam.spring.test.UriUtil.getPathVariable;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;

import net.notejam.spring.domain.Name;
import net.notejam.spring.domain.Pad;
import net.notejam.spring.domain.PadRepository;
import net.notejam.spring.domain.account.EmailAddress;
import net.notejam.spring.presentation.URITemplates;
import net.notejam.spring.test.IntegrationTest;
import net.notejam.spring.test.MockMvcProvider;
import net.notejam.spring.test.SignedUpUserProvider;

/**
 * An integration test for the {@link PadController}.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@WithUserDetails(SignedUpUserProvider.EMAIL)
public class CreatePadTest {

    @Rule
    @Autowired
    public MockMvcProvider mockMvcProvider;
    
    @Rule
    @Autowired
    public SignedUpUserProvider userProvider;
    
    @Autowired
    private PadRepository repository;
    
    /**
     * Pad can be successfully created.
     */
    @Test
    public void padCanBeCreated() throws Exception {
        final Name name = new Name("name");
        
        mockMvcProvider.getMockMvc().perform(post(URITemplates.CREATE_PAD)
                .param("name", name.toString())
                .with(csrf()))
        
            .andExpect(model().hasNoErrors())
            .andExpect((MvcResult result) -> {
                int id  = Integer.parseInt(getPathVariable("id", URITemplates.VIEW_PAD, result.getResponse().getRedirectedUrl())); 
                Pad pad = repository.findOne(id);

                assertEquals(name, pad.getName());
                assertEquals(new EmailAddress(SignedUpUserProvider.EMAIL), pad.getOwner().getEmailAddress());
            });
    }
    
    /**
     * Pad can't be created if required fields are missing.
     */
    @Test
    public void padCannotBeCreatedIfFieldsAreMissing() throws Exception {
        mockMvcProvider.getMockMvc().perform(post(URITemplates.CREATE_PAD)
                .with(csrf()))
        
            .andExpect(model().attributeHasFieldErrors("createPad", "name"))
            .andExpect(view().name("pad/create"));
        
        assertThat(repository.findAll(), empty());
    }
    
}
