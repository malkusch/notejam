package net.notejam.spring.presentation.controller;

import static net.notejam.spring.test.UriUtil.buildUri;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.notejam.spring.application.NoteService;
import net.notejam.spring.domain.Name;
import net.notejam.spring.domain.Note;
import net.notejam.spring.domain.NoteRepository;
import net.notejam.spring.presentation.URITemplates;
import net.notejam.spring.test.IntegrationTest;
import net.notejam.spring.test.MockMvcProvider;
import net.notejam.spring.test.SignedUpUserProvider;

/**
 * An integration test for deleting a note.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@WithUserDetails(SignedUpUserProvider.EMAIL)
public class DeleteNoteTest {

    @Rule
    @Autowired
    public MockMvcProvider mockMvcProvider;
    
    @Rule
    @Autowired
    public SignedUpUserProvider userProvider;
    
    @Autowired
    private NoteRepository repository;
    
    @Autowired
    private NoteService service;
    
    private Note note;
    
    /**
     * The edit note uri.
     */
    private String uri;
    
    @Before
    public void setNote() {
        note = service.writeNote(new Name("name"), userProvider.getUser(), Optional.empty(), "text");
    }
    
    @Before
    public void setUri() {
        uri = buildUri(URITemplates.DELETE_NOTE, note.getId());
    }

    /**
     * Note can be deleted by its owner.
     */
    @Test
    public void noteCanBeDeleted() throws Exception {
        mockMvcProvider.getMockMvc().perform(post(uri)
                .with(csrf()))

            .andExpect(status().is3xxRedirection());
        
        assertNull(repository.findOne(note.getId()));
    }
    
    /**
     * Note can't be deleted by not an owner.
     */
    @Test
    public void noteCannotBeDeletedByOtherUser() throws Exception {
        mockMvcProvider.getMockMvc().perform(post(uri)
                .with(csrf())
                .with(user(userProvider.getAnotherAuthenticatedUser())))

            .andExpect(status().is(403));
        
        assertNotNull(repository.findOne(note.getId()));
    }
    
}
