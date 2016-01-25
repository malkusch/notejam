package net.notejam.spring.presentation.web.account.recovery;

import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;

import net.notejam.spring.domain.account.User;
import net.notejam.spring.domain.account.UserRepository;
import net.notejam.spring.domain.account.security.PasswordEncodingService;
import net.notejam.spring.domain.account.security.PlainTextPassword;
import net.notejam.spring.presentation.URITemplates;
import net.notejam.spring.test.IntegrationTest;
import net.notejam.spring.test.MockMvcProvider;
import net.notejam.spring.test.SignedUpUserProvider;

/**
 * An integration test for the RecoveryController.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RecoveryControllerIT {

    @Rule
    @Autowired
    public SignedUpUserProvider userProvider;

    @Rule
    @Autowired
    public MockMvcProvider mockMvcProvider;
    
    @Autowired
    private PasswordEncodingService encodingService;
    
    @Autowired
    private UserRepository userRepository;

    @Rule
    public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP);

    /**
     * Starting the process for a non existing user doesn't reveal anything
     * about the user database.
     */
    @Test
    public void dontLeverageUserDatabase() throws Exception {
	mockMvcProvider
		.getMockMvc().perform(post(URITemplates.FORGOT_PASSWORD)
			.param("emailAddress", "nonexsiting@exmaple.org").with(csrf()))
		.andExpect(status().is3xxRedirection());
    }

    /**
     * Tests password recovery process.
     */
    @Test
    public void recoverPassword() throws Exception {
	User user = userProvider.getUser();
	
	// Send recovery mail
	mockMvcProvider
		.getMockMvc().perform(post(URITemplates.FORGOT_PASSWORD)
			.param("emailAddress", user.getEmailAddress().toString()).with(csrf()))
		.andExpect(status().is3xxRedirection());

	// Read mail
	greenMail.waitForIncomingEmail(1);
	String mail = GreenMailUtil.getBody(greenMail.getReceivedMessages()[0]);
	URI recoveryURI = readURI(mail);

	// Recover password
	MvcResult result = mockMvcProvider
		.getMockMvc().perform(get(recoveryURI))
		.andReturn();
	PlainTextPassword password = new PlainTextPassword(result.getModelAndView().getModel().get("password").toString());

	user = userRepository.findOne(user.getId());
	assertTrue(encodingService.matches(password, user.getPassword()));
    }

    /**
     * parses the URI from an Email.
     * 
     * @param mail
     *            email
     * @return uri
     * @throws URISyntaxException
     */
    private static URI readURI(String mail) {
	Matcher matcher = Pattern.compile("<([^>]+)>").matcher(mail);
	assertTrue(matcher.find());
	return URI.create(matcher.group(1));
    }

}
