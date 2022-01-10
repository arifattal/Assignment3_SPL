package bgu.spl.net.srv;

import bgu.spl.net.api.Message.Message;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.bidi.BidiMessagingProtocol;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BlockingConnectionHandler<T> implements Runnable, bgu.spl.net.srv.ConnectionHandler<T> {

    private final BidiMessagingProtocol<T> protocol;
    private final MessageEncoderDecoder<T> encdec;
    private final Socket sock;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected = true;

    //added
    private ConnectionsImpl connections = ConnectionsImpl.getInstance();
    private int connectionId;



    public BlockingConnectionHandler(Socket sock, MessageEncoderDecoder<T> reader, BidiMessagingProtocol<T> protocol) {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;
        this.connectionId = connections.getNewConnectionId(); //gets a unique connectionId for this connectionHandler
        connections.addConnection(connectionId, this); //adds the connection handler to the HM that stores this info in connections
        protocol.start(connectionId, connections); //start() is essentially the protocol's constructor
    }

    @Override
    public void run() {
        try (Socket sock = this.sock) { //just for automatic closing
            int read;

            in = new BufferedInputStream(sock.getInputStream());
            out = new BufferedOutputStream(sock.getOutputStream());

            while (!protocol.shouldTerminate() && connected && (read = in.read()) >= 0) { //read() returns -1 in case of an error
                T nextMessage = encdec.decodeNextByte((byte) read);
                if (nextMessage != null) {
                    protocol.process(nextMessage);
                    //previous lines:
//                    T response = protocol.process(nextMessage);
//                    if (response != null) {
//                        out.write(encdec.encode(response));
//                        out.flush();
//                    }
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void close() throws IOException {
        connected = false;
        sock.close();
    }

    @Override
    //this function will be used to send a message to the client, this is accessed through connectionsImpl
    public void send(T msg) {
        if (msg != null){
            try {
                out.write(encdec.encode(msg));
                out.flush();
            } catch (IOException e) {}
        }
    }

    public BidiMessagingProtocol<T> getProtocol(){
        return protocol;
    };
}
