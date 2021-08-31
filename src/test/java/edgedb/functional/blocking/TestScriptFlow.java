package edgedb.functional.blocking;

import edgedb.TestDBConnectionSingleton;
import edgedb.connection.IConnection;
import edgedb.exceptions.clientexception.EdgeQLSyntaxException;
import edgedb.exceptions.clientexception.InvalidArgumentException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class TestScriptFlow {

    static IConnection connection;

    @BeforeAll
    public static void init() {
        try {
            connection = TestDBConnectionSingleton.getInstance(null).getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void TestScriptFlow() {
        Assertions.assertDoesNotThrow(
                () -> executeCreateDBT()
        );
    }

    private void executeCreateDBT() {
        connection.execute("SELECT 'It worked!';");
    }


    @Test
    public void TestScriptFlowException() {
        EdgeQLSyntaxException EdgeQLSyntaxException = assertThrows(EdgeQLSyntaxException.class, () ->
                connection.execute("INVALID COMMAND;")
        );
        assertEquals("EdgeQLSyntaxError", EdgeQLSyntaxException.getErrorCode());
    }


}
