CREATE TABLE access_groups (
    groupid bigserial NOT NULL,
    name character varying(20) NOT NULL
);
CREATE TABLE access_relation (
    mau_userid integer NOT NULL,
    mag_groupid integer NOT NULL,
    validfrom date NOT NULL,
    validtil date NOT NULL
);
CREATE TABLE access_users (
    userid bigserial NOT NULL,
    name character varying(20) NOT NULL,
    password character varying(60) NOT NULL,
    role character varying(8) NOT NULL,
    change_password smallint NOT NULL
);
CREATE TABLE capitalsources (
    capitalsourceid bigserial NOT NULL,
    mau_userid integer NOT NULL,
    mag_groupid integer NOT NULL,
    type smallint DEFAULT '1'::smallint NOT NULL,
    state smallint DEFAULT '1'::smallint NOT NULL,
    accountnumber character varying(34) DEFAULT NULL::character varying,
    bankcode character varying(11) DEFAULT NULL::character varying,
    comment character varying(255) NOT NULL,
    validtil date DEFAULT '2999-12-31'::date NOT NULL,
    validfrom date DEFAULT '1970-01-01'::date NOT NULL,
    att_group_use smallint DEFAULT '0'::smallint NOT NULL,
    import_allowed smallint DEFAULT '0'::smallint NOT NULL
);
CREATE TABLE cmp_data_formats (
    formatid bigserial NOT NULL,
    name character varying(50) NOT NULL,
    start_trigger_0 character varying(50) DEFAULT NULL::character varying,
    start_trigger_1 character varying(50) DEFAULT NULL::character varying,
    start_trigger_2 character varying(50) DEFAULT NULL::character varying,
    end_trigger_0 character varying(50) DEFAULT NULL::character varying,
    startline character varying(255) NOT NULL,
    delimiter character varying(1) NOT NULL,
    pos_date smallint NOT NULL,
    pos_invoicedate smallint,
    pos_partner smallint,
    pos_amount smallint NOT NULL,
    pos_comment smallint,
    fmt_date character varying(10) NOT NULL,
    fmt_amount_decimal character varying(1) NOT NULL,
    fmt_amount_thousand character varying(1) DEFAULT NULL::character varying,
    pos_partner_alt smallint,
    pos_partner_alt_pos_key smallint,
    pos_partner_alt_keyword character varying(255) DEFAULT NULL::character varying,
    pos_credit_debit_indicator smallint,
    credit_indicator character varying(2) DEFAULT NULL::character varying
);
CREATE TABLE contractpartneraccounts (
    contractpartneraccountid bigserial NOT NULL,
    mcp_contractpartnerid integer NOT NULL,
    accountnumber character varying(34) NOT NULL,
    bankcode character varying(11) DEFAULT NULL::character varying
);
CREATE TABLE contractpartners (
    contractpartnerid bigserial NOT NULL,
    mau_userid integer NOT NULL,
    mag_groupid integer NOT NULL,
    name character varying(100) DEFAULT ''::character varying NOT NULL,
    street character varying(100) DEFAULT ''::character varying,
    postcode integer DEFAULT 0,
    town character varying(100) DEFAULT ''::character varying,
    country character varying(100) DEFAULT ''::character varying,
    validfrom date NOT NULL,
    validtil date NOT NULL,
    mmf_comment character varying(100) DEFAULT NULL::character varying,
    mpa_postingaccountid integer
);
CREATE TABLE etf (
    etfid bigserial NOT NULL,
    mau_userid integer NOT NULL,
    mag_groupid integer NOT NULL,
    isin character varying(30) NOT NULL,
    name character varying(60) NOT NULL,
    wkn character varying(10) NOT NULL,
    ticker character varying(10) NOT NULL,
    chart_url character varying(255) DEFAULT NULL::character varying,
    trans_cost_abs numeric(5,2) DEFAULT NULL::numeric,
    trans_cost_rel numeric(5,2) DEFAULT NULL::numeric,
    trans_cost_max numeric(8,2) DEFAULT NULL::numeric,
    part_tax_exempt numeric(5,2) DEFAULT NULL::numeric
);
CREATE TABLE etfflows (
    etfflowid bigserial NOT NULL,
    met_etfid integer NOT NULL,
    flowdate timestamp without time zone NOT NULL,
    amount numeric(12,6) NOT NULL,
    price numeric(11,6) NOT NULL
);
CREATE TABLE etfpreliminarylumpsum (
    etfpreliminarylumpsumid bigserial NOT NULL,
    met_etfid integer NOT NULL,
    "year" smallint NOT NULL,
    type smallint NOT NULL,
    amount_per_piece numeric(12,8) DEFAULT NULL::numeric,
    amount01 numeric(8,2) DEFAULT NULL::numeric,
    amount02 numeric(8,2) DEFAULT NULL::numeric,
    amount03 numeric(8,2) DEFAULT NULL::numeric,
    amount04 numeric(8,2) DEFAULT NULL::numeric,
    amount05 numeric(8,2) DEFAULT NULL::numeric,
    amount06 numeric(8,2) DEFAULT NULL::numeric,
    amount07 numeric(8,2) DEFAULT NULL::numeric,
    amount08 numeric(8,2) DEFAULT NULL::numeric,
    amount09 numeric(8,2) DEFAULT NULL::numeric,
    amount10 numeric(8,2) DEFAULT NULL::numeric,
    amount11 numeric(8,2) DEFAULT NULL::numeric,
    amount12 numeric(8,2) DEFAULT NULL::numeric
);
CREATE TABLE etfvalues (
    isin character varying(30) NOT NULL,
    date date NOT NULL,
    buy_price numeric(10,3) NOT NULL,
    sell_price numeric(10,3) NOT NULL,
    changedate timestamp without time zone NOT NULL
);
CREATE TABLE imp_data (
    dataid bigserial NOT NULL,
    date character varying(10) NOT NULL,
    amount character varying(20) NOT NULL,
    source character varying(100) NOT NULL,
    partner character varying(100) NOT NULL,
    comment character varying(100) NOT NULL,
    status smallint DEFAULT '1'::smallint NOT NULL
);
CREATE TABLE imp_mapping_partner (
    partner_from character varying(100) NOT NULL,
    partner_to character varying(100) NOT NULL
);
CREATE TABLE imp_mapping_source (
    source_from character varying(100) NOT NULL,
    source_to character varying(100) NOT NULL
);
CREATE TABLE impbalance (
    mcs_capitalsourceid integer NOT NULL,
    balance numeric(8,2) NOT NULL,
    changedate timestamp without time zone NOT NULL
);
CREATE TABLE impmoneyflowreceipts (
    impmoneyflowreceiptid bigserial NOT NULL,
    mau_userid integer NOT NULL,
    mag_groupid integer NOT NULL,
    receipt bytea NOT NULL,
    filename character varying(255) NOT NULL,
    mediatype character varying(255) NOT NULL
);
CREATE TABLE impmoneyflows (
    impmoneyflowid bigserial NOT NULL,
    externalid character varying(10) NOT NULL,
    mcs_capitalsourceid integer NOT NULL,
    bookingdate date NOT NULL,
    invoicedate date NOT NULL,
    name character varying(100) NOT NULL,
    accountnumber character varying(34) NOT NULL,
    bankcode character varying(11) NOT NULL,
    comment character varying(512) DEFAULT NULL::character varying,
    amount numeric(8,2) NOT NULL,
    status smallint DEFAULT '0'::smallint NOT NULL
);
CREATE TABLE impmonthlysettlements (
    impmonthlysettlementid bigserial NOT NULL,
    externalid character varying(10) NOT NULL,
    mcs_capitalsourceid integer NOT NULL,
    "month" smallint NOT NULL,
    "year" smallint NOT NULL,
    amount numeric(8,2) NOT NULL
);
CREATE TABLE moneyflowreceipts (
    moneyflowreceiptid bigserial NOT NULL,
    mmf_moneyflowid integer NOT NULL,
    receipt bytea NOT NULL,
    receipt_type smallint DEFAULT '1'::smallint NOT NULL
);
CREATE TABLE moneyflows (
    moneyflowid bigserial NOT NULL,
    mau_userid integer NOT NULL,
    mag_groupid integer NOT NULL,
    bookingdate date DEFAULT '1970-01-01'::date NOT NULL,
    invoicedate date DEFAULT '1970-01-01'::date NOT NULL,
    amount numeric(8,2) DEFAULT 0.00 NOT NULL,
    mcs_capitalsourceid integer NOT NULL,
    mcp_contractpartnerid integer NOT NULL,
    comment character varying(100) DEFAULT ''::character varying NOT NULL,
    mpa_postingaccountid integer NOT NULL,
    private smallint DEFAULT '0'::smallint NOT NULL
);
CREATE TABLE moneyflowsplitentries (
    moneyflowsplitentryid bigserial NOT NULL,
    mmf_moneyflowid integer NOT NULL,
    amount numeric(8,2) NOT NULL,
    comment character varying(100) NOT NULL,
    mpa_postingaccountid integer NOT NULL
);
CREATE TABLE monthlysettlements (
    monthlysettlementid bigserial NOT NULL,
    mau_userid integer NOT NULL,
    mag_groupid integer NOT NULL,
    mcs_capitalsourceid integer NOT NULL,
    "month" smallint NOT NULL,
    "year" smallint NOT NULL,
    amount numeric(8,2) NOT NULL
);
CREATE TABLE postingaccounts (
    postingaccountid bigserial NOT NULL,
    postingaccountname character varying(60) NOT NULL
);
CREATE TABLE predefmoneyflows (
    predefmoneyflowid bigserial NOT NULL,
    mau_userid integer NOT NULL,
    amount numeric(8,2) DEFAULT 0.00 NOT NULL,
    mcs_capitalsourceid integer NOT NULL,
    mcp_contractpartnerid integer NOT NULL,
    comment character varying(100) DEFAULT ''::character varying NOT NULL,
    createdate date DEFAULT '1970-01-01'::date NOT NULL,
    once_a_month smallint DEFAULT '0'::smallint NOT NULL,
    last_used date,
    mpa_postingaccountid integer NOT NULL
);
CREATE TABLE settings (
    mau_userid integer NOT NULL,
    name character varying(50) DEFAULT ''::character varying NOT NULL,
    "value" character varying(2048) DEFAULT NULL::character varying
);
CREATE VIEW vw_capitalsources  AS
 SELECT mcs.mau_userid,
    mar.mau_userid AS mar_mau_userid,
    mcs.mag_groupid,
    mcs.capitalsourceid,
    mcs.type,
    mcs.state,
    mcs.accountnumber,
    mcs.bankcode,
    mcs.comment,
    mcs.validtil,
    mcs.validfrom,
    mcs.att_group_use,
    mcs.import_allowed,
    mar.validfrom AS maf_validfrom,
    mar.validtil AS maf_validtil
   FROM capitalsources mcs,
    access_relation mar
  WHERE (mcs.mag_groupid = mar.mag_groupid);
