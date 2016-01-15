package net.notejam.spring.pad;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.AbstractPersistable;

import net.notejam.spring.security.owner.Owned;
import net.notejam.spring.user.User;

/**
 * The pad groups notes.
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
    @ManyToOne
    @NotNull
    private User user;

    /**
     * Builds a new pad.
     *
     * @param name
     *            name of this pad. Null is not allowed.
     * @param user
     *            user who owns this pad. Null is not allowed.
     */
    Pad(final Name name, final User user) {
        if (name == null) {
            throw new NullPointerException();
        }
        if (user == null) {
            throw new NullPointerException();
        }
        
        this.created = Instant.now();
        this.user = user;
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
    public User getUser() {
        return user;
    }
    
    /**
     * Builds an incomplete pad for the persistence framework.
     */
    Pad() {
        // The persistence framework needs a default constructor.
    }

}
