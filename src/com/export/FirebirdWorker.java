package com.export;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class Contract {
    String outZalogID;
    String outEDROU;
    String outDogSeria;
    String outDogNum;
    String outFam;
    String outImja;
    String outOtc;
    String outOplataSrazu;
    String outSummaKOplate_VKonce;
    String outKolPeriodKOplte_VKonce;
    String outCenaPerioda;
    String outNamePeriod;
    String outSummaKredit;
    String outPinKod;
    String outDateBegin;
    String outDateEnd;
    String outMaxKolPeriodLong;
    String outNameAlgoritm;
}

public class FirebirdWorker {

    public Connection connection = null;
    public Statement statementContracts = null;

    public void Initialize(String ip, String port, String alias, String user, String password) {

        String dbURL = "jdbc:firebirdsql://" + ip + ":" + port + "/" + alias + "?encoding=WIN1251";

        try {
            Class.forName("org.firebirdsql.jdbc.FBDriver").newInstance();
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(dbURL, user, password);
            if (connection == null) {
                System.err.println("Could not connect to database");
            }

            statementContracts = connection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY,
                    ResultSet.HOLD_CURSORS_OVER_COMMIT);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Contract> getContracts(String query) {
        List<Contract> contracts = new ArrayList<Contract>();

        ResultSet contractsResult = null;
        try {
            contractsResult = statementContracts.executeQuery(query);
            System.out.println();

            while (contractsResult.next()) {
                Contract contract = new Contract();

                if (contractsResult.getObject(1) != null)
                    contract.outZalogID = contractsResult.getObject(1).toString();
                if (contractsResult.getObject(2) != null)
                    contract.outEDROU = contractsResult.getObject(2).toString();
                if (contractsResult.getObject(3) != null)
                    contract.outDogSeria = contractsResult.getObject(3).toString();
                if (contractsResult.getObject(4) != null)
                    contract.outDogNum = contractsResult.getObject(4).toString();
                if (contractsResult.getObject(5) != null)
                    contract.outFam = contractsResult.getObject(5).toString();
                if (contractsResult.getObject(6) != null)
                    contract.outImja = contractsResult.getObject(6).toString();
                if (contractsResult.getObject(7) != null)
                    contract.outOtc = contractsResult.getObject(7).toString();
                if (contractsResult.getObject(8) != null)
                    contract.outOplataSrazu = contractsResult.getObject(8).toString();
                if (contractsResult.getObject(9) != null)
                    contract.outSummaKOplate_VKonce = contractsResult.getObject(9).toString();
                if (contractsResult.getObject(10) != null)
                    contract.outKolPeriodKOplte_VKonce = contractsResult.getObject(10).toString();
                if (contractsResult.getObject(11) != null)
                    contract.outCenaPerioda = contractsResult.getObject(11).toString();
                if (contractsResult.getObject(12) != null)
                    contract.outNamePeriod = contractsResult.getObject(12).toString();
                if (contractsResult.getObject(13) != null)
                    contract.outSummaKredit = contractsResult.getObject(13).toString();
                if (contractsResult.getObject(14) != null)
                    contract.outPinKod = contractsResult.getObject(14).toString();
                if (contractsResult.getObject(15) != null)
                    contract.outDateBegin = contractsResult.getObject(15).toString();
                if (contractsResult.getObject(16) != null)
                    contract.outDateEnd = contractsResult.getObject(16).toString();
                if (contractsResult.getObject(17) != null)
                    contract.outMaxKolPeriodLong = contractsResult.getObject(17).toString();
                if (contractsResult.getObject(18) != null)
                    contract.outNameAlgoritm = contractsResult.getObject(18).toString();
                contracts.add(contract);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return contracts;
    }

    public void Release() {
        try {
            statementContracts.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

