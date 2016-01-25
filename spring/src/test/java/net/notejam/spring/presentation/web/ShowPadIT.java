package net.notejam.spring.presentation.web;

import static net.notejam.spring.test.UriUtil.buildUri;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import net.notejam.spring.application.PadService;
import net.notejam.spring.domain.Name;
import net.notejam.spring.domain.Pad;
import net.notejam.spring.presentation.URITemplates;
import net.notejam.spring.test.IntegrationTest;
import net.notejam.spring.test.MockMvcProvider;
import net.notejam.spring.test.SignedUpUserProvider;

/**
 * An integration test for showing a pad.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@WithUserDetails(SignedUpUserProvider.EMAIL)
public class ShowPadIT {

    @Rule
    @Autowired
    public MockMvcProvider mockMvcProvider;
    
    @Rule
    @Autowired
    public SignedUpUserProvider userProvider;
    
    @Autowired
    private PadService padService;
    
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
        
        uri = buildUri(URITemplates.VIEW_PAD, pad.getId());
    }
    
    /**
     * Pad can be viewed by its owner.
     */
    @Test
    public void padCanBeViewed() throws Exception {
        mockMvcProvider.getMockMvc().perform(get(uri))
            .andExpect(model().hasNoErrors())
            .andExpect(status().is2xxSuccessful())
            .andExpect(view().name("notes"));
    }
    
    /**
     * pad can't be viewed by not an owner
     */
    @Test
    public void padCannotBeViewedByOtherUser() throws Exception {
        mockMvcProvider.getMockMvc().perform(get(uri)
                .with(user(userProvider.getAnotherAuthenticatedUser())))
            .andExpect(status().is(403));
    }
    
}