CREATE VIEW vw_contractpartners  AS
 SELECT mcp.mau_userid,
    mar.mau_userid AS mar_mau_userid,
    mcp.mag_groupid,
    mcp.contractpartnerid,
    mcp.name,
    mcp.street,
    mcp.postcode,
    mcp.town,
    mcp.country,
    mcp.validfrom,
    mcp.validtil,
    mcp.mmf_comment,
    mcp.mpa_postingaccountid,
    mar.validfrom AS maf_validfrom,
    mar.validtil AS maf_validtil
   FROM contractpartners mcp,
    access_relation mar
  WHERE (mcp.mag_groupid = mar.mag_groupid);
CREATE VIEW vw_etf  AS
 SELECT met.mau_userid,
    mar.mau_userid AS mar_mau_userid,
    met.mag_groupid,
    met.etfid,
    met.isin,
    met.name,
    met.wkn,
    met.ticker,
    met.chart_url,
    met.trans_cost_abs,
    met.trans_cost_rel,
    met.trans_cost_max,
    met.part_tax_exempt,
    mar.validfrom AS maf_validfrom,
    mar.validtil AS maf_validtil
   FROM etf met,
    access_relation mar
  WHERE (met.mag_groupid = mar.mag_groupid);
CREATE VIEW vw_moneyflows  AS
 SELECT mmf.mau_userid,
    mar.mau_userid AS mar_mau_userid,
    mmf.mag_groupid,
    mmf.moneyflowid,
    mmf.bookingdate,
    mmf.invoicedate,
    mmf.amount,
    mmf.mcs_capitalsourceid,
    mmf.mcp_contractpartnerid,
    mmf.comment,
    mmf.mpa_postingaccountid,
    mmf.private
   FROM moneyflows mmf,
    access_relation mar
  WHERE ((mmf.bookingdate >= mar.validfrom) AND (mmf.bookingdate <= mar.validtil) AND (mmf.mag_groupid = mar.mag_groupid));
