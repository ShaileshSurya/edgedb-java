package edgedb.functional.blocking;

import edgedb.TestDBConnectionSingleton;
import edgedb.client.ResultSet;
import edgedb.connection.IConnection;
import edgedb.exceptions.clientexception.DivisionByZeroException;
import edgedb.exceptions.clientexception.EdgeQLSyntaxException;
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
        String setup = "CREATE TYPE test {\n" +
                " CREATE REQUIRED PROPERTY tmp -> std::str;" +
                "};";

        connection.execute(setup);
    }

    @AfterAll
    public static void tearDown() {
        String teardown = "DROP TYPE test;";

        connection.execute(teardown);

    }

    @Test
    public void TestSyncParseErrorRecover_01() {
        for (int i = 0; i < 2; i++) {
            assertThrows(EdgeQLSyntaxException.class, () ->
                    connection.query("select syntax error")
            );

            assertThrows(EdgeQLSyntaxException.class, () ->
                    connection.query("select (")
            );

            assertThrows(EdgeQLSyntaxException.class, () ->
                    connection.queryJSON("select (")
            );
        }
        for (int i = 0; i < 10; i++) {
            //TODO check the assertion here
            ResultSet result = connection.query("select 1;");
        }

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
            }
        }
    }

}
