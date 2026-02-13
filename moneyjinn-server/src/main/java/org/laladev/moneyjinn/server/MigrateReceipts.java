package org.laladev.moneyjinn.server;

import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class MigrateReceipts {

    private static final String MYSQL_URL =
            "jdbc:mysql://db:3306/moneyflow?useLocalSessionState=true&autoReconnect=true&serverTimezone=Europe/Berlin";
    private static final String MYSQL_USER = "moneyflow_readonly";
    private static final String MYSQL_PASS = "moneyflow_readonly";

    private static final String PGSQL_URL = "jdbc:postgresql://db/postgres?currentSchema=moneyjinn";
    private static final String PGSQL_USER = "moneyjinn_app";
    private static final String PGSQL_PASS = "moneyjinn_app";

    @SneakyThrows(SQLException.class)
    public static void main(final String[] args) throws IOException {

        final Connection mysqlConn = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASS);
        final Connection pgsqlConn = DriverManager.getConnection(PGSQL_URL, PGSQL_USER, PGSQL_PASS);
        mysqlConn.setAutoCommit(false);
        pgsqlConn.setAutoCommit(false);
        final String selectStmt =
                "SELECT moneyflowreceiptid,mmf_moneyflowid,receipt,receipt_type FROM moneyflowreceipts";
        final Statement stmt = mysqlConn.createStatement();
        final ResultSet rs = stmt.executeQuery(selectStmt);

        long ins = 0;
        long overall = 0;
        final var insertStmt =
                pgsqlConn.prepareStatement("INSERT INTO moneyflowreceipts VALUES (?,?,?,?)");

        while (rs.next()) {
            final int moneyflowreceiptid = rs.getInt("moneyflowreceiptid");
            final int mmf_moneyflowid = rs.getInt("mmf_moneyflowid");
            final InputStream receipt = rs.getBinaryStream("receipt");
            final int receipt_type = rs.getInt("receipt_type");

            ins++;
            overall++;
            insertStmt.setInt(1, moneyflowreceiptid);
            insertStmt.setInt(2, mmf_moneyflowid);
            insertStmt.setBinaryStream(3, receipt);
            insertStmt.setInt(4, receipt_type);
            insertStmt.addBatch();
            System.out.println(overall + " - " + moneyflowreceiptid);
            if (ins > 50) {
                insertStmt.executeBatch();
                pgsqlConn.commit();
                System.out.println("COMMITTED");
                ins = 0;
            }
        }
        if (ins > 0) {
            insertStmt.executeBatch();
            pgsqlConn.commit();
            System.out.println("COMMITTED");
        }
    }
}
