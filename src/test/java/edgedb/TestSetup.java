package edgedb;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TestSetup {

    private static String port = "10708";

    private static boolean isDockerInstallation;

    public static void main(String[] args) {
        try {
            deployNewDatabase();
           //connectToTestDB();
        } catch (Exception ex) {

        }
    }

    public void connectToTestDB() {

    }

    public static boolean checkIfDockerInstallation() {
        String os = System.getProperty("os.name");
        return os.toLowerCase().contains("windows");
    }

    private static void deployNewDatabase() throws Exception {
        List<String> cmdList = new ArrayList<String>();
        cmdList.add("edgedb");
        cmdList.add("server");
        cmdList.add("init");
        cmdList.add("test" + TestUtil.randomString(4));
        cmdList.add("--port");
        cmdList.add(port);
        cmdList.add("--default-user");
        cmdList.add("edgedb");
        cmdList.add("--default-database");
        cmdList.add("edgedb");

        TestUtil.executeProcess(cmdList);
    }


}

