//
// Copyright (c) 2015-2023 Oliver Lehmann <lehmann@ans-netz.de>
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
// OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE.
//

package org.laladev.moneyjinn.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.moneyflow.Moneyflow;

/**
 * A pre defined Moneyflow is a Moneyflow where certain attributes are predefined. <br>
 * <br>
 * For example - every month you give your mother 100 EUR with the {@link PostingAccount} "gift" and
 * the comment "I love you so much". Every month you would have to enter the same data again and
 * again. With a pre defined Moneyflow, you can predefine this data once and just "quickadd" this
 * {@link Moneyflow} whenever you spend the money.<br>
 * <br>
 * The pre defined data is meant to be a adjustable before the {@link Moneyflow} is added (client
 * logic). So you could also create placeholders where for example the amount varies from booking to
 * booking (for example credit card billing), but the {@link Contractpartner},
 * {@link PostingAccount}, {@link Capitalsource} and Comment are always the same.<br>
 * <br>
 * It is meant to ease the adding of reoccuring {@link Moneyflow}s.
 *
 * @author Oliver Lehmann
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PreDefMoneyflow extends AbstractEntity<PreDefMoneyflowID>
    implements IHasCapitalsource, IHasContractpartner, IHasPostingAccount, IHasUser {
  private static final long serialVersionUID = 1L;
  private User user;
  private BigDecimal amount;
  private Capitalsource capitalsource;
  private Contractpartner contractpartner;
  private String comment;
  private LocalDate creationDate;
  private boolean onceAMonth;
  private LocalDate lastUsedDate;
  private PostingAccount postingAccount;
}
