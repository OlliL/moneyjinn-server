--
-- PostgreSQL database dump
--

\restrict EhXvAkveJZ0bc0PeOPPDZdOptn8MvaQdlmwIyD103gPyobEOaMbVWOe2z5feiaX

-- Dumped from database version 18.1
-- Dumped by pg_dump version 18.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

ALTER TABLE ONLY moneyjinn.moneyflowsplitentries DROP CONSTRAINT mse_mpa_pk;
ALTER TABLE ONLY moneyjinn.moneyflowsplitentries DROP CONSTRAINT mse_mmf_pk;
ALTER TABLE ONLY moneyjinn.settings DROP CONSTRAINT mse_mau_pk;
ALTER TABLE ONLY moneyjinn.moneyflowreceipts DROP CONSTRAINT mrp_mmf_pk;
ALTER TABLE ONLY moneyjinn.predefmoneyflows DROP CONSTRAINT mpm_mpa_pk;
ALTER TABLE ONLY moneyjinn.predefmoneyflows DROP CONSTRAINT mpm_mcs_pk;
ALTER TABLE ONLY moneyjinn.predefmoneyflows DROP CONSTRAINT mpm_mcp_pk;
ALTER TABLE ONLY moneyjinn.predefmoneyflows DROP CONSTRAINT mpm_mau_pk;
ALTER TABLE ONLY moneyjinn.monthlysettlements DROP CONSTRAINT mms_mcs_pk;
ALTER TABLE ONLY moneyjinn.monthlysettlements DROP CONSTRAINT mms_mau_pk;
ALTER TABLE ONLY moneyjinn.monthlysettlements DROP CONSTRAINT mms_mag_pk;
ALTER TABLE ONLY moneyjinn.moneyflows DROP CONSTRAINT mmf_mpa_pk;
ALTER TABLE ONLY moneyjinn.moneyflows DROP CONSTRAINT mmf_mcs_pk;
ALTER TABLE ONLY moneyjinn.moneyflows DROP CONSTRAINT mmf_mcp_pk;
ALTER TABLE ONLY moneyjinn.moneyflows DROP CONSTRAINT mmf_mau_pk;
ALTER TABLE ONLY moneyjinn.moneyflows DROP CONSTRAINT mmf_mag_pk;
ALTER TABLE ONLY moneyjinn.impmonthlysettlements DROP CONSTRAINT mis_mcs_pk;
ALTER TABLE ONLY moneyjinn.impmoneyflowreceipts DROP CONSTRAINT mir_mau_pk;
ALTER TABLE ONLY moneyjinn.impmoneyflowreceipts DROP CONSTRAINT mir_mag_pk;
ALTER TABLE ONLY moneyjinn.impmoneyflows DROP CONSTRAINT mim_mcs_pk;
ALTER TABLE ONLY moneyjinn.etf DROP CONSTRAINT met_mau_pk;
ALTER TABLE ONLY moneyjinn.etf DROP CONSTRAINT met_mag_pk;
ALTER TABLE ONLY moneyjinn.etfpreliminarylumpsum DROP CONSTRAINT mep_met_pk;
ALTER TABLE ONLY moneyjinn.etfflows DROP CONSTRAINT mef_met_pk;
ALTER TABLE ONLY moneyjinn.capitalsources DROP CONSTRAINT mcs_mau_pk;
ALTER TABLE ONLY moneyjinn.capitalsources DROP CONSTRAINT mcs_mag_pk;
ALTER TABLE ONLY moneyjinn.contractpartners DROP CONSTRAINT mcp_mpa_pk;
ALTER TABLE ONLY moneyjinn.contractpartners DROP CONSTRAINT mcp_mau_pk;
ALTER TABLE ONLY moneyjinn.contractpartners DROP CONSTRAINT mcp_mag_pk;
ALTER TABLE ONLY moneyjinn.contractpartneraccounts DROP CONSTRAINT mca_mcp_pk_01;
ALTER TABLE ONLY moneyjinn.access_relation DROP CONSTRAINT mar_mau_pk;
ALTER TABLE ONLY moneyjinn.access_relation DROP CONSTRAINT mar_mag_pk;
DROP TRIGGER mpm_trg_01 ON moneyjinn.predefmoneyflows;
DROP TRIGGER mib_trg_01 ON moneyjinn.impbalance;
DROP TRIGGER mev_trg_01 ON moneyjinn.predefmoneyflows;
DROP INDEX moneyjinn.mse_mpa_pk;
DROP INDEX moneyjinn.mse_mmf_pk;
DROP INDEX moneyjinn.mse_mau_pk;
DROP INDEX moneyjinn.mpm_mpa_pk;
DROP INDEX moneyjinn.mpm_mcs_pk;
DROP INDEX moneyjinn.mpm_mcp_pk;
DROP INDEX moneyjinn.mpm_mau_pk;
DROP INDEX moneyjinn.mms_mcs_pk;
DROP INDEX moneyjinn.mms_mau_pk;
DROP INDEX moneyjinn.mms_mag_pk;
DROP INDEX moneyjinn.mmf_mpa_pk;
DROP INDEX moneyjinn.mmf_mcs_pk;
DROP INDEX moneyjinn.mmf_mcp_pk;
DROP INDEX moneyjinn.mmf_mau_pk;
DROP INDEX moneyjinn.mmf_mag_pk;
DROP INDEX moneyjinn.mmf_i_02;
DROP INDEX moneyjinn.mmf_i_01;
DROP INDEX moneyjinn.mis_mcs_pk;
DROP INDEX moneyjinn.mir_mau_pk;
DROP INDEX moneyjinn.mir_mag_pk;
DROP INDEX moneyjinn.mim_mcs_pk;
DROP INDEX moneyjinn.met_mau_pk;
DROP INDEX moneyjinn.met_mag_pk;
DROP INDEX moneyjinn.mef_i_01;
DROP INDEX moneyjinn.mcs_mau_pk;
DROP INDEX moneyjinn.mcs_mag_pk;
DROP INDEX moneyjinn.mcp_mpa_pk;
DROP INDEX moneyjinn.mcp_mau_pk;
DROP INDEX moneyjinn.mcp_mag_pk;
DROP INDEX moneyjinn.mca_mcp_pk_01;
DROP INDEX moneyjinn.mar_i_01;
ALTER TABLE ONLY moneyjinn.settings DROP CONSTRAINT settings_pkey;
ALTER TABLE ONLY moneyjinn.predefmoneyflows DROP CONSTRAINT predefmoneyflows_pkey;
ALTER TABLE ONLY moneyjinn.postingaccounts DROP CONSTRAINT postingaccounts_pkey;
ALTER TABLE ONLY moneyjinn.cmp_data_formats DROP CONSTRAINT name;
ALTER TABLE ONLY moneyjinn.moneyflowreceipts DROP CONSTRAINT mrp_i_01;
ALTER TABLE ONLY moneyjinn.monthlysettlements DROP CONSTRAINT monthlysettlements_pkey;
ALTER TABLE ONLY moneyjinn.moneyflowsplitentries DROP CONSTRAINT moneyflowsplitentries_pkey;
ALTER TABLE ONLY moneyjinn.moneyflows DROP CONSTRAINT moneyflows_pkey;
ALTER TABLE ONLY moneyjinn.moneyflowreceipts DROP CONSTRAINT moneyflowreceipts_pkey;
ALTER TABLE ONLY moneyjinn.monthlysettlements DROP CONSTRAINT mms_i_01;
ALTER TABLE ONLY moneyjinn.impmonthlysettlements DROP CONSTRAINT mit_i_01;
ALTER TABLE ONLY moneyjinn.imp_mapping_source DROP CONSTRAINT mis_i_01;
ALTER TABLE ONLY moneyjinn.imp_mapping_partner DROP CONSTRAINT mip_i_01;
ALTER TABLE ONLY moneyjinn.impmoneyflows DROP CONSTRAINT mim_i_01;
ALTER TABLE ONLY moneyjinn.etfpreliminarylumpsum DROP CONSTRAINT mep_i_01;
ALTER TABLE ONLY moneyjinn.contractpartners DROP CONSTRAINT mcp_i_01;
ALTER TABLE ONLY moneyjinn.contractpartneraccounts DROP CONSTRAINT mca_i_01;
ALTER TABLE ONLY moneyjinn.access_users DROP CONSTRAINT mau_i_01;
ALTER TABLE ONLY moneyjinn.access_groups DROP CONSTRAINT mag_i_01;
ALTER TABLE ONLY moneyjinn.impmonthlysettlements DROP CONSTRAINT impmonthlysettlements_pkey;
ALTER TABLE ONLY moneyjinn.impmoneyflows DROP CONSTRAINT impmoneyflows_pkey;
ALTER TABLE ONLY moneyjinn.impmoneyflowreceipts DROP CONSTRAINT impmoneyflowreceipts_pkey;
ALTER TABLE ONLY moneyjinn.impbalance DROP CONSTRAINT impbalance_pkey;
ALTER TABLE ONLY moneyjinn.imp_data DROP CONSTRAINT imp_data_pkey;
ALTER TABLE ONLY moneyjinn.etfpreliminarylumpsum DROP CONSTRAINT etfpreliminarylumpsum_pkey;
ALTER TABLE ONLY moneyjinn.etfflows DROP CONSTRAINT etfflows_pkey;
ALTER TABLE ONLY moneyjinn.etf DROP CONSTRAINT etf_pkey;
ALTER TABLE ONLY moneyjinn.etfvalues DROP CONSTRAINT etf_i_01;
ALTER TABLE ONLY moneyjinn.contractpartners DROP CONSTRAINT contractpartners_pkey;
ALTER TABLE ONLY moneyjinn.contractpartneraccounts DROP CONSTRAINT contractpartneraccounts_pkey;
ALTER TABLE ONLY moneyjinn.cmp_data_formats DROP CONSTRAINT cmp_data_formats_pkey;
ALTER TABLE ONLY moneyjinn.capitalsources DROP CONSTRAINT capitalsources_pkey;
ALTER TABLE ONLY moneyjinn.access_users DROP CONSTRAINT access_users_pkey;
ALTER TABLE ONLY moneyjinn.access_relation DROP CONSTRAINT access_relation_pkey;
ALTER TABLE ONLY moneyjinn.access_groups DROP CONSTRAINT access_groups_pkey;
ALTER TABLE moneyjinn.predefmoneyflows ALTER COLUMN predefmoneyflowid DROP DEFAULT;
ALTER TABLE moneyjinn.postingaccounts ALTER COLUMN postingaccountid DROP DEFAULT;
ALTER TABLE moneyjinn.monthlysettlements ALTER COLUMN monthlysettlementid DROP DEFAULT;
ALTER TABLE moneyjinn.moneyflowsplitentries ALTER COLUMN moneyflowsplitentryid DROP DEFAULT;
ALTER TABLE moneyjinn.moneyflows ALTER COLUMN moneyflowid DROP DEFAULT;
ALTER TABLE moneyjinn.moneyflowreceipts ALTER COLUMN moneyflowreceiptid DROP DEFAULT;
ALTER TABLE moneyjinn.impmonthlysettlements ALTER COLUMN impmonthlysettlementid DROP DEFAULT;
ALTER TABLE moneyjinn.impmoneyflows ALTER COLUMN impmoneyflowid DROP DEFAULT;
ALTER TABLE moneyjinn.impmoneyflowreceipts ALTER COLUMN impmoneyflowreceiptid DROP DEFAULT;
ALTER TABLE moneyjinn.imp_data ALTER COLUMN dataid DROP DEFAULT;
ALTER TABLE moneyjinn.etfpreliminarylumpsum ALTER COLUMN etfpreliminarylumpsumid DROP DEFAULT;
ALTER TABLE moneyjinn.etfflows ALTER COLUMN etfflowid DROP DEFAULT;
ALTER TABLE moneyjinn.etf ALTER COLUMN etfid DROP DEFAULT;
ALTER TABLE moneyjinn.contractpartners ALTER COLUMN contractpartnerid DROP DEFAULT;
ALTER TABLE moneyjinn.contractpartneraccounts ALTER COLUMN contractpartneraccountid DROP DEFAULT;
ALTER TABLE moneyjinn.cmp_data_formats ALTER COLUMN formatid DROP DEFAULT;
ALTER TABLE moneyjinn.capitalsources ALTER COLUMN capitalsourceid DROP DEFAULT;
ALTER TABLE moneyjinn.access_users ALTER COLUMN userid DROP DEFAULT;
ALTER TABLE moneyjinn.access_groups ALTER COLUMN groupid DROP DEFAULT;
DROP VIEW moneyjinn.vw_monthlysettlements;
DROP VIEW moneyjinn.vw_moneyflows;
DROP VIEW moneyjinn.vw_etf;
DROP VIEW moneyjinn.vw_contractpartners;
DROP VIEW moneyjinn.vw_capitalsources;
DROP TABLE moneyjinn.settings;
DROP SEQUENCE moneyjinn.predefmoneyflows_predefmoneyflowid_seq;
DROP TABLE moneyjinn.predefmoneyflows;
DROP SEQUENCE moneyjinn.postingaccounts_postingaccountid_seq;
DROP TABLE moneyjinn.postingaccounts;
DROP SEQUENCE moneyjinn.monthlysettlements_monthlysettlementid_seq;
DROP TABLE moneyjinn.monthlysettlements;
DROP SEQUENCE moneyjinn.moneyflowsplitentries_moneyflowsplitentryid_seq;
DROP TABLE moneyjinn.moneyflowsplitentries;
DROP SEQUENCE moneyjinn.moneyflows_moneyflowid_seq;
DROP TABLE moneyjinn.moneyflows;
DROP SEQUENCE moneyjinn.moneyflowreceipts_moneyflowreceiptid_seq;
DROP TABLE moneyjinn.moneyflowreceipts;
DROP SEQUENCE moneyjinn.impmonthlysettlements_impmonthlysettlementid_seq;
DROP TABLE moneyjinn.impmonthlysettlements;
DROP SEQUENCE moneyjinn.impmoneyflows_impmoneyflowid_seq;
DROP TABLE moneyjinn.impmoneyflows;
DROP SEQUENCE moneyjinn.impmoneyflowreceipts_impmoneyflowreceiptid_seq;
DROP TABLE moneyjinn.impmoneyflowreceipts;
DROP TABLE moneyjinn.impbalance;
DROP TABLE moneyjinn.imp_mapping_source;
DROP TABLE moneyjinn.imp_mapping_partner;
DROP SEQUENCE moneyjinn.imp_data_dataid_seq;
DROP TABLE moneyjinn.imp_data;
DROP TABLE moneyjinn.etfvalues;
DROP SEQUENCE moneyjinn.etfpreliminarylumpsum_etfpreliminarylumpsumid_seq;
DROP TABLE moneyjinn.etfpreliminarylumpsum;
DROP SEQUENCE moneyjinn.etfflows_etfflowid_seq;
DROP TABLE moneyjinn.etfflows;
DROP SEQUENCE moneyjinn.etf_etfid_seq;
DROP TABLE moneyjinn.etf;
DROP SEQUENCE moneyjinn.contractpartners_contractpartnerid_seq;
DROP TABLE moneyjinn.contractpartners;
DROP SEQUENCE moneyjinn.contractpartneraccounts_contractpartneraccountid_seq;
DROP TABLE moneyjinn.contractpartneraccounts;
DROP SEQUENCE moneyjinn.cmp_data_formats_formatid_seq;
DROP TABLE moneyjinn.cmp_data_formats;
DROP SEQUENCE moneyjinn.capitalsources_capitalsourceid_seq;
DROP TABLE moneyjinn.capitalsources;
DROP SEQUENCE moneyjinn.access_users_userid_seq;
DROP TABLE moneyjinn.access_users;
DROP TABLE moneyjinn.access_relation;
DROP SEQUENCE moneyjinn.access_groups_groupid_seq;
DROP TABLE moneyjinn.access_groups;
DROP FUNCTION moneyjinn.upd_createdate();
DROP FUNCTION moneyjinn.upd_changedate();
DROP SCHEMA moneyjinn;
--
-- Name: moneyjinn; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA moneyjinn;


