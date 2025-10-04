package org.laladev.moneyjinn.model.validation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.AbstractEntityID;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationResultItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private AbstractEntityID<? extends Serializable> key;
    private ErrorCode error;
    private List<String> variableArray;

    public ValidationResultItem(final AbstractEntityID<?> key, final ErrorCode error) {
        super();
        this.key = key;
        this.error = error;
    }
}
