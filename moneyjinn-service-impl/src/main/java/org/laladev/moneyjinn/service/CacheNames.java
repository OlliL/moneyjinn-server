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

package org.laladev.moneyjinn.service;

public class CacheNames {
  public static final String USER_BY_ID = "userById";
  public static final String USER_BY_NAME = "userByName";
  public static final String ALL_ACCESS_RELATIONS_BY_USER_ID = "allAccessRelationsByUserId";
  public static final String ALL_GROUPS = "allGroups";
  public static final String GROUP_BY_ID = "groupById";
  public static final String ALL_POSTINGACCOUNTS = "allPostingAccounts";
  public static final String POSTINGACCOUNT_BY_ID = "postingAccountById";
  public static final String ALL_CAPITALSOURCES = "allCapitalsources";
  public static final String GROUP_CAPITALSOURCES_BY_DATE = "groupCapitalsourcesByDate";
  public static final String CAPITALSOURCE_BY_ID = "capitalsourceById";
  public static final String ALL_CONTRACTPARTNER = "allContractpartner";
  public static final String ALL_CONTRACTPARTNER_BY_DATE = "allContractpartnerByDate";
  public static final String CONTRACTPARTNER_BY_ID = "contractpartnerById";
  public static final String ALL_PRE_DEF_MONEYFLOWS = "allPreDefMoneyflows";
  public static final String MONEYFLOW_BY_ID = "moneyflowById";
  public static final String MONEYFLOW_YEARS = "moneyflowYears";
  public static final String MONEYFLOW_MONTH = "moneyflowMonths";
  public static final String CONTRACTPARTNER_ACCOUNT_BY_ID = "contractpartnerAccountById";
  public static final String CONTRACTPARTNER_ACCOUNTS_BY_PARTNER = "contractpartnerAccountsByPartner";

  private CacheNames() {
  }
}