ALTER SCHEMA moneyjinn OWNER TO postgres;

--
-- Name: upd_changedate(); Type: FUNCTION; Schema: moneyjinn; Owner: moneyjinn_owner
--

CREATE FUNCTION moneyjinn.upd_changedate() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
   NEW.changedate = now(); 
   RETURN NEW;
END;
$$;


ALTER FUNCTION moneyjinn.upd_changedate() OWNER TO moneyjinn_owner;

--
-- Name: upd_createdate(); Type: FUNCTION; Schema: moneyjinn; Owner: moneyjinn_owner
--

CREATE FUNCTION moneyjinn.upd_createdate() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
   NEW.createdate = now(); 
   RETURN NEW;
END;
$$;


ALTER FUNCTION moneyjinn.upd_createdate() OWNER TO moneyjinn_owner;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: access_groups; Type: TABLE; Schema: moneyjinn; Owner: postgres
--

CREATE TABLE moneyjinn.access_groups (
    groupid bigint NOT NULL,
    name character varying(20) NOT NULL
);


ALTER TABLE moneyjinn.access_groups OWNER TO postgres;

--
-- Name: access_groups_groupid_seq; Type: SEQUENCE; Schema: moneyjinn; Owner: postgres
--

CREATE SEQUENCE moneyjinn.access_groups_groupid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE moneyjinn.access_groups_groupid_seq OWNER TO postgres;

