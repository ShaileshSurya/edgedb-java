package edgedb.functional;

import edgedb.TestSetup;
import edgedb.connection.IConnection;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class TestScriptFlow {

    IConnection connection;

    @Before
    public void initialize(){
//        try {
//            connection = new TestSetup().connectToTestDB();
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
    }

    @Test
    public void TestScriptFlow(){
        connection.execute(
                "CREATE DATABASE test2 ;"
        );
    }

}
