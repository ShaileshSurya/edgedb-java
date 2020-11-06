package edgedb.client;


import java.io.*;

public class BlockingIOConnection extends BaseConnectionV2 {

    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    
    public BlockingIOConnection(String dsn){
        super(dsn);
    }

    @Override
    public void connect() throws IOException {

    }

    @Override
    public void terminate() {

    }
}
