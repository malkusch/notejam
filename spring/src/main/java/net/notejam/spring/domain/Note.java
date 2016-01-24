package net.notejam.spring.domain;

import java.time.Instant;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.jpa.domain.AbstractPersistable;

import net.notejam.spring.domain.account.User;
import net.notejam.spring.domain.account.security.Owned;

/**
 * The note.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Entity
@Table(indexes = { @Index(columnList = "updated"), @Index(columnList = "name") })
public final class Note extends AbstractPersistable<Integer> implements Owned {

    private static final long serialVersionUID = -1445367127777923455L;

    /**
     * The last update time.
     */
    @NotNull
    private Instant updated;

    /**
     * The owner.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User owner;

    /**
     * The pad to which this note belongs to.
     */
    @ManyToOne
    private Pad pad;

    /**
     * The name.
     */
    @NotNull
    private Name name;

    /**
     * The text.
     */
    @NotEmpty
    @Lob
    @Column(length = 10000)
    private String text;

    /**
     * Writes a new note.
     * 
     * @param name
     *            name
     * @param owner
     *            owner
     * @param pad
     *            optional pad
     * @param text
     *            text
     */
    public Note(final Name name, final User owner, final Optional<Pad> pad, final String text) {
	assertValid(name, owner, pad, text);

	this.name = name;
	this.owner = owner;
	this.pad = pad.orElse(null);
	this.text = text;
	this.updated = Instant.now();
    }

    /**
     * Edits this note.
     * 
     * @param name
     *            edited name, not null
     * @param pad
     *            optional edited pad
     * @param text
     *            edited text, not null
     */
    public void edit(final Name name, final Optional<Pad> pad, final String text) {
	assertValid(name, owner, pad, text);

	this.name = name;
	this.pad = pad.orElse(null);
	this.text = text;
	this.updated = Instant.now();
    }

    /**
     * Check the invariants.
     * 
     * @param name
     *            name
     * @param owner
     *            owner
     * @param pad
     *            optional pad
     * @param text
     *            text
     */
    private static void assertValid(final Name name, final User owner, final Optional<Pad> pad,
	    final String text) {
	if (name == null) {
	    throw new NullPointerException();
	}
	if (pad == null) {
	    throw new NullPointerException();
	}
	if (owner == null) {
	    throw new NullPointerException();
	}
	if (text == null) {
	    throw new NullPointerException();
	}
	pad.ifPresent((Pad p) -> {
	    if (!owner.equals(p.getOwner())) {
		throw new IllegalArgumentException("The pad's owner must be identical with the note's owner.");
	    }
	});
    }

    /**
     * Returns the name.
     *
     * @return The name.
     */
    public Name getName() {
	return name;
    }

    /**
     * Returns the text.
     *
     * @return The text
     */
    public String getText() {
	return text;
    }

    /**
     * Returns the pad.
     *
     * @return The pad or null.
     */
    public Optional<Pad> getPad() {
	return Optional.ofNullable(pad);
    }

    @Override
    public User getOwner() {
	return owner;
    }

    /**
     * Returns the last update time.
     *
     * @return The time.
     */
    public Instant getUpdated() {
	return updated;
    }
    
    /**
     * Builds an incomplete note for the persistence framework.
     */
    Note() {
        // The persistence framework needs a default constructor.
    }

}
