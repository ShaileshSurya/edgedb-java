package edgedb.client;

import edgedb.exceptions.*;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;

public class EdgeDBTest {

    @Mock
    Connection connection;

    @Test
    public void connectTest() {
        String user = "edgedb";
        String database = "tutorial";

        String hostEcho = "localhost";
        String portEcho = "8083";
        String hostReal = "10.199.198.56";
        String portReal = "5656";

        boolean isEcho = false;
        String port = isEcho ? portEcho : portReal;
        String host = isEcho ? hostEcho : hostReal;

        String dsn = "edgedb://" + user + "@" + host + ":" + port + "/" + database;
        EdgeDBClient db = new EdgeDBClient().withDSN(dsn);
        try {
            db.connect();

            //db.execute("SELECT Movie { id, title, year }");
            //String result = db.execute("SELECT 1 + 1");

//            System.out.println(db.execute("INSERT Movie {\n" +
//                    "    title := 'Shailesh_test',\n" +
//                    "    director := (\n" +
//                    "        SELECT Person\n" +
//                    "        FILTER\n" +
//                    "            # the last name is sufficient\n" +
//                    "            # to identify the right person\n" +
//                    "            .last_name = 'Villeneuve'\n" +
//                    "        # the LIMIT is needed to satisfy the single\n" +
//                    "        # link requirement validation\n" +
//                    "        LIMIT 1\n" +
//                    "    )\n" +
//                    "};"));
//            System.out.println(db.queryOneJSON("SELECT Movie {\n" +
//                    "    id,\n" +
//                    "    title,\n" +
//                    "    year\n" +
//                    "}\n" +
//                    "FILTER .id = <uuid>'40913868-1856-11eb-811a-9b60813706a4'"));

            System.out.println(db.queryJSON("SELECT Movie {\n" +
                    "    id,\n" +
                    "    title,\n" +
                    "    year\n" +
                    "}"));


            String result = db.execute("SELECT 2 + 5");

            System.out.println("~~~~~~~~~~~Result~~~~~~~~~~");
            System.out.println("~~~~~~~~~~~Result~~~~~~~~~~");
            System.out.println("~~~~~~~~~~~Result~~~~~~~~~~");
            System.out.println("~~~~~~~~~~~Result~~~~~~~~~~");
            System.out.println(result);

        } catch (IOException | EdgeDBInternalErrException | EdgeDBFailedToConnectServer | EdgeDBCommandException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
//        } catch (EdgeDBInternalErrException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (EdgeDBServerException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void test() {
        byte[] arr = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1};
        for (byte b : arr) {
            System.out.print(Character.forDigit((b >> 4 & 0xF), 16) + " ");
        }
    }
}
