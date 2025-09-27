package org.laladev.moneyjinn.model.capitalsource;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.laladev.moneyjinn.model.*;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class Capitalsource extends AbstractValidPeriodEntity<CapitalsourceID>
        implements IHasBankAccount, IHasUser, IHasGroup {
    @Serial
    private static final long serialVersionUID = 1L;
    private User user;
    private Group group;
    private CapitalsourceType type;
    private CapitalsourceState state;
    private BankAccount bankAccount;
    private String comment;
    private boolean groupUse;
    private CapitalsourceImport importAllowed;

    public Capitalsource(final CapitalsourceID id) {
        super.setId(id);
    }

    public final boolean isAsset() {
        return this.getType() == CapitalsourceType.CURRENT_ASSET || this.getType() == CapitalsourceType.LONG_TERM_ASSET;
    }

    public final boolean groupUseAllowed(final UserID userId) {
        return userId.equals(this.user.getId()) || this.groupUse;
    }
}
