package bgu.spl.net.srv;

import bgu.spl.net.bidi.BidiMessagingProtocol;
import bgu.spl.net.bidi.Connections;
import bgu.spl.net.srv.Message.Message;

public class protocol implements BidiMessagingProtocol {
    User user;

    @Override
    public void start(int connectionId, Connections connections) {

    }

    @Override
    public void process(Message message) {
        message.runMessage(user);
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }
}
