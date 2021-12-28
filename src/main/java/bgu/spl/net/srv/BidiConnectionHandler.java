package bgu.spl.net.srv;

import bgu.spl.net.srv.Message.Message;

import java.io.IOException;

public class BidiConnectionHandler implements bgu.spl.net.srv.bidi.ConnectionHandler<Message> {
    @Override
    public void send(Message msg) {

    }

    @Override
    public void close() throws IOException {

    }
}
