//
//Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without
//modification, are permitted provided that the following conditions
//are met:
//1. Redistributions of source code must retain the above copyright
//notice, this list of conditions and the following disclaimer
//2. Redistributions in binary form must reproduce the above copyright
//notice, this list of conditions and the following disclaimer in the
//documentation and/or other materials provided with the distribution.
//
//THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
//ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
//ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
//FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
//DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
//OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
//HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
//LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
//OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
//SUCH DAMAGE.
//
package org.laladev.hbci.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@NamedQueries({
        @NamedQuery(
                name = "findDailyBalance",
                query = "FROM BalanceDaily b WHERE b.myIban = :myIban AND b.myBic = :myBic AND b.myAccountnumber = :myAccountnumber" +
                        " AND  b.myBankcode = :myBankcode AND b.balanceDate = :balanceDate"
        )
})

@Entity
@Table(name = "balance_daily")
public class BalanceDaily {
    private int id;
    private String myIban;
    private String myBic;
    private long myAccountnumber;
    private int myBankcode;
    private Date balanceDate;
    private Timestamp lastTransactionDate;
    private BigDecimal balanceAvailableValue;
    private BigDecimal lineOfCreditValue;
    private String balanceCurrency;
    private Timestamp lastBalanceUpdate;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column
    public String getMyIban() {
        return myIban;
    }

    public void setMyIban(String myIban) {
        this.myIban = myIban;
    }

    @Column
    public String getMyBic() {
        return myBic;
    }

    public void setMyBic(String myBic) {
        this.myBic = myBic;
    }

    @Column
    public long getMyAccountnumber() {
        return myAccountnumber;
    }

    public void setMyAccountnumber(long myAccountnumber) {
        this.myAccountnumber = myAccountnumber;
    }


    @Column
    public int getMyBankcode() {
        return myBankcode;
    }

    public void setMyBankcode(int myBankcode) {
        this.myBankcode = myBankcode;
    }

    @Column
    public Date getBalanceDate() {
        return balanceDate;
    }

    public void setBalanceDate(Date balanceDate) {
        this.balanceDate = balanceDate;
    }

    @Column
    public Timestamp getLastTransactionDate() {
        return lastTransactionDate;
    }

    public void setLastTransactionDate(Timestamp lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

    @Column
    public BigDecimal getBalanceAvailableValue() {
        return balanceAvailableValue;
    }

    public void setBalanceAvailableValue(BigDecimal balanceAvailableValue) {
        this.balanceAvailableValue = balanceAvailableValue;
    }

    @Column
    public BigDecimal getLineOfCreditValue() {
        return lineOfCreditValue;
    }

    public void setLineOfCreditValue(BigDecimal lineOfCreditValue) {
        this.lineOfCreditValue = lineOfCreditValue;
    }

    @Column
    public String getBalanceCurrency() {
        return balanceCurrency;
    }

    public void setBalanceCurrency(String balanceCurrency) {
        this.balanceCurrency = balanceCurrency;
    }

    @Column
    public Timestamp getLastBalanceUpdate() {
        return lastBalanceUpdate;
    }

    public void setLastBalanceUpdate(Timestamp lastBalanceUpdate) {
        this.lastBalanceUpdate = lastBalanceUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BalanceDaily)) return false;

        BalanceDaily that = (BalanceDaily) o;

        if (id != that.id) return false;
        if (myAccountnumber != that.myAccountnumber) return false;
        if (myBankcode != that.myBankcode) return false;
        if (!balanceDate.equals(that.balanceDate)) return false;
        if (!myBic.equals(that.myBic)) return false;
        if (myIban != null ? !myIban.equals(that.myIban) : that.myIban != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (myIban != null ? myIban.hashCode() : 0);
        result = 31 * result + myBic.hashCode();
        result = 31 * result + (int) (myAccountnumber ^ (myAccountnumber >>> 32));
        result = 31 * result + myBankcode;
        result = 31 * result + balanceDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "BalanceDaily{" +
                "id=" + id +
                ", myIban='" + myIban + '\'' +
                ", myBic='" + myBic + '\'' +
                ", myAccountnumber=" + myAccountnumber +
                ", myBankcode=" + myBankcode +
                ", balanceDate=" + balanceDate +
                ", lastTransactionDate=" + lastTransactionDate +
                ", balanceAvailableValue=" + balanceAvailableValue +
                ", lineOfCreditValue=" + lineOfCreditValue +
                ", balanceCurrency='" + balanceCurrency + '\'' +
                ", lastBalanceUpdate=" + lastBalanceUpdate +
                '}';
    }
}