--
-- Name: access_groups_groupid_seq; Type: SEQUENCE OWNED BY; Schema: moneyjinn; Owner: postgres
--

ALTER SEQUENCE moneyjinn.access_groups_groupid_seq OWNED BY moneyjinn.access_groups.groupid;


--
-- Name: access_relation; Type: TABLE; Schema: moneyjinn; Owner: postgres
--

CREATE TABLE moneyjinn.access_relation (
    mau_userid integer NOT NULL,
    mag_groupid integer NOT NULL,
    validfrom date NOT NULL,
    validtil date NOT NULL
);


ALTER TABLE moneyjinn.access_relation OWNER TO postgres;

--
-- Name: access_users; Type: TABLE; Schema: moneyjinn; Owner: postgres
--

CREATE TABLE moneyjinn.access_users (
    userid bigint NOT NULL,
    name character varying(20) NOT NULL,
    password character varying(60) NOT NULL,
    role character varying(8) NOT NULL,
    change_password smallint NOT NULL
);


ALTER TABLE moneyjinn.access_users OWNER TO postgres;

--
-- Name: access_users_userid_seq; Type: SEQUENCE; Schema: moneyjinn; Owner: postgres
--

CREATE SEQUENCE moneyjinn.access_users_userid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE moneyjinn.access_users_userid_seq OWNER TO postgres;

--
-- Name: access_users_userid_seq; Type: SEQUENCE OWNED BY; Schema: moneyjinn; Owner: postgres
--

ALTER SEQUENCE moneyjinn.access_users_userid_seq OWNED BY moneyjinn.access_users.userid;


--
-- Name: capitalsources; Type: TABLE; Schema: moneyjinn; Owner: postgres
--

