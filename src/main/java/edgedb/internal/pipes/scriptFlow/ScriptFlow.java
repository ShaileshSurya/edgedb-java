package edgedb.internal.pipes.scriptFlow;
import edgedb.internal.protocol.ExecuteScript;
import edgedb.internal.protocol.client.writerV2.ProtocolWritable;
import edgedb.internal.protocol.client.writerV2.SyncBufferWritableDecorator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class ScriptFlow implements IScriptFlow {

    ProtocolWritable protocolWritable;
    @Override
    public void executeScriptMessage(ExecuteScript executeScript) throws IOException {
        log.debug("Sending Execute Script message {}",executeScript);
        protocolWritable.write(new SyncBufferWritableDecorator<>(executeScript));
    }
}