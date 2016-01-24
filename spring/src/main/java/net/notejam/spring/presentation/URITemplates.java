package net.notejam.spring.presentation;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.util.UriComponentsBuilder;

import net.notejam.spring.domain.account.recovery.PasswordRecoveryProcess;

/**
 * URI templates.
 *
 * @author markus@malkusch.de
 *
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 * @see <a href=
 *      "https://github.com/komarserjio/notejam/blob/master/contribute.rst#pages">
 *      Requirements</a>
 */
public interface URITemplates {

    /**
     * The sign up path.
     */
    final String SIGNUP = "/signup";

    /**
     * The sign in path.
     */
    final String SIGNIN = "/signin";

    /**
     * The sign out path.
     */
    final String SIGNOUT = "/signout";

    /**
     * The settings path.
     */
    final String SETTINGS = "/settings";

    /**
     * The forgot password path.
     */
    final String FORGOT_PASSWORD = "/forgot-password";

    /**
     * The recover password path.
     */
    final String RECOVER_PASSWORD = "/recover-password/{id}/{token}";

    /**
     * The create pad path.
     */
    final String CREATE_PAD = "/pads/create";

    /**
     * The edit pad path.
     */
    final String EDIT_PAD = "/pads/{id}/edit";

    /**
     * The view pad path.
     */
    final String VIEW_PAD = "/pads/{id}";

    /**
     * The delete pad path.
     */
    final String DELETE_PAD = "/pads/{id}/delete";

    /**
     * The create note path.
     */
    final String CREATE_NOTE = "/notes/create";

    /**
     * The create note with a preselected pad path.
     */
    final String CREATE_NOTE_FOR_PAD = CREATE_NOTE + "?pad={id}";

    /**
     * The edit note path.
     */
    final String EDIT_NOTE = "/notes/{id}/edit";

    /**
     * The view note path.
     */
    final String VIEW_NOTE = "/notes/{id}";

    /**
     * The delete note path.
     */
    final String DELETE_NOTE = "/notes/{id}/delete";

    /**
     * The view all notes path. This is the default path.
     */
    final String VIEW_ALL_NOTES = "/";

    /**
     * Builds the URI for the created pad.
     *
     * @param id
     *            The pad id
     * @return URI
     */
    static URI buildCreatedPadURI(final int id) {
	UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(VIEW_PAD);
	uriBuilder.queryParam("createdSuccessfully");
	return uriBuilder.buildAndExpand(id).toUri();
    }
    
    /**
     * Builds the URI for the created note.
     *
     * @param id
     *            The note id
     * @return URI
     */
    static URI buildCreatedNoteURI(final int id) {
	UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(VIEW_NOTE);
	uriBuilder.queryParam("successful");
	return uriBuilder.buildAndExpand(id).toUri();
    }
    
    /**
     * Builds the URI for the edited note.
     *
     * @param id
     *            The note id
     * @return URI
     */
    static URI buildEditedNoteURI(final int id) {
	UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(VIEW_NOTE);
	uriBuilder.queryParam("successful");
	return uriBuilder.buildAndExpand(id).toUri();
    }

    /**
     * Builds the URI for the edited pad.
     *
     * @param id
     *            pad id
     * @return URI
     */
    static URI buildEditedPadURI(final int id) {
	UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(EDIT_PAD);
	uriBuilder.queryParam("success");
	return uriBuilder.buildAndExpand(id).toUri();
    }

    /**
     * Builds the fully qualified URI for recovering the password.
     *
     * @param process
     *            password recovery token
     * @param baseUri
     *            base URI to build a fully qualified URI
     * @return password recovery URI
     */
    static URI buildPasswordRecoveryURI(final PasswordRecoveryProcess process, final URI baseUri) {
	UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUri(baseUri);

	Map<String, String> uriVariables = new HashMap<>();
	uriVariables.put("id", process.getId().toString());
	uriVariables.put("token", process.token().toString());

	return uriBuilder.replacePath(RECOVER_PASSWORD).buildAndExpand(uriVariables).toUri();
    }

}