CREATE VIEW vw_monthlysettlements  AS
 SELECT mms.mau_userid,
    mar.mau_userid AS mar_mau_userid,
    mms.mag_groupid,
    mms.monthlysettlementid,
    mms.mcs_capitalsourceid,
    mms."month",
    mms."year",
    mms.amount
   FROM monthlysettlements mms,
    access_relation mar
  WHERE (((((concat(mms."year", '-', lpad((mms."month")::text, 2, '0'::text), '-01'))::date + INTERVAL '1' month) - INTERVAL '1' day) >= mar.validfrom) AND ((((concat(mms."year", '-', lpad((mms."month")::text, 2, '0'::text), '-01'))::date + INTERVAL '1' month) - INTERVAL '1' day) <= mar.validtil) AND (mms.mag_groupid = mar.mag_groupid));
ALTER TABLE access_groups
    ADD CONSTRAINT mag_i_01 UNIQUE (name);
ALTER TABLE access_groups
    ADD CONSTRAINT mag_pk PRIMARY KEY (groupid);
ALTER TABLE access_relation
    ADD CONSTRAINT mar_pk PRIMARY KEY (mau_userid, validfrom);
ALTER TABLE access_users
    ADD CONSTRAINT mau_i_01 UNIQUE (name);
ALTER TABLE access_users
    ADD CONSTRAINT mau_pk PRIMARY KEY (userid);
