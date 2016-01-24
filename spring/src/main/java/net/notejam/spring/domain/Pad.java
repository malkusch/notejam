package net.notejam.spring.domain;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.AbstractPersistable;

import net.notejam.spring.domain.account.User;
import net.notejam.spring.domain.account.security.Owned;

/**
 * An optional pad for grouping notes.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Entity
@Table(indexes = @Index(columnList = "created") )
public final class Pad extends AbstractPersistable<Integer>implements Owned {

    private static final long serialVersionUID = -1186217744141902841L;

    /**
     * The time of creation.
     */
    @NotNull
    private Instant created;

    /**
     * The name.
     */
    @NotNull
    private Name name;

    /**
     * The owner.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User owner;

    /**
     * Builds a new pad.
     *
     * @param name
     *            name of this pad. Null is not allowed.
     * @param owner
     *            user who owns this pad. Null is not allowed.
     */
    public Pad(final Name name, final User owner) {
        if (name == null) {
            throw new NullPointerException();
        }
        if (owner == null) {
            throw new NullPointerException();
        }
        
        this.created = Instant.now();
        this.owner = owner;
        this.name = name;
    }

    /**
     * Returns time of creation.
     *
     * @return time of creation.
     */
    public Instant getCreated() {
        return created;
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
     * Edits the pad
     *
     * @param name
     *            edited name. Null is not allowed.
     */
    public void edit(final Name name) {
        if (name == null) {
            throw new NullPointerException();
        }
        this.name = name;
    }

    @Override
    public User getOwner() {
        return owner;
    }
    
    /**
     * Builds an incomplete pad for the persistence framework.
     */
    Pad() {
        // The persistence framework needs a default constructor.
    }

}
