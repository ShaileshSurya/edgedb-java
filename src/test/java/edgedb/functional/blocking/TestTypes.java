package edgedb.functional.blocking;

import edgedb.TestDBConnectionSingleton;
import edgedb.client.ResultSet;
import edgedb.connection.IConnection;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TestTypes {

    static IConnection blockingConnection;

    @BeforeAll
    public static void init() {
        try {
            blockingConnection = TestDBConnectionSingleton.getInstance(null).getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void basicScalarTest() {
        String query = "select {\n" +
                "        -1,\n" +
                "        1,\n" +
                "        0,\n" +
                "        15,\n" +
                "        281474976710656,\n" +
                "        22,\n" +
                "        -11111,\n" +
                "        346456723423,\n" +
                "        -346456723423,\n" +
                "        2251799813685125,\n" +
                "        -2251799813685125\n" +
                "      };";

        ResultSet result = blockingConnection.query(query);
        log.info("{}", result);
    }


    @Test
    public void testInt32() {
        String query = "select <int32>{-1, 0, 1, 10, 2147483647};";
        ResultSet result = blockingConnection.query(query);
        log.info("{}", result);
    }

    @Test
    public void testIn16() {
        String query = "select <int16>{-1, 0, 1, 10, 15, 22, -1111};";
        ResultSet result = blockingConnection.query(query);
        log.info("{}", result);
    }

    @Test
    public void testBoolean() {
        String query = "select {true, false, false, true, false};";
        ResultSet result = blockingConnection.query(query);
        log.info("{}", result);
    }

    @Test
    public void testFloat64() {
        String query = "select [<float64>123.2, <float64>-1.1]";
        ResultSet result = blockingConnection.queryOne(query);
        log.info("{}", result);
    }

    @Test
    public void testFloat32QueryOne() {
        String query = "select <int32>{-1, 0, 1, 10, 2147483647};";
        ResultSet result = blockingConnection.query(query);
        log.info("{}", result);
    }

    @Test
    public void testStringQueryOne() {
        String query = "select <int32>{-1, 0, 1, 10, 2147483647};";
        ResultSet result = blockingConnection.queryOne(query);
        log.info("{}", result);
    }

    @Test
    public void testJSONQueryOne() {
        String query = "select <json>[1, 2, 3]";
        ResultSet result = blockingConnection.queryOne(query);
        log.info("{}", result);
    }

    public void testBigInt() {
        List<BigInteger> list = new ArrayList<>();
        list.add(BigInteger.valueOf(0));
        list.add(BigInteger.valueOf(-0));


        // Generate random bigints
        for (int i = 0; i < 1000; i++) {
            double length = Math.floor(Math.random() * 30) + 1;
            String numFrom1  = "";
            String numFrom0  = "";
            for (double j = 0; j < length; j++) {
                numFrom1 = ("0123456789"+ String.valueOf(Math.floor(Math.random() * 10)));
            }
            list.add(new BigInteger(numFrom1));
            list.add(new BigInteger(numFrom0));
        }

        for (int i = 0; i < 1000; i++) {
            double length = Math.floor(Math.random() * 50) + 1;
            String numFrom0  = "";
            for (double j = 0; j < length; j++) {
                numFrom0 = ("0000000012"+ String.valueOf(Math.floor(Math.random() * 10)));
            }
            list.add(new BigInteger(numFrom0));
        }

        String query = "select <array<bigint>>$0";
        ResultSet result = blockingConnection.queryOne(query);
    }
}
