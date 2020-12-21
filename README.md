# EdgeDB-JAVA
This is an unofficial EdgeDB driver for JAVA ecosystem.

# ⚠️ WIP ⚠️
This project is far from production ready. We need your support, feel free to contribute !

## API Support
##### BlockingIO Connection 
```java
public String queryOneJSON(String query)
public <T> T queryOneJSON(String query,Class<T> classType)
public String queryJSON(String query)
public <T> List<T> queryJSON(String query, Class<T> classType)
Execute(String command)
```
## Basic Recipes
```java

package edgedb.client;

import edgedb.exceptions.FailedToConnectEdgeDBServer;
import edgedb.exceptions.FailedToDecodeServerResponseException;

import java.io.IOException;

public class ReadMeExample {

    public static void main(String[] args) {
        String dsn = "edgedb://edgedb@localhost:5656/tutorial";
        EdgeDBClient db = new EdgeDBClient().withDSN(dsn);

        try {
            db.connect();
            String jsonResult = db.execute("SELECT Movie { id, title}");
            System.out.Println(result);
            db.terminate();
        } catch (FailedToDecodeServerResponseException |FailedToConnectEdgeDBServer| IOException e) {
            e.printStackTrace();
        } 
    }
}

```

## Contributing

1. Fork the repo and create your branch from main.
2. If you've added code that should be tested, add tests.
4. Ensure the test suite passes.
5. Make sure your code lints.
6. Issue that pull request!

View [CONTRIBUTING.md](CONTRIBUTING.md) to learn more about how to contribute.

## License
edgedb-java is developed and distributed under the Apache 2.0 license.
    