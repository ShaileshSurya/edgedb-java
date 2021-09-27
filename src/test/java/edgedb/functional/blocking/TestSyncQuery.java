package edgedb.functional.blocking;

import edgedb.TestDBConnectionSingleton;
import edgedb.client.ResultSet;
import edgedb.connection.IConnection;
import edgedb.exceptions.clientexception.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class TestSyncQuery {

    static IConnection connection;

    @BeforeAll
    public static void init() {
        try {
            connection = TestDBConnectionSingleton.getInstance(null).getConnection();
            setup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setup() {
        //TODO write a better query here
        String createModule = "CREATE MODULE test";
        connection.execute(createModule);
        String setup = "CREATE TYPE test::Tmp {\n" +
                "            CREATE REQUIRED PROPERTY tmp -> std::str;\n" +
                "        };";

        connection.execute(setup);
    }

    @AfterAll
    public static void tearDown() {
        //TODO write better queries

        String teardown = "DROP TYPE test::Tmp;";

        connection.execute(teardown);

        String dropModule = "DROP MODULE test;";

        connection.execute(dropModule);
    }

    @Test
    public void TestSyncParseErrorRecover_01() {
        ResultSet result = connection.query("select 1;");
//        for (int i = 0; i < 2; i++) {
//            assertThrows(EdgeQLSyntaxException.class, () ->
//                    connection.query("select syntax error")
//            );
//
//            assertThrows(EdgeQLSyntaxException.class, () ->
//                    connection.query("select (")
//            );
//
//            assertThrows(EdgeQLSyntaxException.class, () ->
//                    connection.queryJSON("select (")
//            );
//        }
//        for (int i = 0; i < 10; i++) {
//            //TODO check the assertion here
//            ResultSet result = connection.query("select 1;");
//        }

    }

    @Test
    public void TestSyncParseErrorRecover_02() {

        for (int i = 0; i < 2; i++) {

            assertThrows(EdgeQLSyntaxException.class, () ->
                    connection.execute("select syntax error")
            );

            assertThrows(EdgeQLSyntaxException.class, () ->
                    connection.execute("select syntax error")
            );

            assertThrows(EdgeQLSyntaxException.class, () ->
                    connection.execute("select (")
            );

            for (int j = 0; j < 10; j++) {
                    connection.execute("select 1; select 2;");
            }

        }

    }

    @Test
    public void TestSyncExecErrorRecover_01(){
        for (int i = 0; i < 2; i++) {

            assertThrows(DivisionByZeroException.class, () ->
                    connection.execute("select 1 / 0;")
            );

            assertThrows(DivisionByZeroException.class, () ->
                    connection.execute("select 1 / 0;")
            );

            for (int j = 0; j < 10; j++) {
                connection.execute("select 1; select 2;");
                //TODO  edgedb.Set((1,)))
            }
        }
    }

    @Test
    public void TestSyncExecErrorRecover_02(){
        for (int i = 0; i < 2; i++) {

            assertThrows(DivisionByZeroException.class, () ->
                    connection.execute("select 1 / 0;")
            );

            assertThrows(DivisionByZeroException.class, () ->
                    connection.execute("select 1 / 0;")
            );

            for (int j = 0; j < 10; j++) {
                connection.execute("select 1; select 2;");
                //TODO  edgedb.Set((1,)))
            }
        }
    }

    //TODO:- Fix this test
   /* public void TestSyncExecErrorRecover_03(){
        String query = "select 10 // <int64>$0;";

        List<Integer> list = Arrays.asList(1,2,0,3,1,0,1);

        for(Integer val : list){
            if(val == 0){
                assertThrows(DivisionByZeroException.class, () ->
                        connection.query(query,val) => This returns EdgeDB SET
                );
            }else {

            }
        }
    }*/


//   @Test
//   //TODO fix this testCase
//    public void TestSyncExecErrorRecover_04(){
//        String query = "select 10 / %s;";
//
//        List<String> list = Arrays.asList("1","2","0");
//
//        for(String val : list){
//            if(val == "0"){
//                assertThrows(DivisionByZeroException.class, () ->
//                        connection.query(String.format(query,val))
//                );
//            }else {
//                connection.execute(String.format(query,val));
//            }
//        }
//    }

    @Test
    public void TestSyncExecErrorRecover_05(){
        assertThrows(QueryException.class, () ->
                        connection.execute("select <int64>$0")
        );

        ResultSet resultSet = connection.query("SELECT \"HELLO\"");
        //TODO check how you can assert this to
//        self.assertEqual(
//                self.con.query('SELECT "HELLO"'),
//                ["HELLO"])
    }

    //TODO add assertions
    @Test
    public void TestSyncQuerySingleCommand01(){
        ResultSet resultSet = connection.query("CREATE TYPE test::server_query_single_command_01 {\n" +
                "                CREATE REQUIRED PROPERTY server_query_single_command_01 -> std::str;\n" +
                "            };");

        log.info("ResultSet rs {}", resultSet);

        resultSet = connection.query("DROP TYPE test::server_query_single_command_01;");

        log.info("ResultSet rs {}", resultSet);

        resultSet = connection.queryJSON("CREATE TYPE test::server_query_single_command_01 {\n" +
                "                CREATE REQUIRED PROPERTY server_query_single_command_01 ->\n" +
                "                    std::str;\n" +
                "            };");

        log.info("ResultSet rs {}", resultSet);


        resultSet = connection.query("DROP TYPE test::server_query_single_command_01;");

        log.info("ResultSet rs {}", resultSet);

        //self.assertTrue(self.con._get_last_status().startswith('DROP'))

    }

    @Test
    public void TestSyncQuerySingleCommand02(){
        ResultSet resultSet = connection.query("SET MODULE default;");

        log.info("ResultSet rs {}", resultSet);

        resultSet = connection.query("SET ALIAS foo AS MODULE default;");

        log.info("ResultSet rs {}", resultSet);

        resultSet = connection.query("SET MODULE default;");

        log.info("ResultSet rs {}", resultSet);

        //TODO fix this
//        assertThrows(InterfaceException.class, () ->
//                connection.queryOne("SET ALIAS bar AS MODULE std;")
//        );

        resultSet = connection.query("SET ALIAS bar AS MODULE std;");

        log.info("ResultSet rs {}", resultSet);

        resultSet = connection.queryJSON("SET MODULE default;");

        log.info("ResultSet rs {}", resultSet);

        resultSet = connection.queryJSON("SET ALIAS bar AS MODULE std;");

        log.info("ResultSet rs {}", resultSet);
    }

}