ALTER TABLE contractpartneraccounts
    ADD CONSTRAINT mca_i_01 UNIQUE (accountnumber, bankcode);
ALTER TABLE contractpartneraccounts
    ADD CONSTRAINT mca_pk PRIMARY KEY (contractpartneraccountid);
ALTER TABLE cmp_data_formats
    ADD CONSTRAINT mcf_i_01 UNIQUE (name);
ALTER TABLE cmp_data_formats
    ADD CONSTRAINT mcf_pk PRIMARY KEY (formatid);
ALTER TABLE contractpartners
    ADD CONSTRAINT mcp_i_01 UNIQUE (mag_groupid, name);
ALTER TABLE contractpartners
    ADD CONSTRAINT mcp_pk PRIMARY KEY (contractpartnerid);
ALTER TABLE capitalsources
    ADD CONSTRAINT mcs_pk PRIMARY KEY (capitalsourceid);
ALTER TABLE etfflows
    ADD CONSTRAINT mef_pk PRIMARY KEY (etfflowid);
ALTER TABLE etfpreliminarylumpsum
    ADD CONSTRAINT mep_i_01 UNIQUE (met_etfid, "year");
ALTER TABLE etfpreliminarylumpsum
    ADD CONSTRAINT mep_pk PRIMARY KEY (etfpreliminarylumpsumid);
ALTER TABLE etf
    ADD CONSTRAINT met_pk PRIMARY KEY (etfid);
ALTER TABLE etfvalues
    ADD CONSTRAINT mev_pk PRIMARY KEY (isin, date);
ALTER TABLE impbalance
    ADD CONSTRAINT mib_pk PRIMARY KEY (mcs_capitalsourceid);
ALTER TABLE imp_data
    ADD CONSTRAINT mid_pk PRIMARY KEY (dataid);
ALTER TABLE impmoneyflows
    ADD CONSTRAINT mim_i_01 UNIQUE (externalid);
ALTER TABLE impmoneyflows
    ADD CONSTRAINT mim_pk PRIMARY KEY (impmoneyflowid);
ALTER TABLE imp_mapping_partner
    ADD CONSTRAINT mip_pk PRIMARY KEY (partner_from);
ALTER TABLE impmoneyflowreceipts
    ADD CONSTRAINT mir_pk PRIMARY KEY (impmoneyflowreceiptid);
ALTER TABLE imp_mapping_source
    ADD CONSTRAINT mis_pk PRIMARY KEY (source_from);
ALTER TABLE impmonthlysettlements
    ADD CONSTRAINT mit_i_01 UNIQUE (externalid);
ALTER TABLE impmonthlysettlements
    ADD CONSTRAINT mit_pk PRIMARY KEY (impmonthlysettlementid);
ALTER TABLE moneyflows
    ADD CONSTRAINT mmf_pk PRIMARY KEY (moneyflowid);
ALTER TABLE monthlysettlements
    ADD CONSTRAINT mms_i_01 UNIQUE ("month", "year", mcs_capitalsourceid);
ALTER TABLE monthlysettlements
    ADD CONSTRAINT mms_pk PRIMARY KEY (monthlysettlementid);
ALTER TABLE postingaccounts
    ADD CONSTRAINT mpa_pk PRIMARY KEY (postingaccountid);
ALTER TABLE predefmoneyflows
    ADD CONSTRAINT mpm_pk PRIMARY KEY (predefmoneyflowid);
