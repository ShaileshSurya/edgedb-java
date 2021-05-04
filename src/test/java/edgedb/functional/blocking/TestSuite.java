package edgedb.functional.blocking;


import edgedb.Credentials;
import edgedb.TestUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;

import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectClasses({TestScriptFlow.class})
public class TestSuite {
    private static String instanceName = "test" + TestUtil.randomString(4);
    private static Credentials credentials;

    @BeforeAll
    public static void init(){
        try {
            credentials = TestUtil.deployNewInstance(instanceName);
            TestUtil.sleepForMinute();
            TestUtil.connectToInstance(credentials);
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

    @AfterAll
    public static void teardown(){
        try {
            TestUtil.destroyInstance(instanceName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
