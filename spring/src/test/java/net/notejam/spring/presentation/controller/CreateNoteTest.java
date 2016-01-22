package net.notejam.spring.presentation.controller;

import static net.notejam.spring.test.UriUtil.getPathVariable;
import static net.notejam.spring.test.UriUtil.redirectToAuthentication;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;

import net.notejam.spring.domain.Name;
import net.notejam.spring.domain.Note;
import net.notejam.spring.domain.NoteRepository;
import net.notejam.spring.domain.Pad;
import net.notejam.spring.domain.PadRepository;
import net.notejam.spring.domain.account.EmailAddress;
import net.notejam.spring.presentation.URITemplates;
import net.notejam.spring.test.IntegrationTest;
import net.notejam.spring.test.MockMvcProvider;
import net.notejam.spring.test.SignedUpUserProvider;

/**
 * An integration test for creating a note.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CreateNoteTest {

    @Autowired
    private PadRepository padRepository;
    
    @Autowired
    private NoteRepository repository;
    
    @Rule
    @Autowired
    public MockMvcProvider mockMvcProvider;
    
    @Rule
    @Autowired
    public SignedUpUserProvider userProvider;

    /**
     * Note can be successfully created.
     */
    @Test
    @WithUserDetails(SignedUpUserProvider.EMAIL)
    public void noteCanBeCreated() throws Exception {
        final String name = "name";
        final String text = "text";
        
        mockMvcProvider.getMockMvc().perform(post(URITemplates.CREATE_NOTE)
                .param("name", name)
                .param("text", text)
                .param("padId", "")
                .with(csrf()))
        
            .andExpect(model().hasNoErrors())
            .andExpect((MvcResult result) -> {
                int id = Integer.parseInt(getPathVariable("id", URITemplates.VIEW_NOTE, result.getResponse().getRedirectedUrl())); 
                Note note = repository.findOne(id);

                assertEquals(new Name(name), note.getName());
                assertEquals(text, note.getText());
                assertEquals(new EmailAddress(SignedUpUserProvider.EMAIL), note.getOwner().getEmailAddress());
                assertFalse(note.getPad().isPresent());
            });
    }
    
    /**
     * Note can't be created by anonymous user.
     */
    @Test
    public void noteCannotBeCreatedByAnonymous() throws Exception {
        mockMvcProvider.getMockMvc().perform(post(URITemplates.CREATE_NOTE)
                .param("name", "name")
                .param("text", "text")
                .param("padId", "")
                .with(csrf()))
        
            .andExpect(redirectToAuthentication());
        
        assertThat(repository.findAll(), empty());
    }
    
    /**
     * Note can't be created if required fields are missing.
     */
    @Test
    @WithUserDetails(SignedUpUserProvider.EMAIL)
    public void noteCannotBeCreatedIfFieldsAreMissing() throws Exception {
        mockMvcProvider.getMockMvc().perform(post(URITemplates.CREATE_NOTE)
                .param("text", "text")
                .with(csrf()))
        
            .andExpect(model().attributeHasFieldErrors("noteCommand", "name"))
            .andExpect(view().name("note/create"));
        
        assertThat(repository.findAll(), empty());
    }
    
    /**
     * Note can't be added into another's user pad.
     */
    @Test
    @WithUserDetails(SignedUpUserProvider.EMAIL)
    public void noteCannotBeAddedIntoAnotherUserPad() throws Exception {
        Pad pad = new Pad(new Name("name"), userProvider.getAnotherUser());
        padRepository.save(pad);
        
        mockMvcProvider.getMockMvc().perform(post(URITemplates.CREATE_NOTE)
                .param("name", "name")
                .param("text", "text")
                .param("padId", pad.getId().toString())
                .with(csrf()))
        
            .andExpect(status().is(403));
        
        assertThat(repository.findAll(), empty());
    }

}
