package net.notejam.spring.presentation.controller;

import static net.notejam.spring.test.UriUtil.buildUri;
import static net.notejam.spring.test.UriUtil.getPathVariable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;

import net.notejam.spring.application.NoteService;
import net.notejam.spring.domain.Name;
import net.notejam.spring.domain.Note;
import net.notejam.spring.domain.NoteRepository;
import net.notejam.spring.domain.Pad;
import net.notejam.spring.domain.PadRepository;
import net.notejam.spring.presentation.URITemplates;
import net.notejam.spring.test.IntegrationTest;
import net.notejam.spring.test.MockMvcProvider;
import net.notejam.spring.test.SignedUpUserProvider;

/**
 * An integration test for editing a note.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@WithUserDetails(SignedUpUserProvider.EMAIL)
public class EditNoteTest {

    @Autowired
    private NoteRepository repository;
    
    @Autowired
    private PadRepository padRepository;
    
    @Rule
    @Autowired
    public MockMvcProvider mockMvcProvider;
    
    @Rule
    @Autowired
    public SignedUpUserProvider userProvider;
    
    @Autowired
    private NoteService service;
    
    private Note note;
    
    /**
     * The provided note name.
     */
    private final Name NAME = new Name("name");
    
    /**
     * The edit note uri.
     */
    private String uri;
    
    private void setNote() {
	note = service.writeNote(NAME, userProvider.getUser(), Optional.empty(), "text");
    }
    
    @Before
    public void setUri() {
        setNote();
        
        uri = buildUri(URITemplates.EDIT_NOTE, note.getId());
    }

    /**
     * Note can be edited by its owner.
     */
    @Test
    public void noteCanBeEdited() throws Exception {
        final String name = "name2";
        final String text = "text2";
        
        mockMvcProvider.getMockMvc().perform(post(uri)
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
            });
    }
    
    /**
     * Note can't be edited if required fields are missing.
     */
    @Test
    public void noteCannotBeEditedIfFieldIsMissing() throws Exception {
        mockMvcProvider.getMockMvc().perform(post(uri)
                .param("name", "name2")
                .param("text", "")
                .param("padId", "")
                .with(csrf()))
        
            .andExpect(model().attributeHasFieldErrors("noteCommand", "text"))
            .andExpect(view().name("note/edit"));
    }

    /**
     * Note can't be edited by not an owner.
     */
    @Test
    public void noteCannotBeEditedByOtherUser() throws Exception {
        mockMvcProvider.getMockMvc().perform(post(uri)
                .param("name", "name2")
                .param("text", "text2")
                .param("padId", "")
                .with(csrf())
                .with(user(userProvider.getAnotherAuthenticatedUser())))
        
            .andExpect(status().is(403));
        
        assertEquals(NAME, repository.getOne(note.getId()).getName());
    }
    
    /**
     * Note can't be added into another's user pad.
     */
    @Test
    public void noteCannotBeAddedIntoAnotherUserPad() throws Exception {
        Pad pad = new Pad(new Name("name"), userProvider.getAnotherUser());
        padRepository.save(pad);
        
        mockMvcProvider.getMockMvc().perform(post(uri)
                .param("name", "name")
                .param("text", "text")
                .param("padId", pad.getId().toString())
                .with(csrf()))
        
            .andExpect(status().is(403));
        
        assertFalse(repository.getOne(note.getId()).getPad().isPresent());
    }

}
