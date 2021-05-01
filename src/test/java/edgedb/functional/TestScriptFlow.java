package edgedb.functional;

import edgedb.client.EdgeDBClientV2;
import edgedb.connection.BlockingConnection;
import edgedb.connection.IConnection;
import edgedb.connectionparams.ConnectionParams;
import edgedb.exceptions.EdgeDBIncompatibleDriverException;
import edgedb.exceptions.EdgeDBInternalErrException;
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
    static void init() {

        try {
            ConnectionParams connectionParams = ConnectionParams.builder()
                    .port(10700)
                    .password("2TDvaRfTziTIR4wBy6DQ4SqY")
                    .database("edgedb")
                    .user("edgedb")
                    .host("localhost")
                    .build();
            connection = new EdgeDBClientV2(new BlockingConnection()).getConnection(connectionParams);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (EdgeDBIncompatibleDriverException e) {
            e.printStackTrace();
        } catch (EdgeDBInternalErrException e) {
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
        InvalidArgumentException invalidArgumentException = assertThrows(InvalidArgumentException.class, () ->
                connection.execute("INVALID COMMAND;")
        );
        assertEquals("EdgeQLSyntaxError", invalidArgumentException.getErrorCode());
    }


}
