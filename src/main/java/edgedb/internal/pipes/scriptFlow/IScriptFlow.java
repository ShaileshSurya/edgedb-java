package edgedb.internal.pipes.scriptFlow;

import edgedb.internal.protocol.ExecuteScript;
import edgedb.internal.protocol.Prepare;

import java.io.IOException;

public interface IScriptFlow {
    public void executeScriptMessage(ExecuteScript executeScript) throws IOException;
}
