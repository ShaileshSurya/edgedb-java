package edgedb;

import edgedb.client.EdgeDBClientV2;
import edgedb.client.ResultSet;
import edgedb.connection.BlockingConnection;
import edgedb.connection.IConnection;
import edgedb.exceptions.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TestSetup {

    private static String port = "10708";
    private static String instanceName;

    // <driver>://<username>:<password>@<host>:<port>/<database>
    String dsnNew = "edgedb://edgedb@localhost:10708/edgedb";

    private static boolean isDockerInstallation;

    public static void main(String[] args) {
        try {
            //deployNewDatabase();
            //changeAuthMethodToTrust();
            IConnection connection = connectToTestDB();

            ResultSet result = connection.queryJSON("SELECT Movie { id, title, year }");
            log.info("Result ~~~~~{}",result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void changeAuthMethodToTrust() throws Exception {
        List<String> cmdList = new ArrayList<String>();
        cmdList.add("edgedb");
        cmdList.add("-I"+instanceName);
        cmdList.add("--admin");
        cmdList.add("configure");
        cmdList.add("insert");
        cmdList.add("Auth");
        cmdList.add("--method=Trust");
        cmdList.add("--priority=0");
        TestUtil.executeProcess(cmdList);
    }

    public static IConnection connectToTestDB() throws InterruptedException, EdgeDBInternalErrException, EdgeDBIncompatibleDriverException, IOException {
        EdgeDBClientV2 clientV2 = new EdgeDBClientV2(new BlockingConnection());

        String username = "edgedb";
        String host = "localhost";
        // TODO -- fetch this programmatically.
        int port = 10701;
        String database = "edgedb";
        String dsn = String.format("edgedb://%s@%s:%d/%s",username,host,port,database);

        log.info("DSN to connect {}",dsn);

        return clientV2.getConnection(dsn);
    }

    public static boolean checkIfDockerInstallation() {
        String os = System.getProperty("os.name");
        return os.toLowerCase().contains("windows");
    }

    private static void deployNewDatabase() throws Exception {

        instanceName = "test" + TestUtil.randomString(4);
        List<String> cmdList = new ArrayList<String>();
        cmdList.add("edgedb");
        cmdList.add("server");
        cmdList.add("init");
        cmdList.add(instanceName);
        cmdList.add("--port");
        cmdList.add(port);
        cmdList.add("--default-user");
        cmdList.add("edgedb");
        cmdList.add("--default-database");
        cmdList.add("edgedb");

        TestUtil.executeProcess(cmdList);
    }


}

