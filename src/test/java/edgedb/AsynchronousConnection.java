package edgedb;

import edgedb.exceptions.clientexception.ClientException;
import edgedb.exceptions.constants.Severity;
import edgedb.internal.buffer.SingletonBuffer;
import edgedb.internal.pipes.SyncFlow.SyncPipe;
import edgedb.internal.pipes.SyncFlow.SyncPipeImpl;
import edgedb.internal.protocol.*;
import edgedb.internal.protocol.client.writerV2.AsyncSocketChannelProtocolWritableImpl;
import edgedb.internal.protocol.client.writerV2.ChannelProtocolWritableImpl;
import edgedb.internal.protocol.server.readerfactory.ChannelProtocolReaderFactoryImpl;
import edgedb.internal.protocol.server.readerv2.*;
import lombok.extern.slf4j.Slf4j;

import javax.print.attribute.standard.Severity;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static edgedb.client.ClientConstants.MAJOR_VERSION;
import static edgedb.client.ClientConstants.MINOR_VERSION;
import static edgedb.exceptions.constants.ClientErrors.INCOMPATIBLE_DRIVER;
import static edgedb.internal.protocol.constants.CommonConstants.BUFFER_SIZE;
import static edgedb.internal.protocol.constants.TransactionState.*;

@Slf4j
public class AsynchronousConnection {



    public static void main(String args[]) throws IOException, ExecutionException, InterruptedException {

        try (AsynchronousSocketChannel client = AsynchronousSocketChannel.open()) {

            Future<Void> result = client.connect(
                    new InetSocketAddress("localhost", 10700));
            result.get();

            String str = "Hello! How are you?";

            ClientHandshake clientHandshake = new ClientHandshake("edgedb","edgedb");

            AsyncSocketChannelProtocolWritableImpl protocolWritable = new AsyncSocketChannelProtocolWritableImpl(client);
            protocolWritable.write(clientHandshake);

            ByteBuffer readBuffer = SingletonBuffer.getInstance().getBuffer();
            BufferReader bufferReader = new AsyncClientReaderImpl(client);
            readBuffer = bufferReader.read(readBuffer);

            while (readBuffer.hasRemaining()) {
                byte mType = readBuffer.get();
                ProtocolReader reader = new ChannelProtocolReaderFactoryImpl(readBuffer)
                        .getProtocolReader((char) mType, readBuffer);

                ServerProtocolBehaviour response = reader.read(readBuffer);
                log.info(" Response Found was {}", response.toString());
                if (response instanceof ServerHandshakeBehaviour) {
                    ServerHandshakeBehaviour serverHandshake = (ServerHandshakeBehaviour) response;
                    log.debug("Response is an Instance Of ServerHandshake {}", serverHandshake);
                    String message = String.format("Incompatible driver expected Minor Version %s and Major Version %s,found Minor Version %s and Major Version %s", MAJOR_VERSION, MINOR_VERSION, serverHandshake.getMajorVersion(), serverHandshake.getMinorVersion());
                    throw new ClientException(message);
                } else if (response instanceof ReadyForCommand) {
                    ReadyForCommand readyForCommand = (ReadyForCommand) response;
                    log.debug("Response is an Instance Of ReadyForCommand {}", readyForCommand);

                    switch (readyForCommand.getTransactionState()) {
                        case (int) IN_FAILED_TRANSACTION:
//                            SyncPipe syncPipe = new SyncPipeImpl(
//                                    new ChannelProtocolWritableImpl(getChannel()));
//                            syncPipe.sendSyncMessage();
                            break;
                        case (int) IN_TRANSACTION:
                            break;
                        case (int) NOT_IN_TRANSACTION:
                            return;
                    }
                } else if (response instanceof ServerAuthenticationBehaviour) {
                    ServerAuthenticationBehaviour serverAuthenticationBehaviour = (ServerAuthenticationBehaviour) response;

                    if (serverAuthenticationBehaviour.isAuthenticationOkMessage()) {
                        log.info("Authentication Successful");
                        return;
                    } else if (serverAuthenticationBehaviour.isAuthenticationRequiredSASLMessage()) {
                        //authenticateSASL();
                        continue;
                    }
                } else if (response instanceof ServerKeyDataBehaviour) {
                    continue;
                }
            }

//            buffer.flip();
//            //ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
//            Future<Integer> writeval = client.write(buffer);
//
//            writeval.get();
//            buffer.flip();
//
//           // ByteBuffer buffer1 =  ByteBuffer.allocate(BUFFER_SIZE);;
//            Future<Integer> readval = client.read(buffer);
//
//            readval.get();
//
//            System.out.println("Received from server: "
//                    + new String(buffer.array()).trim());
//            buffer.clear();
        } catch (ExecutionException | IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("Disconnected from the server.");
        }

    }

}
