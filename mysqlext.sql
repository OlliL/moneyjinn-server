/*
 * this view will show all data from moneyflows which is visible
 * to a user. Use mar_mau_userid in your SELECT for your userid. In
 * mau_userid you'll find the original userid of the creator
 */
CREATE OR REPLACE SQL SECURITY INVOKER VIEW vw_moneyflows (
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
CREATE OR REPLACE SQL SECURITY INVOKER VIEW vw_monthlysettlements (
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
CREATE OR REPLACE SQL SECURITY INVOKER VIEW vw_contractpartners (
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
CREATE OR REPLACE SQL SECURITY INVOKER VIEW vw_capitalsources (
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
CREATE OR REPLACE SQL SECURITY INVOKER VIEW vw_etf (
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

DELIMITER $$

-- PROCEDURES

/*
 * import data which is stored in table imp_data
 * and try to map the capitalsource and the contractpartner
 * if possible to the internal IDs (External data comes as
 * text instead of IDs)
 *
 * FIXME: long time not tested.... it probably does NOT work without modification
 *
 */
DROP PROCEDURE IF EXISTS imp_moneyflows$$
CREATE PROCEDURE imp_moneyflows (IN pi_userid   INT(10) UNSIGNED
                                ,IN pi_write    INT(1)  UNSIGNED
                                )
READS SQL DATA
BEGIN
  DECLARE l_found             BOOLEAN             DEFAULT TRUE;
  DECLARE l_insert            BOOLEAN             DEFAULT TRUE;
  DECLARE l_contractpartnerid INT(10);
  DECLARE l_capitalsourceid   INT(10);
  DECLARE l_dataid            INT(10);
  DECLARE l_date              VARCHAR(100);
  DECLARE l_amount            VARCHAR(100);
  DECLARE l_comment           VARCHAR(100);

  DECLARE c_mid CURSOR FOR
    SELECT mcp.contractpartnerid
          ,mcs.capitalsourceid
          ,mid.dataid
          ,mid.date
          ,mid.amount
          ,mid.comment
      FROM                 imp_data            mid
           LEFT OUTER JOIN imp_mapping_source  mis ON mid.source  LIKE mis.source_from
           LEFT OUTER JOIN imp_mapping_partner mip ON mid.partner = mip.partner_from
           LEFT OUTER JOIN capitalsources      mcs ON IFNULL(mis.source_to,mid.source)   = mcs.comment AND mcs.mau_userid = pi_userid
           LEFT OUTER JOIN contractpartners    mcp ON IFNULL(mip.partner_to,mid.partner) = mcp.name    AND mcp.mau_userid = pi_userid
     WHERE mid.status = 1;

  DECLARE CONTINUE HANDLER FOR NOT FOUND        SET l_found  := FALSE;
  DECLARE CONTINUE HANDLER FOR SQLSTATE '23000' SET l_insert := FALSE;

  UPDATE imp_data SET status=1 WHERE status=3;

  OPEN c_mid;
  REPEAT
    FETCH c_mid INTO l_contractpartnerid, l_capitalsourceid, l_dataid, l_date, l_amount, l_comment;
    IF l_found THEN

      START TRANSACTION;

      SET l_insert := TRUE;

      INSERT INTO moneyflows
            (mau_userid
            ,mag_groupid
            ,bookingdate
            ,invoicedate
            ,amount
            ,mcs_capitalsourceid
            ,mcp_contractpartnerid
            ,comment
            )
              VALUES
            (pi_userid
            ,pi_userid
            ,str_to_date(l_date,'%d.%m.%Y')
            ,str_to_date(l_date,'%d.%m.%Y')
            ,l_amount
            ,l_capitalsourceid
            ,l_contractpartnerid
            ,l_comment
            );

      IF pi_write = 1 AND l_insert = TRUE THEN
        COMMIT;
      ELSE
        ROLLBACK;
      END IF;
      
      START TRANSACTION;
      IF l_insert = FALSE THEN
        UPDATE imp_data 
           SET status = 3
         WHERE dataid = l_dataid;
      ELSE
        UPDATE imp_data 
           SET status = 2
         WHERE dataid = l_dataid;
      END IF;
      COMMIT;

    END IF;
  UNTIL NOT l_found END REPEAT;
  CLOSE c_mid;
END;
$$


-- TRIGGERS

/* journalling on predefmoneyflows */
DROP TRIGGER IF EXISTS mpm_trg_01$$
CREATE TRIGGER mpm_trg_01 BEFORE INSERT ON predefmoneyflows
  FOR EACH ROW BEGIN
    SET NEW.createdate = NOW();
  END;
$$

/* journalling on impbalance */
DROP TRIGGER IF EXISTS mib_trg_01$$
CREATE TRIGGER mib_trg_01 BEFORE INSERT ON impbalance
  FOR EACH ROW BEGIN
    SET NEW.changedate = NOW();
  END;
$$

DROP TRIGGER IF EXISTS mib_trg_02$$
CREATE TRIGGER mib_trg_02 BEFORE UPDATE ON impbalance
  FOR EACH ROW BEGIN
    SET NEW.changedate = NOW();
  END;
$$

DELIMITER ;