CREATE TABLE moneyjinn.capitalsources (
    capitalsourceid bigint NOT NULL,
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


ALTER TABLE moneyjinn.capitalsources OWNER TO postgres;

--
-- Name: capitalsources_capitalsourceid_seq; Type: SEQUENCE; Schema: moneyjinn; Owner: postgres
--

CREATE SEQUENCE moneyjinn.capitalsources_capitalsourceid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE moneyjinn.capitalsources_capitalsourceid_seq OWNER TO postgres;

--
-- Name: capitalsources_capitalsourceid_seq; Type: SEQUENCE OWNED BY; Schema: moneyjinn; Owner: postgres
--

ALTER SEQUENCE moneyjinn.capitalsources_capitalsourceid_seq OWNED BY moneyjinn.capitalsources.capitalsourceid;


--
-- Name: cmp_data_formats; Type: TABLE; Schema: moneyjinn; Owner: postgres
--

CREATE TABLE moneyjinn.cmp_data_formats (
    formatid bigint NOT NULL,
    name character varying(50) NOT NULL,
    start_trigger_0 character varying(30) DEFAULT NULL::character varying,
    start_trigger_1 character varying(30) DEFAULT NULL::character varying,
    start_trigger_2 character varying(30) DEFAULT NULL::character varying,
    end_trigger_0 character varying(30) DEFAULT NULL::character varying,
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


ALTER TABLE moneyjinn.cmp_data_formats OWNER TO postgres;

--
-- Name: cmp_data_formats_formatid_seq; Type: SEQUENCE; Schema: moneyjinn; Owner: postgres
--

CREATE SEQUENCE moneyjinn.cmp_data_formats_formatid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE moneyjinn.cmp_data_formats_formatid_seq OWNER TO postgres;

--
-- Name: cmp_data_formats_formatid_seq; Type: SEQUENCE OWNED BY; Schema: moneyjinn; Owner: postgres
--

ALTER SEQUENCE moneyjinn.cmp_data_formats_formatid_seq OWNED BY moneyjinn.cmp_data_formats.formatid;


--
-- Name: contractpartneraccounts; Type: TABLE; Schema: moneyjinn; Owner: postgres
--

CREATE TABLE moneyjinn.contractpartneraccounts (
    contractpartneraccountid bigint NOT NULL,
    mcp_contractpartnerid integer NOT NULL,
    accountnumber character varying(34) NOT NULL,
    bankcode character varying(11) DEFAULT NULL::character varying
);


ALTER TABLE moneyjinn.contractpartneraccounts OWNER TO postgres;

--
-- Name: contractpartneraccounts_contractpartneraccountid_seq; Type: SEQUENCE; Schema: moneyjinn; Owner: postgres
--

CREATE SEQUENCE moneyjinn.contractpartneraccounts_contractpartneraccountid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE moneyjinn.contractpartneraccounts_contractpartneraccountid_seq OWNER TO postgres;

--
-- Name: contractpartneraccounts_contractpartneraccountid_seq; Type: SEQUENCE OWNED BY; Schema: moneyjinn; Owner: postgres
--

ALTER SEQUENCE moneyjinn.contractpartneraccounts_contractpartneraccountid_seq OWNED BY moneyjinn.contractpartneraccounts.contractpartneraccountid;


--
-- Name: contractpartners; Type: TABLE; Schema: moneyjinn; Owner: postgres
--

CREATE TABLE moneyjinn.contractpartners (
    contractpartnerid bigint NOT NULL,
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


ALTER TABLE moneyjinn.contractpartners OWNER TO postgres;

--
-- Name: contractpartners_contractpartnerid_seq; Type: SEQUENCE; Schema: moneyjinn; Owner: postgres
--

CREATE SEQUENCE moneyjinn.contractpartners_contractpartnerid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE moneyjinn.contractpartners_contractpartnerid_seq OWNER TO postgres;

--
-- Name: contractpartners_contractpartnerid_seq; Type: SEQUENCE OWNED BY; Schema: moneyjinn; Owner: postgres
--

ALTER SEQUENCE moneyjinn.contractpartners_contractpartnerid_seq OWNED BY moneyjinn.contractpartners.contractpartnerid;


--
-- Name: etf; Type: TABLE; Schema: moneyjinn; Owner: postgres
--

CREATE TABLE moneyjinn.etf (
    etfid bigint NOT NULL,
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


ALTER TABLE moneyjinn.etf OWNER TO postgres;

--
-- Name: etf_etfid_seq; Type: SEQUENCE; Schema: moneyjinn; Owner: postgres
--

CREATE SEQUENCE moneyjinn.etf_etfid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE moneyjinn.etf_etfid_seq OWNER TO postgres;

--
-- Name: etf_etfid_seq; Type: SEQUENCE OWNED BY; Schema: moneyjinn; Owner: postgres
--

ALTER SEQUENCE moneyjinn.etf_etfid_seq OWNED BY moneyjinn.etf.etfid;


--
-- Name: etfflows; Type: TABLE; Schema: moneyjinn; Owner: postgres
--

CREATE TABLE moneyjinn.etfflows (
    etfflowid bigint NOT NULL,
    met_etfid integer NOT NULL,
    flowdate timestamp without time zone NOT NULL,
    amount numeric(12,6) NOT NULL,
    price numeric(11,6) NOT NULL
);


ALTER TABLE moneyjinn.etfflows OWNER TO postgres;

--
-- Name: etfflows_etfflowid_seq; Type: SEQUENCE; Schema: moneyjinn; Owner: postgres
--

CREATE SEQUENCE moneyjinn.etfflows_etfflowid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE moneyjinn.etfflows_etfflowid_seq OWNER TO postgres;

--
-- Name: etfflows_etfflowid_seq; Type: SEQUENCE OWNED BY; Schema: moneyjinn; Owner: postgres
--

ALTER SEQUENCE moneyjinn.etfflows_etfflowid_seq OWNED BY moneyjinn.etfflows.etfflowid;


--
-- Name: etfpreliminarylumpsum; Type: TABLE; Schema: moneyjinn; Owner: postgres
--

CREATE TABLE moneyjinn.etfpreliminarylumpsum (
    etfpreliminarylumpsumid bigint NOT NULL,
    met_etfid integer NOT NULL,
    year smallint NOT NULL,
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


ALTER TABLE moneyjinn.etfpreliminarylumpsum OWNER TO postgres;

--
-- Name: etfpreliminarylumpsum_etfpreliminarylumpsumid_seq; Type: SEQUENCE; Schema: moneyjinn; Owner: postgres
--

CREATE SEQUENCE moneyjinn.etfpreliminarylumpsum_etfpreliminarylumpsumid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE moneyjinn.etfpreliminarylumpsum_etfpreliminarylumpsumid_seq OWNER TO postgres;

--
-- Name: etfpreliminarylumpsum_etfpreliminarylumpsumid_seq; Type: SEQUENCE OWNED BY; Schema: moneyjinn; Owner: postgres
--

ALTER SEQUENCE moneyjinn.etfpreliminarylumpsum_etfpreliminarylumpsumid_seq OWNED BY moneyjinn.etfpreliminarylumpsum.etfpreliminarylumpsumid;


--
-- Name: etfvalues; Type: TABLE; Schema: moneyjinn; Owner: postgres
--

CREATE TABLE moneyjinn.etfvalues (
    isin character varying(30) NOT NULL,
    date date NOT NULL,
    buy_price numeric(10,3) NOT NULL,
    sell_price numeric(10,3) NOT NULL,
    changedate timestamp without time zone NOT NULL
);


ALTER TABLE moneyjinn.etfvalues OWNER TO postgres;

--
-- Name: imp_data; Type: TABLE; Schema: moneyjinn; Owner: postgres
--

CREATE TABLE moneyjinn.imp_data (
    dataid bigint NOT NULL,
    date character varying(10) NOT NULL,
    amount character varying(20) NOT NULL,
    source character varying(100) NOT NULL,
    partner character varying(100) NOT NULL,
    comment character varying(100) NOT NULL,
    status smallint DEFAULT '1'::smallint NOT NULL
);


ALTER TABLE moneyjinn.imp_data OWNER TO postgres;

--
-- Name: imp_data_dataid_seq; Type: SEQUENCE; Schema: moneyjinn; Owner: postgres
--

CREATE SEQUENCE moneyjinn.imp_data_dataid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE moneyjinn.imp_data_dataid_seq OWNER TO postgres;

--
-- Name: imp_data_dataid_seq; Type: SEQUENCE OWNED BY; Schema: moneyjinn; Owner: postgres
--

ALTER SEQUENCE moneyjinn.imp_data_dataid_seq OWNED BY moneyjinn.imp_data.dataid;


--
-- Name: imp_mapping_partner; Type: TABLE; Schema: moneyjinn; Owner: postgres
--

CREATE TABLE moneyjinn.imp_mapping_partner (
    partner_from character varying(100) NOT NULL,
    partner_to character varying(100) NOT NULL
);


ALTER TABLE moneyjinn.imp_mapping_partner OWNER TO postgres;

--
-- Name: imp_mapping_source; Type: TABLE; Schema: moneyjinn; Owner: postgres
--

CREATE TABLE moneyjinn.imp_mapping_source (
    source_from character varying(100) NOT NULL,
    source_to character varying(100) NOT NULL
);


ALTER TABLE moneyjinn.imp_mapping_source OWNER TO postgres;

--
-- Name: impbalance; Type: TABLE; Schema: moneyjinn; Owner: postgres
--

CREATE TABLE moneyjinn.impbalance (
    mcs_capitalsourceid integer NOT NULL,
    balance numeric(8,2) NOT NULL,
    changedate timestamp without time zone NOT NULL
);


ALTER TABLE moneyjinn.impbalance OWNER TO postgres;

--
-- Name: impmoneyflowreceipts; Type: TABLE; Schema: moneyjinn; Owner: postgres
--

CREATE TABLE moneyjinn.impmoneyflowreceipts (
    impmoneyflowreceiptid bigint NOT NULL,
    mau_userid integer NOT NULL,
    mag_groupid integer NOT NULL,
    receipt bytea NOT NULL,
    filename character varying(255) NOT NULL,
    mediatype character varying(255) NOT NULL
);


ALTER TABLE moneyjinn.impmoneyflowreceipts OWNER TO postgres;

--
-- Name: impmoneyflowreceipts_impmoneyflowreceiptid_seq; Type: SEQUENCE; Schema: moneyjinn; Owner: postgres
--

CREATE SEQUENCE moneyjinn.impmoneyflowreceipts_impmoneyflowreceiptid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE moneyjinn.impmoneyflowreceipts_impmoneyflowreceiptid_seq OWNER TO postgres;

--
-- Name: impmoneyflowreceipts_impmoneyflowreceiptid_seq; Type: SEQUENCE OWNED BY; Schema: moneyjinn; Owner: postgres
--

ALTER SEQUENCE moneyjinn.impmoneyflowreceipts_impmoneyflowreceiptid_seq OWNED BY moneyjinn.impmoneyflowreceipts.impmoneyflowreceiptid;


--
-- Name: impmoneyflows; Type: TABLE; Schema: moneyjinn; Owner: postgres
--

CREATE TABLE moneyjinn.impmoneyflows (
    impmoneyflowid bigint NOT NULL,
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


ALTER TABLE moneyjinn.impmoneyflows OWNER TO postgres;

--
-- Name: impmoneyflows_impmoneyflowid_seq; Type: SEQUENCE; Schema: moneyjinn; Owner: postgres
--

CREATE SEQUENCE moneyjinn.impmoneyflows_impmoneyflowid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE moneyjinn.impmoneyflows_impmoneyflowid_seq OWNER TO postgres;

--
-- Name: impmoneyflows_impmoneyflowid_seq; Type: SEQUENCE OWNED BY; Schema: moneyjinn; Owner: postgres
--

ALTER SEQUENCE moneyjinn.impmoneyflows_impmoneyflowid_seq OWNED BY moneyjinn.impmoneyflows.impmoneyflowid;


--
-- Name: impmonthlysettlements; Type: TABLE; Schema: moneyjinn; Owner: postgres
--

CREATE TABLE moneyjinn.impmonthlysettlements (
    impmonthlysettlementid bigint NOT NULL,
    externalid character varying(10) NOT NULL,
    mcs_capitalsourceid integer NOT NULL,
    month smallint NOT NULL,
    year smallint NOT NULL,
    amount numeric(8,2) NOT NULL
);


ALTER TABLE moneyjinn.impmonthlysettlements OWNER TO postgres;

--
-- Name: impmonthlysettlements_impmonthlysettlementid_seq; Type: SEQUENCE; Schema: moneyjinn; Owner: postgres
--

CREATE SEQUENCE moneyjinn.impmonthlysettlements_impmonthlysettlementid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE moneyjinn.impmonthlysettlements_impmonthlysettlementid_seq OWNER TO postgres;

--
-- Name: impmonthlysettlements_impmonthlysettlementid_seq; Type: SEQUENCE OWNED BY; Schema: moneyjinn; Owner: postgres
--

ALTER SEQUENCE moneyjinn.impmonthlysettlements_impmonthlysettlementid_seq OWNED BY moneyjinn.impmonthlysettlements.impmonthlysettlementid;


--
-- Name: moneyflowreceipts; Type: TABLE; Schema: moneyjinn; Owner: postgres
--

CREATE TABLE moneyjinn.moneyflowreceipts (
    moneyflowreceiptid bigint NOT NULL,
    mmf_moneyflowid integer NOT NULL,
    receipt bytea NOT NULL,
    receipt_type smallint DEFAULT '1'::smallint NOT NULL
);


ALTER TABLE moneyjinn.moneyflowreceipts OWNER TO postgres;

--
-- Name: moneyflowreceipts_moneyflowreceiptid_seq; Type: SEQUENCE; Schema: moneyjinn; Owner: postgres
--

CREATE SEQUENCE moneyjinn.moneyflowreceipts_moneyflowreceiptid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE moneyjinn.moneyflowreceipts_moneyflowreceiptid_seq OWNER TO postgres;

--
-- Name: moneyflowreceipts_moneyflowreceiptid_seq; Type: SEQUENCE OWNED BY; Schema: moneyjinn; Owner: postgres
--

ALTER SEQUENCE moneyjinn.moneyflowreceipts_moneyflowreceiptid_seq OWNED BY moneyjinn.moneyflowreceipts.moneyflowreceiptid;


--
-- Name: moneyflows; Type: TABLE; Schema: moneyjinn; Owner: postgres
--

CREATE TABLE moneyjinn.moneyflows (
    moneyflowid bigint NOT NULL,
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


ALTER TABLE moneyjinn.moneyflows OWNER TO postgres;

--
-- Name: moneyflows_moneyflowid_seq; Type: SEQUENCE; Schema: moneyjinn; Owner: postgres
--

CREATE SEQUENCE moneyjinn.moneyflows_moneyflowid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE moneyjinn.moneyflows_moneyflowid_seq OWNER TO postgres;

--
-- Name: moneyflows_moneyflowid_seq; Type: SEQUENCE OWNED BY; Schema: moneyjinn; Owner: postgres
--

ALTER SEQUENCE moneyjinn.moneyflows_moneyflowid_seq OWNED BY moneyjinn.moneyflows.moneyflowid;


--
-- Name: moneyflowsplitentries; Type: TABLE; Schema: moneyjinn; Owner: postgres
--

CREATE TABLE moneyjinn.moneyflowsplitentries (
    moneyflowsplitentryid bigint NOT NULL,
    mmf_moneyflowid integer NOT NULL,
    amount numeric(8,2) NOT NULL,
    comment character varying(100) NOT NULL,
    mpa_postingaccountid integer NOT NULL
);


ALTER TABLE moneyjinn.moneyflowsplitentries OWNER TO postgres;

--
-- Name: moneyflowsplitentries_moneyflowsplitentryid_seq; Type: SEQUENCE; Schema: moneyjinn; Owner: postgres
--

CREATE SEQUENCE moneyjinn.moneyflowsplitentries_moneyflowsplitentryid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE moneyjinn.moneyflowsplitentries_moneyflowsplitentryid_seq OWNER TO postgres;

--
-- Name: moneyflowsplitentries_moneyflowsplitentryid_seq; Type: SEQUENCE OWNED BY; Schema: moneyjinn; Owner: postgres
--

ALTER SEQUENCE moneyjinn.moneyflowsplitentries_moneyflowsplitentryid_seq OWNED BY moneyjinn.moneyflowsplitentries.moneyflowsplitentryid;


--
-- Name: monthlysettlements; Type: TABLE; Schema: moneyjinn; Owner: postgres
--

CREATE TABLE moneyjinn.monthlysettlements (
    monthlysettlementid bigint NOT NULL,
    mau_userid integer NOT NULL,
    mag_groupid integer NOT NULL,
    mcs_capitalsourceid integer NOT NULL,
    month smallint NOT NULL,
    year smallint NOT NULL,
    amount numeric(8,2) NOT NULL
);


ALTER TABLE moneyjinn.monthlysettlements OWNER TO postgres;

--
-- Name: monthlysettlements_monthlysettlementid_seq; Type: SEQUENCE; Schema: moneyjinn; Owner: postgres
--

CREATE SEQUENCE moneyjinn.monthlysettlements_monthlysettlementid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE moneyjinn.monthlysettlements_monthlysettlementid_seq OWNER TO postgres;

--
-- Name: monthlysettlements_monthlysettlementid_seq; Type: SEQUENCE OWNED BY; Schema: moneyjinn; Owner: postgres
--

ALTER SEQUENCE moneyjinn.monthlysettlements_monthlysettlementid_seq OWNED BY moneyjinn.monthlysettlements.monthlysettlementid;


--
-- Name: postingaccounts; Type: TABLE; Schema: moneyjinn; Owner: postgres
--

CREATE TABLE moneyjinn.postingaccounts (
    postingaccountid bigint NOT NULL,
    postingaccountname character varying(60) NOT NULL
);


ALTER TABLE moneyjinn.postingaccounts OWNER TO postgres;

--
-- Name: postingaccounts_postingaccountid_seq; Type: SEQUENCE; Schema: moneyjinn; Owner: postgres
--

CREATE SEQUENCE moneyjinn.postingaccounts_postingaccountid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE moneyjinn.postingaccounts_postingaccountid_seq OWNER TO postgres;

--
-- Name: postingaccounts_postingaccountid_seq; Type: SEQUENCE OWNED BY; Schema: moneyjinn; Owner: postgres
--

ALTER SEQUENCE moneyjinn.postingaccounts_postingaccountid_seq OWNED BY moneyjinn.postingaccounts.postingaccountid;


--
-- Name: predefmoneyflows; Type: TABLE; Schema: moneyjinn; Owner: postgres
--

CREATE TABLE moneyjinn.predefmoneyflows (
    predefmoneyflowid bigint NOT NULL,
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


ALTER TABLE moneyjinn.predefmoneyflows OWNER TO postgres;

--
-- Name: predefmoneyflows_predefmoneyflowid_seq; Type: SEQUENCE; Schema: moneyjinn; Owner: postgres
--

CREATE SEQUENCE moneyjinn.predefmoneyflows_predefmoneyflowid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE moneyjinn.predefmoneyflows_predefmoneyflowid_seq OWNER TO postgres;

--
-- Name: predefmoneyflows_predefmoneyflowid_seq; Type: SEQUENCE OWNED BY; Schema: moneyjinn; Owner: postgres
--

ALTER SEQUENCE moneyjinn.predefmoneyflows_predefmoneyflowid_seq OWNED BY moneyjinn.predefmoneyflows.predefmoneyflowid;


--
-- Name: settings; Type: TABLE; Schema: moneyjinn; Owner: postgres
--

CREATE TABLE moneyjinn.settings (
    mau_userid integer NOT NULL,
    name character varying(50) DEFAULT ''::character varying NOT NULL,
    value character varying(2048) DEFAULT NULL::character varying
);


ALTER TABLE moneyjinn.settings OWNER TO postgres;

--
-- Name: vw_capitalsources; Type: VIEW; Schema: moneyjinn; Owner: postgres
--

CREATE VIEW moneyjinn.vw_capitalsources WITH (security_invoker='true') AS
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
   FROM moneyjinn.capitalsources mcs,
    moneyjinn.access_relation mar
  WHERE (mcs.mag_groupid = mar.mag_groupid);


ALTER VIEW moneyjinn.vw_capitalsources OWNER TO postgres;

--
-- Name: vw_contractpartners; Type: VIEW; Schema: moneyjinn; Owner: postgres
--

CREATE VIEW moneyjinn.vw_contractpartners WITH (security_invoker='true') AS
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
   FROM moneyjinn.contractpartners mcp,
    moneyjinn.access_relation mar
  WHERE (mcp.mag_groupid = mar.mag_groupid);


ALTER VIEW moneyjinn.vw_contractpartners OWNER TO postgres;

--
-- Name: vw_etf; Type: VIEW; Schema: moneyjinn; Owner: postgres
--

CREATE VIEW moneyjinn.vw_etf WITH (security_invoker='true') AS
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
   FROM moneyjinn.etf met,
    moneyjinn.access_relation mar
  WHERE (met.mag_groupid = mar.mag_groupid);


ALTER VIEW moneyjinn.vw_etf OWNER TO postgres;

--
-- Name: vw_moneyflows; Type: VIEW; Schema: moneyjinn; Owner: postgres
--

CREATE VIEW moneyjinn.vw_moneyflows WITH (security_invoker='true') AS
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
   FROM moneyjinn.moneyflows mmf,
    moneyjinn.access_relation mar
  WHERE (((mmf.bookingdate >= mar.validfrom) AND (mmf.bookingdate <= mar.validtil)) AND (mmf.mag_groupid = mar.mag_groupid));


ALTER VIEW moneyjinn.vw_moneyflows OWNER TO postgres;

--
-- Name: vw_monthlysettlements; Type: VIEW; Schema: moneyjinn; Owner: postgres
--

CREATE VIEW moneyjinn.vw_monthlysettlements WITH (security_invoker='true') AS
 SELECT mms.mau_userid,
    mar.mau_userid AS mar_mau_userid,
    mms.mag_groupid,
    mms.monthlysettlementid,
    mms.mcs_capitalsourceid,
    mms.month,
    mms.year,
    mms.amount
   FROM moneyjinn.monthlysettlements mms,
    moneyjinn.access_relation mar
  WHERE ((((((concat(mms.year, '-', lpad((mms.month)::text, 2, '0'::text), '-01'))::date + '1 mon'::interval) - '1 day'::interval) >= mar.validfrom) AND ((((concat(mms.year, '-', lpad((mms.month)::text, 2, '0'::text), '-01'))::date + '1 mon'::interval) - '1 day'::interval) <= mar.validtil)) AND (mms.mag_groupid = mar.mag_groupid));


ALTER VIEW moneyjinn.vw_monthlysettlements OWNER TO postgres;

--
-- Name: access_groups groupid; Type: DEFAULT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.access_groups ALTER COLUMN groupid SET DEFAULT nextval('moneyjinn.access_groups_groupid_seq'::regclass);


--
-- Name: access_users userid; Type: DEFAULT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.access_users ALTER COLUMN userid SET DEFAULT nextval('moneyjinn.access_users_userid_seq'::regclass);


--
-- Name: capitalsources capitalsourceid; Type: DEFAULT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.capitalsources ALTER COLUMN capitalsourceid SET DEFAULT nextval('moneyjinn.capitalsources_capitalsourceid_seq'::regclass);


--
-- Name: cmp_data_formats formatid; Type: DEFAULT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.cmp_data_formats ALTER COLUMN formatid SET DEFAULT nextval('moneyjinn.cmp_data_formats_formatid_seq'::regclass);


--
-- Name: contractpartneraccounts contractpartneraccountid; Type: DEFAULT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.contractpartneraccounts ALTER COLUMN contractpartneraccountid SET DEFAULT nextval('moneyjinn.contractpartneraccounts_contractpartneraccountid_seq'::regclass);


--
-- Name: contractpartners contractpartnerid; Type: DEFAULT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.contractpartners ALTER COLUMN contractpartnerid SET DEFAULT nextval('moneyjinn.contractpartners_contractpartnerid_seq'::regclass);


--
-- Name: etf etfid; Type: DEFAULT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.etf ALTER COLUMN etfid SET DEFAULT nextval('moneyjinn.etf_etfid_seq'::regclass);


--
-- Name: etfflows etfflowid; Type: DEFAULT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.etfflows ALTER COLUMN etfflowid SET DEFAULT nextval('moneyjinn.etfflows_etfflowid_seq'::regclass);


--
-- Name: etfpreliminarylumpsum etfpreliminarylumpsumid; Type: DEFAULT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.etfpreliminarylumpsum ALTER COLUMN etfpreliminarylumpsumid SET DEFAULT nextval('moneyjinn.etfpreliminarylumpsum_etfpreliminarylumpsumid_seq'::regclass);


--
-- Name: imp_data dataid; Type: DEFAULT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.imp_data ALTER COLUMN dataid SET DEFAULT nextval('moneyjinn.imp_data_dataid_seq'::regclass);


--
-- Name: impmoneyflowreceipts impmoneyflowreceiptid; Type: DEFAULT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.impmoneyflowreceipts ALTER COLUMN impmoneyflowreceiptid SET DEFAULT nextval('moneyjinn.impmoneyflowreceipts_impmoneyflowreceiptid_seq'::regclass);


--
-- Name: impmoneyflows impmoneyflowid; Type: DEFAULT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.impmoneyflows ALTER COLUMN impmoneyflowid SET DEFAULT nextval('moneyjinn.impmoneyflows_impmoneyflowid_seq'::regclass);


--
-- Name: impmonthlysettlements impmonthlysettlementid; Type: DEFAULT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.impmonthlysettlements ALTER COLUMN impmonthlysettlementid SET DEFAULT nextval('moneyjinn.impmonthlysettlements_impmonthlysettlementid_seq'::regclass);


--
-- Name: moneyflowreceipts moneyflowreceiptid; Type: DEFAULT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.moneyflowreceipts ALTER COLUMN moneyflowreceiptid SET DEFAULT nextval('moneyjinn.moneyflowreceipts_moneyflowreceiptid_seq'::regclass);


--
-- Name: moneyflows moneyflowid; Type: DEFAULT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.moneyflows ALTER COLUMN moneyflowid SET DEFAULT nextval('moneyjinn.moneyflows_moneyflowid_seq'::regclass);


--
-- Name: moneyflowsplitentries moneyflowsplitentryid; Type: DEFAULT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.moneyflowsplitentries ALTER COLUMN moneyflowsplitentryid SET DEFAULT nextval('moneyjinn.moneyflowsplitentries_moneyflowsplitentryid_seq'::regclass);


--
-- Name: monthlysettlements monthlysettlementid; Type: DEFAULT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.monthlysettlements ALTER COLUMN monthlysettlementid SET DEFAULT nextval('moneyjinn.monthlysettlements_monthlysettlementid_seq'::regclass);


--
-- Name: postingaccounts postingaccountid; Type: DEFAULT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.postingaccounts ALTER COLUMN postingaccountid SET DEFAULT nextval('moneyjinn.postingaccounts_postingaccountid_seq'::regclass);


--
-- Name: predefmoneyflows predefmoneyflowid; Type: DEFAULT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.predefmoneyflows ALTER COLUMN predefmoneyflowid SET DEFAULT nextval('moneyjinn.predefmoneyflows_predefmoneyflowid_seq'::regclass);


--
-- Name: access_groups access_groups_pkey; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.access_groups
    ADD CONSTRAINT access_groups_pkey PRIMARY KEY (groupid);


--
-- Name: access_relation access_relation_pkey; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.access_relation
    ADD CONSTRAINT access_relation_pkey PRIMARY KEY (mau_userid, validfrom);


--
-- Name: access_users access_users_pkey; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.access_users
    ADD CONSTRAINT access_users_pkey PRIMARY KEY (userid);


--
-- Name: capitalsources capitalsources_pkey; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.capitalsources
    ADD CONSTRAINT capitalsources_pkey PRIMARY KEY (capitalsourceid);


--
-- Name: cmp_data_formats cmp_data_formats_pkey; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.cmp_data_formats
    ADD CONSTRAINT cmp_data_formats_pkey PRIMARY KEY (formatid);


--
-- Name: contractpartneraccounts contractpartneraccounts_pkey; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.contractpartneraccounts
    ADD CONSTRAINT contractpartneraccounts_pkey PRIMARY KEY (contractpartneraccountid);


--
-- Name: contractpartners contractpartners_pkey; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.contractpartners
    ADD CONSTRAINT contractpartners_pkey PRIMARY KEY (contractpartnerid);


--
-- Name: etfvalues etf_i_01; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.etfvalues
    ADD CONSTRAINT etf_i_01 UNIQUE (isin, date);


--
-- Name: etf etf_pkey; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.etf
    ADD CONSTRAINT etf_pkey PRIMARY KEY (etfid);


--
-- Name: etfflows etfflows_pkey; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.etfflows
    ADD CONSTRAINT etfflows_pkey PRIMARY KEY (etfflowid);


--
-- Name: etfpreliminarylumpsum etfpreliminarylumpsum_pkey; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.etfpreliminarylumpsum
    ADD CONSTRAINT etfpreliminarylumpsum_pkey PRIMARY KEY (etfpreliminarylumpsumid);


--
-- Name: imp_data imp_data_pkey; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.imp_data
    ADD CONSTRAINT imp_data_pkey PRIMARY KEY (dataid);


--
-- Name: impbalance impbalance_pkey; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.impbalance
    ADD CONSTRAINT impbalance_pkey PRIMARY KEY (mcs_capitalsourceid);


--
-- Name: impmoneyflowreceipts impmoneyflowreceipts_pkey; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.impmoneyflowreceipts
    ADD CONSTRAINT impmoneyflowreceipts_pkey PRIMARY KEY (impmoneyflowreceiptid);


--
-- Name: impmoneyflows impmoneyflows_pkey; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.impmoneyflows
    ADD CONSTRAINT impmoneyflows_pkey PRIMARY KEY (impmoneyflowid);


--
-- Name: impmonthlysettlements impmonthlysettlements_pkey; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.impmonthlysettlements
    ADD CONSTRAINT impmonthlysettlements_pkey PRIMARY KEY (impmonthlysettlementid);


--
-- Name: access_groups mag_i_01; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.access_groups
    ADD CONSTRAINT mag_i_01 UNIQUE (name);


--
-- Name: access_users mau_i_01; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.access_users
    ADD CONSTRAINT mau_i_01 UNIQUE (name);


--
-- Name: contractpartneraccounts mca_i_01; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.contractpartneraccounts
    ADD CONSTRAINT mca_i_01 UNIQUE (accountnumber, bankcode);


--
-- Name: contractpartners mcp_i_01; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.contractpartners
    ADD CONSTRAINT mcp_i_01 UNIQUE (mag_groupid, name);


--
-- Name: etfpreliminarylumpsum mep_i_01; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.etfpreliminarylumpsum
    ADD CONSTRAINT mep_i_01 UNIQUE (met_etfid, year);


--
-- Name: impmoneyflows mim_i_01; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.impmoneyflows
    ADD CONSTRAINT mim_i_01 UNIQUE (externalid);


--
-- Name: imp_mapping_partner mip_i_01; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.imp_mapping_partner
    ADD CONSTRAINT mip_i_01 UNIQUE (partner_from);


--
-- Name: imp_mapping_source mis_i_01; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.imp_mapping_source
    ADD CONSTRAINT mis_i_01 UNIQUE (source_from);


--
-- Name: impmonthlysettlements mit_i_01; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.impmonthlysettlements
    ADD CONSTRAINT mit_i_01 UNIQUE (externalid);


--
-- Name: monthlysettlements mms_i_01; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.monthlysettlements
    ADD CONSTRAINT mms_i_01 UNIQUE (month, year, mcs_capitalsourceid);


--
-- Name: moneyflowreceipts moneyflowreceipts_pkey; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.moneyflowreceipts
    ADD CONSTRAINT moneyflowreceipts_pkey PRIMARY KEY (moneyflowreceiptid);


--
-- Name: moneyflows moneyflows_pkey; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.moneyflows
    ADD CONSTRAINT moneyflows_pkey PRIMARY KEY (moneyflowid);


--
-- Name: moneyflowsplitentries moneyflowsplitentries_pkey; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.moneyflowsplitentries
    ADD CONSTRAINT moneyflowsplitentries_pkey PRIMARY KEY (moneyflowsplitentryid);


--
-- Name: monthlysettlements monthlysettlements_pkey; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.monthlysettlements
    ADD CONSTRAINT monthlysettlements_pkey PRIMARY KEY (monthlysettlementid);


--
-- Name: moneyflowreceipts mrp_i_01; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.moneyflowreceipts
    ADD CONSTRAINT mrp_i_01 UNIQUE (mmf_moneyflowid);


--
-- Name: cmp_data_formats name; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.cmp_data_formats
    ADD CONSTRAINT name UNIQUE (name);


--
-- Name: postingaccounts postingaccounts_pkey; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.postingaccounts
    ADD CONSTRAINT postingaccounts_pkey PRIMARY KEY (postingaccountid);


--
-- Name: predefmoneyflows predefmoneyflows_pkey; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.predefmoneyflows
    ADD CONSTRAINT predefmoneyflows_pkey PRIMARY KEY (predefmoneyflowid);


--
-- Name: settings settings_pkey; Type: CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.settings
    ADD CONSTRAINT settings_pkey PRIMARY KEY (name, mau_userid);


--
-- Name: mar_i_01; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mar_i_01 ON moneyjinn.access_relation USING btree (mag_groupid, validfrom);


--
-- Name: mca_mcp_pk_01; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mca_mcp_pk_01 ON moneyjinn.contractpartneraccounts USING btree (mcp_contractpartnerid);


--
-- Name: mcp_mag_pk; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mcp_mag_pk ON moneyjinn.contractpartners USING btree (mag_groupid);


--
-- Name: mcp_mau_pk; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mcp_mau_pk ON moneyjinn.contractpartners USING btree (mau_userid);


--
-- Name: mcp_mpa_pk; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mcp_mpa_pk ON moneyjinn.contractpartners USING btree (mpa_postingaccountid);


--
-- Name: mcs_mag_pk; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mcs_mag_pk ON moneyjinn.capitalsources USING btree (mag_groupid);


--
-- Name: mcs_mau_pk; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mcs_mau_pk ON moneyjinn.capitalsources USING btree (mau_userid);


--
-- Name: mef_i_01; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mef_i_01 ON moneyjinn.etfflows USING btree (met_etfid, flowdate);


--
-- Name: met_mag_pk; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX met_mag_pk ON moneyjinn.etf USING btree (mag_groupid);


--
-- Name: met_mau_pk; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX met_mau_pk ON moneyjinn.etf USING btree (mau_userid);


--
-- Name: mim_mcs_pk; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mim_mcs_pk ON moneyjinn.impmoneyflows USING btree (mcs_capitalsourceid);


--
-- Name: mir_mag_pk; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mir_mag_pk ON moneyjinn.impmoneyflowreceipts USING btree (mag_groupid);


--
-- Name: mir_mau_pk; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mir_mau_pk ON moneyjinn.impmoneyflowreceipts USING btree (mau_userid);


--
-- Name: mis_mcs_pk; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mis_mcs_pk ON moneyjinn.impmonthlysettlements USING btree (mcs_capitalsourceid);


--
-- Name: mmf_i_01; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mmf_i_01 ON moneyjinn.moneyflows USING btree (bookingdate, mag_groupid, moneyflowid);


--
-- Name: mmf_i_02; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mmf_i_02 ON moneyjinn.moneyflows USING btree (mag_groupid, bookingdate);


--
-- Name: mmf_mag_pk; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mmf_mag_pk ON moneyjinn.moneyflows USING btree (mag_groupid);


--
-- Name: mmf_mau_pk; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mmf_mau_pk ON moneyjinn.moneyflows USING btree (mau_userid);


--
-- Name: mmf_mcp_pk; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mmf_mcp_pk ON moneyjinn.moneyflows USING btree (mcp_contractpartnerid);


--
-- Name: mmf_mcs_pk; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mmf_mcs_pk ON moneyjinn.moneyflows USING btree (mcs_capitalsourceid);


--
-- Name: mmf_mpa_pk; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mmf_mpa_pk ON moneyjinn.moneyflows USING btree (mpa_postingaccountid);


--
-- Name: mms_mag_pk; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mms_mag_pk ON moneyjinn.monthlysettlements USING btree (mag_groupid);


--
-- Name: mms_mau_pk; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mms_mau_pk ON moneyjinn.monthlysettlements USING btree (mau_userid);


--
-- Name: mms_mcs_pk; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mms_mcs_pk ON moneyjinn.monthlysettlements USING btree (mcs_capitalsourceid);


--
-- Name: mpm_mau_pk; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mpm_mau_pk ON moneyjinn.predefmoneyflows USING btree (mau_userid);


--
-- Name: mpm_mcp_pk; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mpm_mcp_pk ON moneyjinn.predefmoneyflows USING btree (mcp_contractpartnerid);


--
-- Name: mpm_mcs_pk; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mpm_mcs_pk ON moneyjinn.predefmoneyflows USING btree (mcs_capitalsourceid);


--
-- Name: mpm_mpa_pk; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mpm_mpa_pk ON moneyjinn.predefmoneyflows USING btree (mpa_postingaccountid);


--
-- Name: mse_mau_pk; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mse_mau_pk ON moneyjinn.settings USING btree (mau_userid);


--
-- Name: mse_mmf_pk; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mse_mmf_pk ON moneyjinn.moneyflowsplitentries USING btree (mmf_moneyflowid);


--
-- Name: mse_mpa_pk; Type: INDEX; Schema: moneyjinn; Owner: postgres
--

CREATE INDEX mse_mpa_pk ON moneyjinn.moneyflowsplitentries USING btree (mpa_postingaccountid);


--
-- Name: predefmoneyflows mev_trg_01; Type: TRIGGER; Schema: moneyjinn; Owner: postgres
--

CREATE TRIGGER mev_trg_01 BEFORE UPDATE ON moneyjinn.predefmoneyflows FOR EACH ROW EXECUTE FUNCTION moneyjinn.upd_changedate();


--
-- Name: impbalance mib_trg_01; Type: TRIGGER; Schema: moneyjinn; Owner: postgres
--

CREATE TRIGGER mib_trg_01 BEFORE UPDATE ON moneyjinn.impbalance FOR EACH ROW EXECUTE FUNCTION moneyjinn.upd_changedate();


--
-- Name: predefmoneyflows mpm_trg_01; Type: TRIGGER; Schema: moneyjinn; Owner: postgres
--

CREATE TRIGGER mpm_trg_01 BEFORE UPDATE ON moneyjinn.predefmoneyflows FOR EACH ROW EXECUTE FUNCTION moneyjinn.upd_createdate();


--
-- Name: access_relation mar_mag_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.access_relation
    ADD CONSTRAINT mar_mag_pk FOREIGN KEY (mag_groupid) REFERENCES moneyjinn.access_groups(groupid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: access_relation mar_mau_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.access_relation
    ADD CONSTRAINT mar_mau_pk FOREIGN KEY (mau_userid) REFERENCES moneyjinn.access_users(userid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: contractpartneraccounts mca_mcp_pk_01; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.contractpartneraccounts
    ADD CONSTRAINT mca_mcp_pk_01 FOREIGN KEY (mcp_contractpartnerid) REFERENCES moneyjinn.contractpartners(contractpartnerid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: contractpartners mcp_mag_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.contractpartners
    ADD CONSTRAINT mcp_mag_pk FOREIGN KEY (mag_groupid) REFERENCES moneyjinn.access_groups(groupid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: contractpartners mcp_mau_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.contractpartners
    ADD CONSTRAINT mcp_mau_pk FOREIGN KEY (mau_userid) REFERENCES moneyjinn.access_users(userid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: contractpartners mcp_mpa_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.contractpartners
    ADD CONSTRAINT mcp_mpa_pk FOREIGN KEY (mpa_postingaccountid) REFERENCES moneyjinn.postingaccounts(postingaccountid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: capitalsources mcs_mag_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.capitalsources
    ADD CONSTRAINT mcs_mag_pk FOREIGN KEY (mag_groupid) REFERENCES moneyjinn.access_groups(groupid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: capitalsources mcs_mau_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.capitalsources
    ADD CONSTRAINT mcs_mau_pk FOREIGN KEY (mau_userid) REFERENCES moneyjinn.access_users(userid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: etfflows mef_met_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.etfflows
    ADD CONSTRAINT mef_met_pk FOREIGN KEY (met_etfid) REFERENCES moneyjinn.etf(etfid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: etfpreliminarylumpsum mep_met_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.etfpreliminarylumpsum
    ADD CONSTRAINT mep_met_pk FOREIGN KEY (met_etfid) REFERENCES moneyjinn.etf(etfid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: etf met_mag_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.etf
    ADD CONSTRAINT met_mag_pk FOREIGN KEY (mag_groupid) REFERENCES moneyjinn.access_groups(groupid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: etf met_mau_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.etf
    ADD CONSTRAINT met_mau_pk FOREIGN KEY (mau_userid) REFERENCES moneyjinn.access_users(userid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: impmoneyflows mim_mcs_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.impmoneyflows
    ADD CONSTRAINT mim_mcs_pk FOREIGN KEY (mcs_capitalsourceid) REFERENCES moneyjinn.capitalsources(capitalsourceid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: impmoneyflowreceipts mir_mag_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.impmoneyflowreceipts
    ADD CONSTRAINT mir_mag_pk FOREIGN KEY (mag_groupid) REFERENCES moneyjinn.access_groups(groupid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: impmoneyflowreceipts mir_mau_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.impmoneyflowreceipts
    ADD CONSTRAINT mir_mau_pk FOREIGN KEY (mau_userid) REFERENCES moneyjinn.access_users(userid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: impmonthlysettlements mis_mcs_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.impmonthlysettlements
    ADD CONSTRAINT mis_mcs_pk FOREIGN KEY (mcs_capitalsourceid) REFERENCES moneyjinn.capitalsources(capitalsourceid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: moneyflows mmf_mag_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.moneyflows
    ADD CONSTRAINT mmf_mag_pk FOREIGN KEY (mag_groupid) REFERENCES moneyjinn.access_groups(groupid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: moneyflows mmf_mau_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.moneyflows
    ADD CONSTRAINT mmf_mau_pk FOREIGN KEY (mau_userid) REFERENCES moneyjinn.access_users(userid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: moneyflows mmf_mcp_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.moneyflows
    ADD CONSTRAINT mmf_mcp_pk FOREIGN KEY (mcp_contractpartnerid) REFERENCES moneyjinn.contractpartners(contractpartnerid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: moneyflows mmf_mcs_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.moneyflows
    ADD CONSTRAINT mmf_mcs_pk FOREIGN KEY (mcs_capitalsourceid) REFERENCES moneyjinn.capitalsources(capitalsourceid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: moneyflows mmf_mpa_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.moneyflows
    ADD CONSTRAINT mmf_mpa_pk FOREIGN KEY (mpa_postingaccountid) REFERENCES moneyjinn.postingaccounts(postingaccountid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: monthlysettlements mms_mag_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.monthlysettlements
    ADD CONSTRAINT mms_mag_pk FOREIGN KEY (mag_groupid) REFERENCES moneyjinn.access_groups(groupid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: monthlysettlements mms_mau_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.monthlysettlements
    ADD CONSTRAINT mms_mau_pk FOREIGN KEY (mau_userid) REFERENCES moneyjinn.access_users(userid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: monthlysettlements mms_mcs_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.monthlysettlements
    ADD CONSTRAINT mms_mcs_pk FOREIGN KEY (mcs_capitalsourceid) REFERENCES moneyjinn.capitalsources(capitalsourceid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: predefmoneyflows mpm_mau_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.predefmoneyflows
    ADD CONSTRAINT mpm_mau_pk FOREIGN KEY (mau_userid) REFERENCES moneyjinn.access_users(userid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: predefmoneyflows mpm_mcp_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.predefmoneyflows
    ADD CONSTRAINT mpm_mcp_pk FOREIGN KEY (mcp_contractpartnerid) REFERENCES moneyjinn.contractpartners(contractpartnerid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: predefmoneyflows mpm_mcs_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.predefmoneyflows
    ADD CONSTRAINT mpm_mcs_pk FOREIGN KEY (mcs_capitalsourceid) REFERENCES moneyjinn.capitalsources(capitalsourceid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: predefmoneyflows mpm_mpa_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.predefmoneyflows
    ADD CONSTRAINT mpm_mpa_pk FOREIGN KEY (mpa_postingaccountid) REFERENCES moneyjinn.postingaccounts(postingaccountid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: moneyflowreceipts mrp_mmf_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.moneyflowreceipts
    ADD CONSTRAINT mrp_mmf_pk FOREIGN KEY (mmf_moneyflowid) REFERENCES moneyjinn.moneyflows(moneyflowid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: settings mse_mau_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.settings
    ADD CONSTRAINT mse_mau_pk FOREIGN KEY (mau_userid) REFERENCES moneyjinn.access_users(userid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: moneyflowsplitentries mse_mmf_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.moneyflowsplitentries
    ADD CONSTRAINT mse_mmf_pk FOREIGN KEY (mmf_moneyflowid) REFERENCES moneyjinn.moneyflows(moneyflowid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: moneyflowsplitentries mse_mpa_pk; Type: FK CONSTRAINT; Schema: moneyjinn; Owner: postgres
--

ALTER TABLE ONLY moneyjinn.moneyflowsplitentries
    ADD CONSTRAINT mse_mpa_pk FOREIGN KEY (mpa_postingaccountid) REFERENCES moneyjinn.postingaccounts(postingaccountid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: SCHEMA moneyjinn; Type: ACL; Schema: -; Owner: postgres
--

GRANT ALL ON SCHEMA moneyjinn TO moneyjinn_owner;


--
-- PostgreSQL database dump complete
--

\unrestrict EhXvAkveJZ0bc0PeOPPDZdOptn8MvaQdlmwIyD103gPyobEOaMbVWOe2z5feiaX

