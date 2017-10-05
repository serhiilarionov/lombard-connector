package com.export;

import java.io.Console;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;

public final class MainClass {
    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            String certificate = null;
            String certificatePassword = null;
            int exitVal = 0;
            List<Contract> contractsArray;
            List<Contract> errors = null;
            if (Objects.equals(args[0], "export")) {
                Config config = new Config();
                String serverIps = config.getProperty("serverIP");
                String serverPort = config.getProperty("serverPort");
                String users = config.getProperty("users");
                String passwords = config.getProperty("passwords");
                String alias = config.getProperty("alias");
                String exportUrl = config.getProperty("exportUrl");

//                Scanner console = new Scanner(System.in);
//                while(certificatePassword==null || Objects.equals(certificatePassword, "")) {
//                    System.out.println("Введіть пароль до сертифікату.");
//                    certificatePassword = console.nextLine();
//                    if(certificatePassword == null || Objects.equals(certificatePassword, "")) {
//                        System.out.println("Ви не ввели пароль до сертифікату.");
//                    }
//                }

                StringTokenizer stringTokenizerIp = new StringTokenizer(serverIps, ", ");
                String[] serversIpArray = new String[stringTokenizerIp.countTokens()];

                StringTokenizer stringTokenizerAlias = new StringTokenizer(alias, ", ");
                String[] aliasArray = new String[stringTokenizerAlias.countTokens()];

                StringTokenizer stringTokenizerUser = new StringTokenizer(users, ", ");
                String[] usersArray = new String[stringTokenizerUser.countTokens()];

                StringTokenizer stringTokenizerPasswords = new StringTokenizer(passwords, ", ");
                String[] passwordsArray = new String[stringTokenizerPasswords.countTokens()];

                for (int i = 0; i < serversIpArray.length; i++) {
                    serversIpArray[i] = stringTokenizerIp.nextToken();
                    aliasArray[i] = stringTokenizerAlias.nextToken();
                    usersArray[i] = stringTokenizerUser.nextToken();
                    passwordsArray[i] = stringTokenizerPasswords.nextToken();
                }
                FirebirdWorker firebirdWorker = new FirebirdWorker();
                String errorsString = "";
                if(SendingData.delete(exportUrl)) {
                    for (int i = 0; i < serversIpArray.length; i++) {
                        firebirdWorker.Initialize(serversIpArray[i], serverPort, aliasArray[i], usersArray[i], passwordsArray[i]);

                        contractsArray = firebirdWorker.getContracts("SELECT \"outZalogID\", \"outEDROU\", \"outDogSeria\", \"outDogNum\", \"outFam\",\n" +
                                "    \"outImja\", \"outOtc\", \"outOplataSrazu\", \"outSummaKOplate_VKonce\",\n" +
                                "    \"outKolPeriodKOplte_VKonce\", \"outCenaPerioda\", \"outNamePeriod\",\n" +
                                "    \"outSummaKredit\", \"outPinKod\", \"outDateBegin\", \"outDateEnd\",\n" +
                                "    \"outMaxKolPeriodLong\", \"outNameAlgoritm\"\n" +
                                "FROM \"PrlmbZalogForInternet\" ;");
                        Date curTime = new Date();
                        String curStringDate = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss").format(curTime);
                        PrintStream out = new PrintStream(System.out, true, "UTF-8");
                        if (contractsArray.size() != 0) {
                            try {
                                errors = SendingData.sending(exportUrl, contractsArray, certificatePassword);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (errors.size() != 0) {
                                for (int j = 0; j < errors.size(); j++) {
                                    if (!errors.isEmpty())
                                        errorsString += errors.get(j) + "\n";
                                }
                                out.println(curStringDate + ": В базі " + serversIpArray[i] + "/" + aliasArray[i] + " знайдено " + errors.size() + " помилок\n");
                                FileWorker.write("./error.txt", errorsString);
                            } else {
                                out.println(curStringDate + ": З бази " + serversIpArray[i] + "/" + aliasArray[i] + " контракти вивантажено успішно\n");
                            }
                        } else {
                            out.println(curStringDate + ": В базі " + serversIpArray[i] + "/" + aliasArray[i] + " контрактів не знайдено\n");
                        }
                    }
                }
            }

            if (Objects.equals(args[0], "setSertificate")) {
                PrintStream out = new PrintStream(System.out, true, "UTF-8");
                PrintStream err = new PrintStream(System.err, true, "UTF-8");
                try {
                    try {
                        Path keystorePath = FileSystems.getDefault().getPath("", "keystore.jks");
                        if(keystorePath.toFile().exists())
                            Files.delete(keystorePath);
                    } catch (NoSuchFileException x) {
                        err.format("%s: no such" + " file or directory%n", "keystore.jks");
                    } catch (DirectoryNotEmptyException x) {
                        err.format("%s not empty%n", "keystorePath");
                    } catch (IOException x) {
                        err.println(x);
                    }
                    Scanner console = new Scanner(System.in);
                    while(certificate==null || Objects.equals(certificate, "")) {
                        out.println("Введіть назву вашого сертифікату.");
                        certificate = console.nextLine();
                        if(certificate == null || Objects.equals(certificate, "")) {
                            err.println("Ви не ввели назву сертифікату.");
                        }
                    }
                    String crt = certificate + ".crt";
                    String p12 = certificate + ".p12";

                    while(certificatePassword==null || Objects.equals(certificatePassword, "")) {
                        out.println("Введіть пароль до сертифікату.");
                        certificatePassword = console.nextLine();
                        if(certificatePassword == null || Objects.equals(certificatePassword, "")) {
                            err.println("Ви не ввели пароль до сертифікату.");
                        }
                    }

                    String CreateKeyStore = "keytool -genkey -alias keystore -keystore keystore.jks" +
                            " -dname \"CN=localhost, OU=dev64, O=dev64-payment, L=Unknown, ST=Unknown, C=RU\"" +
                            " -storepass " + certificatePassword +
                            " -keypass " + certificatePassword;
                    Runtime.getRuntime().exec(CreateKeyStore).waitFor();
                    String DeleteKey = "keytool -delete -alias keystore -keystore keystore.jks" +
                            " -storepass " + certificatePassword +
                            " -keypass " + certificatePassword;
                    Runtime.getRuntime().exec(DeleteKey).waitFor();
                    String ImportCertificate = "keytool -import -v -trustcacerts -noprompt -alias server" +
                            " -file server -keystore keystore.jks" +
                            " -storepass " + certificatePassword +
                            " -keypass " + certificatePassword;
                    Runtime.getRuntime().exec(ImportCertificate).waitFor();

                    String crtExec = "keytool -import -v -trustcacerts -noprompt -alias " + certificate +
                            " -file " + crt + " -keystore keystore.jks" +
                            " -storepass " + certificatePassword;
                    Process crtExecProc = Runtime.getRuntime().exec(crtExec);
                    exitVal = crtExecProc.waitFor();
                    if(exitVal == 1) {
                        err.println("Назва або пароль введено не вірно.");
                        return;
                    }
                    String p12Exec = "keytool -v -importkeystore" +
                            " -srckeystore " + p12 +
                            " -srcstoretype pkcs12 -srcstorepass " + certificatePassword + " -destkeystore keystore.jks" +
                            " -deststoretype JKS -deststorepass " + certificatePassword;
                    Process p12ExecProc = Runtime.getRuntime().exec(p12Exec);
                    exitVal = p12ExecProc.waitFor();
                    if(exitVal == 1) {
                        err.println("Назва або пароль введено не вірно.");
                        return;
                    }
                    out.println("Сертифікат додано успішно.");
                } catch (Throwable ex) {
                    out.println(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }
    }

    private static String consoleReader(String display) {
        Console console = null;
        String inputString = null;
        try
        {
            console = System.console();
            if (console != null)
            {
                inputString = console.readLine(display);
                return inputString;
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return inputString;
    }
}