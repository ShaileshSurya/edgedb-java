package edgedb;

import com.fasterxml.jackson.databind.ObjectMapper;
import edgedb.client.EdgeDBClientV2;
import edgedb.connection.BlockingConnection;
import edgedb.connection.IConnection;
import edgedb.connectionparams.ConnectionParams;
import edgedb.exceptions.EdgeDBIncompatibleDriverException;
import edgedb.exceptions.EdgeDBInternalErrException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public final class TestSetup {

    private static String instanceName;
    private static Credentials credentials;
    private static final String LOCALHOST = "localhost";

    public static void main(String[] args) {
        try {
            deployNewInstance();
            sleepForMinute();
            connectToInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                destroyInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void sleepForMinute() throws InterruptedException {
        Thread.sleep( 30 * 1000);
    }

    public static void destroyInstance() throws Exception {
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

    public static void changeAuthMethodToTrust() throws Exception {
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

    public static IConnection connectToInstance() throws InterruptedException, EdgeDBInternalErrException, EdgeDBIncompatibleDriverException, IOException {
        EdgeDBClientV2 clientV2 = new EdgeDBClientV2(new BlockingConnection());

        ConnectionParams connectionParams = ConnectionParams.builder()
                .user(credentials.getUser())
                .database(credentials.getDatabase())
                .host(LOCALHOST)
                .port(Integer.parseInt(credentials.getPort()))
                .password(credentials.getPassword())
                .build();

        return clientV2.getConnection(connectionParams);
    }

    public static boolean checkIfDockerInstallation() {
        String os = System.getProperty("os.name");
        return os.toLowerCase().contains("windows");
    }

    private static void deployNewInstance() throws Exception {

        instanceName = "test" + TestUtil.randomString(4);
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

        credentials = readCredentialsPath(findCredentialsPath(consoleOutput));

    }

    private static Credentials readCredentialsPath(String credentialsPath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(String.valueOf(credentialsPath)), Credentials.class);
    }

    private static String findCredentialsPath(List<String> consoleOutput) {
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

