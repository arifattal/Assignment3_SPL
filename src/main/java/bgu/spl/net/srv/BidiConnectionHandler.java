package bgu.spl.net.srv;

import bgu.spl.net.api.Message.Message;
import bgu.spl.net.srv.bidi.ConnectionHandler;
import java.io.IOException;

public class BidiConnectionHandler implements ConnectionHandler<Message> {
    @Override
    public void send(Message msg) {

    }

    @Override
    public void close() throws IOException {

    }
}
