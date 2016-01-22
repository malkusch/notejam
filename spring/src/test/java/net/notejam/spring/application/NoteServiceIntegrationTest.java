package net.notejam.spring.application;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.notejam.spring.domain.Name;
import net.notejam.spring.domain.Note;
import net.notejam.spring.domain.Pad;
import net.notejam.spring.domain.PadRepository;
import net.notejam.spring.domain.account.User;
import net.notejam.spring.test.IntegrationTest;
import net.notejam.spring.test.SignedUpUserProvider;

/**
 * An integration test for the {@link NoteService}.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@WithUserDetails(SignedUpUserProvider.EMAIL)
public class NoteServiceIntegrationTest {

    @Autowired
    private NoteService service;
    
    @Autowired
    private PadRepository padRepository;
    
    @Rule
    @Autowired
    public SignedUpUserProvider userProvider;

    /**
     * Tests writeNote() without pad.
     */
    @Test
    public void testWriteNoteWithoutPad() {
	Name name = new Name("name");
	String text = "text";
	User owner = userProvider.getUser();
	
	int id = service.writeNote(name, owner, Optional.empty(), text).getId();
	
	Note note = service.showNote(id).get();
        assertEquals(name, note.getName());
        assertEquals(owner, note.getOwner());
        assertFalse(note.getPad().isPresent());
        assertEquals(text, note.getText());
    }
    
    /**
     * Tests writing a note in a pad.
     */
    @Test
    public void testWriteNoteInPad() {
        Pad pad = new Pad(new Name("name"), userProvider.getUser());
        padRepository.save(pad);

        int id = service.writeNote(new Name("name"), userProvider.getUser(), Optional.of(pad.getId()), "text").getId();
        
        Note note = service.showNote(id).get();

        assertEquals(pad.getId(), note.getPad().get().getId());
    }
    
    /**
     * Tests deleteNote().
     */
    @Test
    public void testDeleteNote() {
        int id = service.writeNote(new Name("name"), userProvider.getUser(), Optional.empty(), "text").getId();

        service.deleteNote(id);

        assertFalse(service.showNote(id).isPresent());
    }

}
