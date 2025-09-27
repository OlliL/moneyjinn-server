package org.laladev.moneyjinn.model.etf;

import org.laladev.moneyjinn.model.AbstractEntityID;

import java.io.Serial;

public class EtfIsin extends AbstractEntityID<String> {
    @Serial
    private static final long serialVersionUID = 1L;

    public EtfIsin(final String id) {
        super(id);
    }
}
