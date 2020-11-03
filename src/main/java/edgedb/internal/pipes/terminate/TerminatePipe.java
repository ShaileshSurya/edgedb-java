package edgedb.internal.pipes.terminate;

import edgedb.client.SocketStream;
import edgedb.internal.protocol.client.Terminate;
import edgedb.internal.protocol.client.writer.TerminateWriter;
import lombok.AllArgsConstructor;

import java.io.IOException;

@AllArgsConstructor
public class TerminatePipe {

    SocketStream socketStream;

    public void terminate() throws IOException {
        writeAndFlushTerminateMessage();
    }

    private void writeAndFlushTerminateMessage() throws IOException {
        Terminate terminate = buildTerminateMessage();
        TerminateWriter writer = new TerminateWriter(socketStream.getDataOutputStream(), terminate);
        writer.writeAndFlush();
    }

    private Terminate buildTerminateMessage() {
        Terminate terminate = new Terminate();
        terminate.setMessageLength(terminate.calculateMessageLength());
        return terminate;
    }

}
