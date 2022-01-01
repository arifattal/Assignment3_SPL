package bgu.spl.net.api;

import bgu.spl.net.bidi.BidiMessagingProtocol;
import bgu.spl.net.bidi.Connections;
import bgu.spl.net.api.Message.Message;

public class Protocol<T> implements BidiMessagingProtocol<T> {
    private User user;
    private Integer connectionId = null;
    private Connections connections = null;
    private boolean shouldTerminate = false;

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
        return shouldTerminate;
    }

    public void setShouldTerminate(boolean set){
        shouldTerminate = set;
    }

}
