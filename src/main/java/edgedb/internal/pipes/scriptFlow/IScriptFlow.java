package edgedb.internal.pipes.scriptFlow;

import edgedb.internal.protocol.ExecuteScript;

import java.io.IOException;

public interface IScriptFlow {
    public void executeScriptMessage(ExecuteScript executeScriptMessage) throws IOException;
}