ALTER TABLE moneyflowreceipts
    ADD CONSTRAINT mrp_i_01 UNIQUE (mmf_moneyflowid);
ALTER TABLE moneyflowreceipts
    ADD CONSTRAINT mrp_pk PRIMARY KEY (moneyflowreceiptid);
ALTER TABLE moneyflowsplitentries
    ADD CONSTRAINT mse_pk PRIMARY KEY (moneyflowsplitentryid);
ALTER TABLE settings
    ADD CONSTRAINT mst_pk PRIMARY KEY (name, mau_userid);
CREATE INDEX mar_i_01 ON access_relation USING btree (mag_groupid, validfrom);
CREATE INDEX mca_mcp_pk_01 ON contractpartneraccounts USING btree (mcp_contractpartnerid);
CREATE INDEX mcp_mag_pk ON contractpartners USING btree (mag_groupid);
CREATE INDEX mcp_mau_pk ON contractpartners USING btree (mau_userid);
CREATE INDEX mcp_mpa_pk ON contractpartners USING btree (mpa_postingaccountid);
CREATE INDEX mcs_mag_pk ON capitalsources USING btree (mag_groupid);
CREATE INDEX mcs_mau_pk ON capitalsources USING btree (mau_userid);
CREATE INDEX mef_i_01 ON etfflows USING btree (met_etfid, flowdate);
CREATE INDEX met_mag_pk ON etf USING btree (mag_groupid);
CREATE INDEX met_mau_pk ON etf USING btree (mau_userid);
CREATE INDEX mim_mcs_pk ON impmoneyflows USING btree (mcs_capitalsourceid);
CREATE INDEX mir_mag_pk ON impmoneyflowreceipts USING btree (mag_groupid);
CREATE INDEX mir_mau_pk ON impmoneyflowreceipts USING btree (mau_userid);
CREATE INDEX mis_mcs_pk ON impmonthlysettlements USING btree (mcs_capitalsourceid);
CREATE INDEX mmf_i_01 ON moneyflows USING btree (bookingdate, mag_groupid, moneyflowid);
CREATE INDEX mmf_i_02 ON moneyflows USING btree (mag_groupid, bookingdate);
CREATE INDEX mmf_mag_pk ON moneyflows USING btree (mag_groupid);
CREATE INDEX mmf_mau_pk ON moneyflows USING btree (mau_userid);
CREATE INDEX mmf_mcp_pk ON moneyflows USING btree (mcp_contractpartnerid);
CREATE INDEX mmf_mcs_pk ON moneyflows USING btree (mcs_capitalsourceid);
CREATE INDEX mmf_mpa_pk ON moneyflows USING btree (mpa_postingaccountid);
CREATE INDEX mms_mag_pk ON monthlysettlements USING btree (mag_groupid);
CREATE INDEX mms_mau_pk ON monthlysettlements USING btree (mau_userid);
CREATE INDEX mms_mcs_pk ON monthlysettlements USING btree (mcs_capitalsourceid);
CREATE INDEX mpm_mau_pk ON predefmoneyflows USING btree (mau_userid);
CREATE INDEX mpm_mcp_pk ON predefmoneyflows USING btree (mcp_contractpartnerid);
CREATE INDEX mpm_mcs_pk ON predefmoneyflows USING btree (mcs_capitalsourceid);
CREATE INDEX mpm_mpa_pk ON predefmoneyflows USING btree (mpa_postingaccountid);
CREATE INDEX mse_mau_pk ON settings USING btree (mau_userid);
CREATE INDEX mse_mmf_pk ON moneyflowsplitentries USING btree (mmf_moneyflowid);
CREATE INDEX mse_mpa_pk ON moneyflowsplitentries USING btree (mpa_postingaccountid);
ALTER TABLE access_relation
    ADD CONSTRAINT mar_mag_pk FOREIGN KEY (mag_groupid) REFERENCES access_groups(groupid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE access_relation
    ADD CONSTRAINT mar_mau_pk FOREIGN KEY (mau_userid) REFERENCES access_users(userid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE contractpartneraccounts
    ADD CONSTRAINT mca_mcp_pk_01 FOREIGN KEY (mcp_contractpartnerid) REFERENCES contractpartners(contractpartnerid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE contractpartners
    ADD CONSTRAINT mcp_mag_pk FOREIGN KEY (mag_groupid) REFERENCES access_groups(groupid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE contractpartners
    ADD CONSTRAINT mcp_mau_pk FOREIGN KEY (mau_userid) REFERENCES access_users(userid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE contractpartners
    ADD CONSTRAINT mcp_mpa_pk FOREIGN KEY (mpa_postingaccountid) REFERENCES postingaccounts(postingaccountid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE capitalsources
    ADD CONSTRAINT mcs_mag_pk FOREIGN KEY (mag_groupid) REFERENCES access_groups(groupid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE capitalsources
    ADD CONSTRAINT mcs_mau_pk FOREIGN KEY (mau_userid) REFERENCES access_users(userid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE etfflows
    ADD CONSTRAINT mef_met_pk FOREIGN KEY (met_etfid) REFERENCES etf(etfid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE etfpreliminarylumpsum
    ADD CONSTRAINT mep_met_pk FOREIGN KEY (met_etfid) REFERENCES etf(etfid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE etf
    ADD CONSTRAINT met_mag_pk FOREIGN KEY (mag_groupid) REFERENCES access_groups(groupid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE etf
    ADD CONSTRAINT met_mau_pk FOREIGN KEY (mau_userid) REFERENCES access_users(userid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE impmoneyflows
    ADD CONSTRAINT mim_mcs_pk FOREIGN KEY (mcs_capitalsourceid) REFERENCES capitalsources(capitalsourceid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE impmoneyflowreceipts
    ADD CONSTRAINT mir_mag_pk FOREIGN KEY (mag_groupid) REFERENCES access_groups(groupid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE impmoneyflowreceipts
    ADD CONSTRAINT mir_mau_pk FOREIGN KEY (mau_userid) REFERENCES access_users(userid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE impmonthlysettlements
    ADD CONSTRAINT mis_mcs_pk FOREIGN KEY (mcs_capitalsourceid) REFERENCES capitalsources(capitalsourceid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE moneyflows
    ADD CONSTRAINT mmf_mag_pk FOREIGN KEY (mag_groupid) REFERENCES access_groups(groupid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE moneyflows
    ADD CONSTRAINT mmf_mau_pk FOREIGN KEY (mau_userid) REFERENCES access_users(userid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE moneyflows
    ADD CONSTRAINT mmf_mcp_pk FOREIGN KEY (mcp_contractpartnerid) REFERENCES contractpartners(contractpartnerid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE moneyflows
    ADD CONSTRAINT mmf_mcs_pk FOREIGN KEY (mcs_capitalsourceid) REFERENCES capitalsources(capitalsourceid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE moneyflows
    ADD CONSTRAINT mmf_mpa_pk FOREIGN KEY (mpa_postingaccountid) REFERENCES postingaccounts(postingaccountid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE monthlysettlements
    ADD CONSTRAINT mms_mag_pk FOREIGN KEY (mag_groupid) REFERENCES access_groups(groupid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE monthlysettlements
    ADD CONSTRAINT mms_mau_pk FOREIGN KEY (mau_userid) REFERENCES access_users(userid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE monthlysettlements
    ADD CONSTRAINT mms_mcs_pk FOREIGN KEY (mcs_capitalsourceid) REFERENCES capitalsources(capitalsourceid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE predefmoneyflows
    ADD CONSTRAINT mpm_mau_pk FOREIGN KEY (mau_userid) REFERENCES access_users(userid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE predefmoneyflows
    ADD CONSTRAINT mpm_mcp_pk FOREIGN KEY (mcp_contractpartnerid) REFERENCES contractpartners(contractpartnerid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE predefmoneyflows
    ADD CONSTRAINT mpm_mcs_pk FOREIGN KEY (mcs_capitalsourceid) REFERENCES capitalsources(capitalsourceid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE predefmoneyflows
    ADD CONSTRAINT mpm_mpa_pk FOREIGN KEY (mpa_postingaccountid) REFERENCES postingaccounts(postingaccountid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE moneyflowreceipts
    ADD CONSTRAINT mrp_mmf_pk FOREIGN KEY (mmf_moneyflowid) REFERENCES moneyflows(moneyflowid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE moneyflowsplitentries
    ADD CONSTRAINT mse_mmf_pk FOREIGN KEY (mmf_moneyflowid) REFERENCES moneyflows(moneyflowid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE moneyflowsplitentries
    ADD CONSTRAINT mse_mpa_pk FOREIGN KEY (mpa_postingaccountid) REFERENCES postingaccounts(postingaccountid) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE settings
    ADD CONSTRAINT mst_mau_pk FOREIGN KEY (mau_userid) REFERENCES access_users(userid) ON UPDATE RESTRICT ON DELETE RESTRICT;
