package bgu.spl.net.api;

import bgu.spl.net.bidi.BidiMessagingProtocol;
import bgu.spl.net.bidi.Connections;
import bgu.spl.net.api.Message.Message;

public class protocol<T> implements BidiMessagingProtocol<T> {
    User user;
    Integer connectionId = null;
    Connections connections = null;

    @Override
    public void start(int connectionId, Connections connections) {
        this.connectionId = connectionId;
        this.connections = connections;
    }

    @Override
    public void process(T message) {
        ((Message)message).runMessage(user);
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }

}
