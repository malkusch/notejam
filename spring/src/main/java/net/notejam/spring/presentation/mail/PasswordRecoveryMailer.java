package net.notejam.spring.presentation.mail;

import java.net.URI;
import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import net.notejam.spring.domain.account.EmailAddress;

/**
 * A password recovery mailer.
 *
 * @author markus@malkusch.de
 *
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 * @see <a href=
 *      "https://github.com/komarserjio/notejam/blob/master/contribute.rst#pages">
 *      Requirements</a>
 */
@Service
public class PasswordRecoveryMailer {

    /**
     * The optional mail transport.
     */
    private final Optional<MailSender> mailTransport;

    /**
     * The sender's email address.
     */
    private final String sender;

    /**
     * The message source.
     */
    private final MessageSource messageSource;

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordRecoveryMailer.class);

    /**
     * Builds the mailer.
     *
     * @param sender
     *            sender of the mail
     * @param messageSource
     *            message source for translations
     * @param mailTransport
     *            optional mail transport
     */
    @Autowired
    PasswordRecoveryMailer(@Value("${email.sender}") final String sender, final MessageSource messageSource,
            final Optional<MailSender> mailTransport) {

        this.sender = sender;
        this.messageSource = messageSource;
        this.mailTransport = mailTransport;
    }

    /**
     * Sends the password recovery mail.
     *
     * @param receiver
     *            email address of the receiver
     * @param uri
     *            recovery uri
     * @param locale
     *            locale in which the process should happen
     */
    @Async("mailExecutor")
    public void sendRecoveryMail(final EmailAddress receiver, final URI uri, final Locale locale) {
        if (!mailTransport.isPresent()) {
            LOGGER.warn(
                    "Mail transport is not available. Consider setting the property spring.mail.host in the file application.properties.");
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(sender);
        message.setTo(receiver.toString());
        message.setSubject(messageSource.getMessage("forgot.mail.subject", null, locale));
        message.setText(messageSource.getMessage("forgot.mail.message", new URI[] { uri }, locale));

        mailTransport.get().send(message);
    }

}
