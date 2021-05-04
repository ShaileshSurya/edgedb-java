package edgedb;

import edgedb.functional.blocking.TestRunner;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class TestSetup {

    private static String instanceName = "test" + TestUtil.randomString(4);

    private static Credentials credentials;
    private static final String LOCALHOST = "localhost";


    public static void main(String[] args) {
        try {
            credentials = TestUtil.deployNewInstance(instanceName);
            TestUtil.sleepForMinute();
            TestUtil.connectToInstance(credentials);
            runBlockingConnectionTests();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                TestUtil.destroyInstance(instanceName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void runBlockingConnectionTests(){
       TestRunner.runTests();
    }

}

