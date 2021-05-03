package edgedb;

import com.fasterxml.jackson.databind.ObjectMapper;
import edgedb.client.EdgeDBClientV2;
import edgedb.connection.BlockingConnection;
import edgedb.connection.IConnection;
import edgedb.connectionparams.ConnectionParams;
import edgedb.exceptions.EdgeDBIncompatibleDriverException;
import edgedb.exceptions.EdgeDBInternalErrException;
import edgedb.functional.blocking.TestRunner;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.security.SecureRandom;
import java.util.*;

@Slf4j
public final class TestUtil {

    private static final String LOCALHOST = "localhost";
    public static List executeProcess(List<String> cmdList) throws Exception {
        log.info("Executing Process with Command {}", cmdList);

        List<String> console = new ArrayList<String>();
        Process p;
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(cmdList);
        p = pb.start();
        p.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                p.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            console.add(line);
        }
        return console;
    }

    public static String randomAlphaNumeric(int len){
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        return randomString(len, AB);
    }
    public static String randomString(int len) {
        final String AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        return randomString(len, AB);
    }

    private static String randomString(int len, String AB) {
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }



    public static void sleepForMinute() throws InterruptedException {
        Thread.sleep( 30 * 1000);
    }

    public static void destroyInstance(String instanceName) throws Exception {
        List<String> cmdList = new ArrayList<String>();
        cmdList.add("edgedb");
        cmdList.add("server");
        cmdList.add("destroy");
        cmdList.add(instanceName);
        TestUtil.executeProcess(cmdList);
    }

//    public static void runMigration(IConnection connection) {
//        connection.execute(
//                ""
//        );
//    }

    public static void changeAuthMethodToTrust(String instanceName) throws Exception {
        List<String> cmdList = new ArrayList<String>();
        cmdList.add("edgedb");
        cmdList.add("-I" + instanceName);
        cmdList.add("--admin");
        cmdList.add("configure");
        cmdList.add("insert");
        cmdList.add("Auth");
        cmdList.add("--method=Trust");
        cmdList.add("--priority=0");

        TestUtil.executeProcess(cmdList);
    }

    public static IConnection connectToInstance(Credentials credentials) throws InterruptedException, EdgeDBInternalErrException, EdgeDBIncompatibleDriverException, IOException {

        ConnectionParams connectionParams = ConnectionParams.builder()
                .user(credentials.getUser())
                .database(credentials.getDatabase())
                .host(LOCALHOST)
                .port(Integer.parseInt(credentials.getPort()))
                .password(credentials.getPassword())
                .build();

        return TestDBConnectionSingleton.getInstance(connectionParams).getConnection();
    }

    public static boolean checkIfDockerInstallation() {
        String os = System.getProperty("os.name");
        return os.toLowerCase().contains("windows");
    }

    public static Credentials deployNewInstance(String instanceName) throws Exception {

        List<String> cmdList = new ArrayList<String>();
        cmdList.add("edgedb");
        cmdList.add("server");
        cmdList.add("init");
        cmdList.add(instanceName);
        cmdList.add("--default-user");
        cmdList.add("edgedb");
        cmdList.add("--default-database");
        cmdList.add("edgedb");

        List<String> consoleOutput = TestUtil.executeProcess(cmdList);

        return readCredentialsPath(findCredentialsPath(consoleOutput,instanceName));

    }

    private static Credentials readCredentialsPath(String credentialsPath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(String.valueOf(credentialsPath)), Credentials.class);
    }

    private static String findCredentialsPath(List<String> consoleOutput,String instanceName) {
        String pathValue = "";
        Optional<String> pathLineOptional = consoleOutput.stream().filter(
                line -> line.contains("Credentials Path")
        ).findAny();

        if (pathLineOptional.isPresent()) {
            String pathLine = pathLineOptional.get();

            Optional<String> pathOptional = Arrays.stream(pathLine.split("\\s+")).filter(
                    line -> line.contains(instanceName)
            ).findAny();

            if (pathOptional.isPresent()) {
                pathValue = pathOptional.get();
            }
        }
        return pathValue.trim();
    }
}
