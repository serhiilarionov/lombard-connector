package com.export;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class SendingData {

    static {
//        HttpURLConnection.setDefaultHostnameVerifier(
//                new javax.net.ssl.HostnameVerifier() {
//
//                    public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
//                        return true;
//                    }
//                });
    }
    public static Boolean delete(String exportUrl) {
        try {
            HttpURLConnection con = null;
            try {
                URL url = new URL(exportUrl + "/delete/contracts");
                con = (HttpURLConnection) url.openConnection();
            } catch (Exception e){
                e.printStackTrace();
                if(con != null)
                    con.disconnect();
                throw e;
            }

            if (con == null) {
                throw new Exception ("Failed to open HTTPS Conenction");
            }

            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoInput(true);
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.flush();
            writer.close();
            os.close();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "UTF-8"));
            PrintStream out = new PrintStream(System.out, true, "UTF-8");
            String line = null;
            while ((line = in.readLine()) != null) {
                out.println(line);
            }
            in.close();
        } catch (Exception t) {
            t.printStackTrace();
            return false;
        }
        return true;
    }

    public static List<Contract> sending(String exportUrl, List<Contract> contracts, String certificatePassword) throws Exception {
        String keystore = "keystore.jks";
//        System.setProperty("javax.net.ssl.trustStore", keystore);
//        System.setProperty("javax.net.ssl.trustStorePassword", certificatePassword);
//        System.setProperty("javax.net.ssl.keyStore", keystore);
//        System.setProperty("javax.net.ssl.keyStorePassword", certificatePassword);

        JSONObject json = new JSONObject();
        JSONArray contractsJSON = new JSONArray();
        Map<String, String> contractsMap = new HashMap<String, String>();
        List<Contract> errors = new ArrayList<Contract>();
        Contract error = new Contract();

        int contractsLength = contracts.size();
        for (int i = 0; i < contractsLength; i++) {
            String outZalogID = contracts.get(i).outZalogID;
            String outEDROU = contracts.get(i).outEDROU;
            String outDogSeria = contracts.get(i).outDogSeria;
            String outDogNum = contracts.get(i).outDogNum;
            String outFam = contracts.get(i).outFam;
            String outImja = contracts.get(i).outImja;
            String outOtc = contracts.get(i).outOtc;
            String outOplataSrazu = contracts.get(i).outOplataSrazu;
            String outSummaKOplate_VKonce = contracts.get(i).outSummaKOplate_VKonce;
            String outKolPeriodKOplte_VKonce = contracts.get(i).outKolPeriodKOplte_VKonce;
            String outCenaPerioda = contracts.get(i).outCenaPerioda;
            String outNamePeriod = contracts.get(i).outNamePeriod;
            String outSummaKredit = contracts.get(i).outSummaKredit;
            String outPinKod = contracts.get(i).outPinKod;
            String outDateBegin = contracts.get(i).outDateBegin;
            String outDateEnd = contracts.get(i).outDateEnd;
            String outMaxKolPeriodLong = contracts.get(i).outMaxKolPeriodLong;
            String outNameAlgoritm = contracts.get(i).outNameAlgoritm;

            Boolean err = false;
            if(outEDROU == null) {
                error.outEDROU = "outEDROU";
                err=true;
            }
            if(outDogSeria == null) {
                error.outDogSeria = "outDogSeria";
                err=true;
            }
            if(outDogNum == null) {
                error.outDogNum = "outDogNum";
                err=true;
            }
            if(outZalogID == null) {
                error.outDogNum = "outDogNum";
                err=true;
            }
            if(outPinKod == null) {
                error.outPinKod = "outPinKod";
                err=true;
            }
            if(outOplataSrazu == null) {
                error.outOplataSrazu = "outOplataSrazu";
                err=true;
            }
            if(outFam == null && outImja == null && outOtc == null) {
                error.outFam = "fio";
                error.outImja = "fio";
                error.outOtc = "fio";
                err=true;
            }

            if(outNamePeriod == null) {
                error.outNamePeriod = "outNamePeriod";
                err=true;
            }
            if(Objects.equals(outOplataSrazu, "1")) {
                if(outCenaPerioda == null) {
                    error.outCenaPerioda = "outCenaPerioda";
                    err=true;
                }
            }
            if(Objects.equals(outOplataSrazu, "1")) {
                if(outSummaKOplate_VKonce == null) {
                    error.outSummaKOplate_VKonce = "outSummaKOplate_VKonce";
                    err=true;
                }
            }
            if(outDateBegin == null) {
                error.outDateBegin = "outDateBegin";
                err=true;
            }
            if(outDateEnd == null) {
                error.outDateEnd = "outDateEnd";
                err=true;
            }
            if(outNameAlgoritm == null) {
                error.outNameAlgoritm = "outNameAlgoritm";
                err=true;
            }
            if(outMaxKolPeriodLong == null) {
                error.outMaxKolPeriodLong = "outMaxKolPeriodLong";
                err=true;
            }
            if(err) {
                errors.add(error);
                continue;
            }

            contractsMap.put("enterpriseId", outEDROU);
            contractsMap.put("seriesOfContract", outDogSeria);
            contractsMap.put("NumberOfContract", outDogNum);
            contractsMap.put("contractId", outZalogID);
            contractsMap.put("pinCode", outPinKod);
            contractsMap.put("paymentInEndOfTerm", outOplataSrazu);
            contractsMap.put("fio", outFam + " " + outImja + " " + outOtc);
            contractsMap.put("periodName", outNamePeriod);
            contractsMap.put("periodPrice", outCenaPerioda);
            contractsMap.put("paymentAmount", outSummaKOplate_VKonce);
            contractsMap.put("numberPaymentPeriods", outKolPeriodKOplte_VKonce);
            contractsMap.put("contractBeginDate", outDateBegin);
            contractsMap.put("contractEndDate", outDateEnd);
            contractsMap.put("contractAlgorithm", outNameAlgoritm);
            contractsMap.put("limitExtension", outMaxKolPeriodLong);
            contractsJSON.put(contractsMap);

            if((i!=0 && i%20==0) || i+1==contractsLength) {
                try {
                    HttpURLConnection con = null;
                    try {
                        URL url = new URL(exportUrl+"/add/contracts");
                        con = (HttpURLConnection) url.openConnection();
                    }catch(Exception e){
                        e.printStackTrace();
                        if(con != null)
                            con.disconnect();
                        throw e;
                    }

                    if(con==null){
                        throw new Exception ("Failed to open HTTPS Conenction");
                    }
                    con.setRequestMethod("POST");
                    con.setRequestProperty("User-Agent", "Mozilla/5.0");
                    con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                    con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    con.setRequestProperty("Accept", "application/json");
                    con.setRequestProperty("Connection", "Keep-Alive");
                    con.setRequestProperty("Keep-Alive", "header");
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    OutputStream os = con.getOutputStream();

                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    json.put("contractsArray", contractsJSON);

                    writer.write(json.toString());
                    writer.flush();
                    writer.close();
                    os.close();
                    contractsMap.clear();
                    contractsJSON = new JSONArray();
                    json.remove("contractsArray");

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream(), "UTF-8"));
                    PrintStream out = new PrintStream(System.out, true, "UTF-8");
                    String line = null;
                    while ((line = in.readLine()) != null) {
                        out.println(i+1 + ": " + line);
                    }
                    in.close();

                } catch (Throwable t)
                {
                    t.printStackTrace();
                }
            }
        }
        return errors;
    }
}
