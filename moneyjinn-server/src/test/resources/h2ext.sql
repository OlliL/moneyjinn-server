/*
 * this view will show all data from moneyflows which is visible
 * to a user. Use maf_id in your SELECT for your userid. In
 * mac_id_creator you'll find the original userid of the creator
 */
CREATE OR REPLACE VIEW vw_moneyflows (
   mac_id_creator
  ,maf_id
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
      SELECT mmf.mac_id_creator
            ,maf.id
            ,mmf.moneyflowid
            ,mmf.bookingdate
            ,mmf.invoicedate
            ,mmf.amount
            ,mmf.mcs_capitalsourceid
            ,mmf.mcp_contractpartnerid
            ,mmf.comment
            ,mmf.mpa_postingaccountid
            ,mmf.private
        FROM moneyflows       mmf
            ,access_flattened maf
       WHERE mmf.bookingdate BETWEEN maf.validfrom AND maf.validtil
         AND mmf.mac_id_accessor IN (maf.id_level_1,maf.id_level_2,maf.id_level_3,maf.id_level_4,maf.id_level_5);

/*
 * this view will show all data from monthlysettlements which is visible
 * to a user. Use maf_id in your SELECT for your userid. In
 * mac_id_creator you'll find the original userid of the creator
 */
CREATE OR REPLACE VIEW vw_monthlysettlements (
   mac_id_creator
  ,maf_id
  ,monthlysettlementid
  ,mcs_capitalsourceid
  ,month
  ,year
  ,amount
  ) AS
      SELECT mms.mac_id_creator
            ,maf.id
            ,mms.monthlysettlementid
            ,mms.mcs_capitalsourceid
            ,mms.month
            ,mms.year
            ,mms.amount
        FROM monthlysettlements mms
            ,access_flattened   maf
       WHERE TIMESTAMPADD(DAY,-1, TIMESTAMPADD(MONTH,1, CONCAT(year,'-',LPAD(month,2,'0'),'-01')))
             /*LAST_DAY(STR_TO_DATE(CONCAT(year,'-',LPAD(month,2,'0'),'-01'),GET_FORMAT(DATE,'ISO'))) */
                         BETWEEN maf.validfrom and maf.validtil
         AND mms.mac_id_accessor IN (maf.id_level_1,maf.id_level_2,maf.id_level_3,maf.id_level_4,maf.id_level_5);

/*
 * this view will show all data from contractpartners which is visible
 * to a user. Use maf_id in your SELECT for your userid. In
 * mac_id_creator you'll find the original userid of the creator
 */
CREATE OR REPLACE VIEW vw_contractpartners (
   mac_id_creator
  ,maf_id
  ,mac_id_accessor
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
      SELECT mcp.mac_id_creator
            ,maf.id
            ,mcp.mac_id_accessor
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
            ,maf.validfrom maf_validfrom
            ,maf.validtil  maf_validtil
        FROM contractpartners mcp
            ,access_flattened maf
       WHERE mcp.mac_id_accessor IN (maf.id_level_1,maf.id_level_2,maf.id_level_3,maf.id_level_4,maf.id_level_5);

/*
 * this view will show all data from capitalsources which is visible
 * to a user. Use maf_id in your SELECT for your userid. In
 * mac_id_creator you'll find the original userid of the creator
 */
CREATE OR REPLACE VIEW vw_capitalsources (
   mac_id_creator
  ,maf_id
  ,mac_id_accessor
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
      SELECT mcs.mac_id_creator
            ,maf.id
            ,mcs.mac_id_accessor
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
            ,maf.validfrom maf_validfrom
            ,maf.validtil  maf_validtil
        FROM capitalsources     mcs
            ,access_flattened   maf
       WHERE mcs.mac_id_accessor IN (maf.id_level_1,maf.id_level_2,maf.id_level_3,maf.id_level_4,maf.id_level_5);


-- FUNCTIONS

