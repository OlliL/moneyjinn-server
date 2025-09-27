package org.laladev.moneyjinn.model.capitalsource;

import lombok.NoArgsConstructor;
import org.laladev.moneyjinn.model.AbstractEntityID;

import java.io.Serial;

/**
 * The unique ID of {@link Capitalsource}.
 *
 * @author Oliver Lehmann
 */
// Is needed for Settings recovering from the JSON string which is stored in the DB (Jackson
// Mapper).
@NoArgsConstructor
public class CapitalsourceID extends AbstractEntityID<Long> {
    @Serial
    private static final long serialVersionUID = 1L;

    public CapitalsourceID(final Long id) {
        super(id);
    }
}
