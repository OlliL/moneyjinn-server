//
// Copyright (c) 2015-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

import lombok.Data;
import org.laladev.moneyjinn.core.error.ErrorCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Data
public class BankAccount implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Integer ACCOUNT_NUMBER_MAX_LENGTH = 34;
    private static final Integer BANK_CODE_MAX_LENGTH = 11;
    private static final Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
    private String accountNumber;
    private String bankCode;

    public BankAccount(final String accountNumber, final String bankCode) {
        super();
        this.accountNumber = accountNumber;
        this.setBankCode(bankCode);
    }

    public final void setBankCode(final String bankCode) {
        // Always fill 8 digits BIC to 11 digits BIC!
        if (bankCode != null && bankCode.length() == 8 && !bankCode.matches("^\\d+$")) {
            this.bankCode = bankCode + "XXX";
        } else {
            this.bankCode = bankCode;
        }
    }

    public List<ErrorCode> checkValidity() {
        final List<ErrorCode> errorCodes = new ArrayList<>();
        if (this.accountNumber != null) {
            if (this.accountNumber.length() > BankAccount.ACCOUNT_NUMBER_MAX_LENGTH) {
                errorCodes.add(ErrorCode.ACCOUNT_NUMBER_TO_LONG);
            }
            if (pattern.matcher(this.accountNumber).find()) {
                errorCodes.add(ErrorCode.ACCOUNT_NUMBER_CONTAINS_ILLEGAL_CHARS);
            }
        }
        if (this.bankCode != null) {
            if (this.bankCode.length() > BankAccount.BANK_CODE_MAX_LENGTH) {
                errorCodes.add(ErrorCode.BANK_CODE_TO_LONG);
            }
            if (pattern.matcher(this.bankCode).find()) {
                errorCodes.add(ErrorCode.BANK_CODE_CONTAINS_ILLEGAL_CHARS);
            }
        }
        return errorCodes;
    }
}
