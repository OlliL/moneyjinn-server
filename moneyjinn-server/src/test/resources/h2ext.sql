/*
 * this view will show all data from moneyflows which is visible
 * to a user. Use mar_mau_userid in your SELECT for your userid. In
 * mau_userid you'll find the original userid of the creator
 */
CREATE OR REPLACE VIEW vw_moneyflows (
   mau_userid
  ,mar_mau_userid
  ,mag_groupid
  ,moneyflowid
  ,bookingdate
  ,invoicedate
  ,amount
  ,mcs_capitalsourceid
  ,mcp_contractpartnerid
  ,comment
  ,mpa_postingaccountid
  ,private
  ) AS
      SELECT mmf.mau_userid
            ,mar.mau_userid
            ,mmf.mag_groupid
            ,mmf.moneyflowid
            ,mmf.bookingdate
            ,mmf.invoicedate
            ,mmf.amount
            ,mmf.mcs_capitalsourceid
            ,mmf.mcp_contractpartnerid
            ,mmf.comment
            ,mmf.mpa_postingaccountid
            ,mmf.private
        FROM moneyflows      mmf
            ,access_relation mar
       WHERE mmf.bookingdate BETWEEN mar.validfrom AND mar.validtil
         AND mmf.mag_groupid = mar.mag_groupid;

/*
 * this view will show all data from monthlysettlements which is visible
 * to a user. Use mar_mau_userid in your SELECT for your userid. In
 * mau_userid you'll find the original userid of the creator
 */
CREATE OR REPLACE VIEW vw_monthlysettlements (
   mau_userid
  ,mar_mau_userid
  ,mag_groupid
  ,monthlysettlementid
  ,mcs_capitalsourceid
  ,`month`
  ,`year`
  ,amount
  ) AS
      SELECT mms.mau_userid
            ,mar.mau_userid
            ,mms.mag_groupid
            ,mms.monthlysettlementid
            ,mms.mcs_capitalsourceid
            ,mms.`month`
            ,mms.`year`
            ,mms.amount
        FROM monthlysettlements mms
            ,access_relation    mar
       WHERE TIMESTAMPADD(DAY,-1, TIMESTAMPADD(MONTH,1, CONCAT(`year`,'-',LPAD(`month`,2,'0'),'-01')))
             /*LAST_DAY(STR_TO_DATE(CONCAT(year,'-',LPAD(month,2,'0'),'-01'),GET_FORMAT(DATE,'ISO'))) */
                         BETWEEN mar.validfrom and mar.validtil
         AND mms.mag_groupid = mar.mag_groupid;

/*
 * this view will show all data from contractpartners which is visible
 * to a user. Use mar_mau_userid in your SELECT for your userid. In
 * mau_userid you'll find the original userid of the creator
 */
CREATE OR REPLACE VIEW vw_contractpartners (
   mau_userid
  ,mar_mau_userid
  ,mag_groupid
  ,contractpartnerid
  ,name
  ,street
  ,postcode
  ,town
  ,country
  ,validfrom
  ,validtil
  ,mmf_comment
  ,mpa_postingaccountid
  ,maf_validfrom
  ,maf_validtil
  ) AS
      SELECT mcp.mau_userid
            ,mar.mau_userid
            ,mcp.mag_groupid
            ,mcp.contractpartnerid
            ,mcp.name
            ,mcp.street
            ,mcp.postcode
            ,mcp.town
            ,mcp.country
            ,mcp.validfrom
            ,mcp.validtil
            ,mcp.mmf_comment
            ,mcp.mpa_postingaccountid
            ,mar.validfrom maf_validfrom
            ,mar.validtil  maf_validtil
        FROM contractpartners mcp
            ,access_relation  mar
       WHERE mcp.mag_groupid = mar.mag_groupid;

/*
 * this view will show all data from capitalsources which is visible
 * to a user. Use mar_mau_userid in your SELECT for your userid. In
 * mau_userid you'll find the original userid of the creator
 */
CREATE OR REPLACE VIEW vw_capitalsources (
   mau_userid
  ,mar_mau_userid
  ,mag_groupid
  ,capitalsourceid
  ,type
  ,state
  ,accountnumber
  ,bankcode
  ,comment
  ,validtil
  ,validfrom
  ,att_group_use
  ,import_allowed
  ,maf_validfrom
  ,maf_validtil
  ) AS
      SELECT mcs.mau_userid
            ,mar.mau_userid
            ,mcs.mag_groupid
            ,mcs.capitalsourceid
            ,mcs.type
            ,mcs.state
            ,mcs.accountnumber
            ,mcs.bankcode
            ,mcs.comment
            ,mcs.validtil
            ,mcs.validfrom
            ,mcs.att_group_use
            ,mcs.import_allowed
            ,mar.validfrom maf_validfrom
            ,mar.validtil  maf_validtil
        FROM capitalsources  mcs
            ,access_relation mar
       WHERE mcs.mag_groupid = mar.mag_groupid;

/*
 * this view will show all data from etf which is visible
 * to a user. Use mar_mau_userid in your SELECT for your userid. In
 * mau_userid you'll find the original userid of the creator
 */
CREATE OR REPLACE VIEW vw_etf (
   mau_userid
  ,mar_mau_userid
  ,mag_groupid
  ,etfid
  ,isin
  ,name
  ,wkn
  ,ticker
  ,chart_url
  ,trans_cost_abs
  ,trans_cost_rel
  ,trans_cost_max
  ,part_tax_exempt
  ,maf_validfrom
  ,maf_validtil
  ) AS
      SELECT met.mau_userid
            ,mar.mau_userid
            ,met.mag_groupid
            ,met.etfid
            ,met.isin
            ,met.name
            ,met.wkn
            ,met.ticker
            ,met.chart_url
            ,met.trans_cost_abs
            ,met.trans_cost_rel
            ,met.trans_cost_max
            ,met.part_tax_exempt
            ,mar.validfrom maf_validfrom
            ,mar.validtil  maf_validtil
        FROM etf             met
            ,access_relation mar
       WHERE met.mag_groupid = mar.mag_groupid;

-- FUNCTIONS

