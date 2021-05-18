package edgedb.functional.blocking;

import edgedb.Credentials;
import edgedb.TestUtil;
import edgedb.client.EdgeDBClientV2;
import edgedb.connection.BlockingConnection;
import edgedb.connectionparams.ConnectionParams;
import edgedb.exceptions.clientexception.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@Disabled
public class TestConnection {

    private static Credentials credentials;
    private static ConnectionParams connectionParams;


    public static String instanceName = "test" + TestUtil.randomString(3);

    @BeforeAll
    public static void init(){
        try {
            credentials = TestUtil.deployNewInstance(instanceName);
            TestUtil.sleepForMinute();
            connectionParams = ConnectionParams.builder()
                    .user(credentials.getUser())
                    .database(credentials.getDatabase())
                    .host("localhost")
                    .port(Integer.parseInt(credentials.getPort()))
                    .password(credentials.getPassword())
                    .build();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @AfterAll
    public static void tearDown(){
        try {
            TestUtil.destroyInstance(instanceName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void connectAndTerminateSuccessTest() {
        Assertions.assertDoesNotThrow(
                () -> tryConnect(connectionParams)
        );
    }

    private void tryConnect(ConnectionParams connectionParams) throws IOException {
        EdgeDBClientV2 clientV2 = new EdgeDBClientV2(new BlockingConnection());
        clientV2.getConnection(connectionParams);
        clientV2.terminateConnection();
    }

    @Test
    public void connectionParamsBuilderTest() {

        //password
        assertThrows(NullPointerException.class, () ->
                ConnectionParams.builder()
                        .port(10700)
                        .database("edgedb")
                        .user("edgedb")
                        .host("localhost")
                        .build()
        );

        //database
        assertThrows(NullPointerException.class, () ->
                ConnectionParams.builder()
                        .port(10700)
                        .password("2TDvaRfTziTIR4wBy6DQ4SqY")
                        .user("edgedb")
                        .host("localhost")
                        .build()
        );

        //user
        assertThrows(NullPointerException.class, () ->
                ConnectionParams.builder()
                        .port(10700)
                        .password("2TDvaRfTziTIR4wBy6DQ4SqY")
                        .database("edgedb")
                        .host("localhost")
                        .build()
        );

        //host
        assertThrows(NullPointerException.class, () ->
                ConnectionParams.builder()
                        .port(10700)
                        .password("2TDvaRfTziTIR4wBy6DQ4SqY")
                        .database("edgedb")
                        .user("edgedb")
                        .build()
        );

        //port
        assertThrows(NullPointerException.class, () ->
                ConnectionParams.builder()
                        .host("localhost")
                        .password("2TDvaRfTziTIR4wBy6DQ4SqY")
                        .database("edgedb")
                        .user("edgedb")
                        .build()
        );

    }

    @Test
    public void connectWithWrongParametersFailRandomUserTest() throws IOException {
        EdgeDBClientV2 clientV2 = new EdgeDBClientV2(new BlockingConnection());
        ConnectionParams connectionParams1 = shallowCopyConnectionParams(connectionParams);
        clientV2.getConnection(connectionParams1);
        connectionParams1.setUser("randomuser");

        ClientException clientException= assertThrows(ClientException.class, () ->
                clientV2.getConnection(connectionParams1)
        );
        Assertions.assertEquals("AuthenticationError", clientException.getErrorCode());
    }

    @Test
    public void connectWithWrongParametersFailWrongPasswordTest() throws IOException {
        EdgeDBClientV2 clientV2 = new EdgeDBClientV2(new BlockingConnection());
        ConnectionParams connectionParams1 = shallowCopyConnectionParams(connectionParams);
        clientV2.getConnection(connectionParams1);
        connectionParams1.setPassword("randompass");

        ClientException clientException= assertThrows(ClientException.class, () ->
                clientV2.getConnection(connectionParams1)
        );
        Assertions.assertEquals("AuthenticationError", clientException.getErrorCode());
    }


    @Test
    public void connectWithWrongParametersFailRandomDBTest() throws IOException {
        EdgeDBClientV2 clientV2 = new EdgeDBClientV2(new BlockingConnection());
        ConnectionParams connectionParams1 = shallowCopyConnectionParams(connectionParams);
        clientV2.getConnection(connectionParams1);
        connectionParams1.setPassword("randomDB");

        ClientException clientException= assertThrows(ClientException.class, () ->
                clientV2.getConnection(connectionParams1)
        );
        Assertions.assertEquals("AuthenticationError", clientException.getErrorCode());
    }

    private ConnectionParams shallowCopyConnectionParams(ConnectionParams connectionParams) {
        ConnectionParams shallowCopy = new ConnectionParams();
        shallowCopy.setAdmin(connectionParams.getAdmin());
        shallowCopy.setDatabase(connectionParams.getDatabase());
        shallowCopy.setUser(connectionParams.getUser());
        shallowCopy.setHost(connectionParams.getHost());
        shallowCopy.setPort(connectionParams.getPort());
        shallowCopy.setDsn(connectionParams.getDsn());
        shallowCopy.setTimeout(connectionParams.getTimeout());
        shallowCopy.setPassword(connectionParams.getPassword());
        return shallowCopy;
    }
}
