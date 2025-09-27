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

package org.laladev.moneyjinn.hbci.core.entity;

public class AbstractAccountEntity {
    private Integer id;
    private String myIban;
    private String myBic;
    private Long myAccountnumber;
    private Integer myBankcode;

    public final Integer getId() {
        return this.id;
    }

    public final void setId(final Integer id) {
        this.id = id;
    }

    public final String getMyIban() {
        return this.myIban;
    }

    public final void setMyIban(final String myIban) {
        this.myIban = myIban;
    }

    public final String getMyBic() {
        return this.myBic;
    }

    public final void setMyBic(final String myBic) {
        this.myBic = myBic;
    }

    public final Long getMyAccountnumber() {
        return this.myAccountnumber;
    }

    public final void setMyAccountnumber(final Long myAccountnumber) {
        this.myAccountnumber = myAccountnumber;
    }

    public final Integer getMyBankcode() {
        return this.myBankcode;
    }

    public final void setMyBankcode(final Integer myBankcode) {
        this.myBankcode = myBankcode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.myAccountnumber == null) ? 0 : this.myAccountnumber.hashCode());
        result = prime * result + ((this.myBankcode == null) ? 0 : this.myBankcode.hashCode());
        result = prime * result + ((this.myBic == null) ? 0 : this.myBic.hashCode());
        result = prime * result + ((this.myIban == null) ? 0 : this.myIban.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final AbstractAccountEntity other = (AbstractAccountEntity) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        if (this.myAccountnumber == null) {
            if (other.myAccountnumber != null) {
                return false;
            }
        } else if (!this.myAccountnumber.equals(other.myAccountnumber)) {
            return false;
        }
        if (this.myBankcode == null) {
            if (other.myBankcode != null) {
                return false;
            }
        } else if (!this.myBankcode.equals(other.myBankcode)) {
            return false;
        }
        if (this.myBic == null) {
            if (other.myBic != null) {
                return false;
            }
        } else if (!this.myBic.equals(other.myBic)) {
            return false;
        }
        if (this.myIban == null) {
            return other.myIban == null;
        } else {
            return this.myIban.equals(other.myIban);
        }
    }

}
