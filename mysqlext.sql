/*
 * this view will show all data from moneyflows which is visible
 * to a user. Use maf_id in your SELECT for your userid. In
 * mac_id_creator you'll find the original userid of the creator
 */
CREATE OR REPLACE SQL SECURITY INVOKER VIEW vw_moneyflows (
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
CREATE OR REPLACE SQL SECURITY INVOKER VIEW vw_monthlysettlements (
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
CREATE OR REPLACE SQL SECURITY INVOKER VIEW vw_contractpartners (
   mac_id_creator
  ,maf_id
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
CREATE OR REPLACE SQL SECURITY INVOKER VIEW vw_capitalsources (
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
           LEFT OUTER JOIN capitalsources      mcs ON IFNULL(mis.source_to,mid.source)   = mcs.comment AND mcs.mac_id_creator = pi_userid
           LEFT OUTER JOIN contractpartners    mcp ON IFNULL(mip.partner_to,mid.partner) = mcp.name    AND mcp.mac_id_Creator = pi_userid
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
            (mac_id_creator
            ,mac_id_accessor
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


