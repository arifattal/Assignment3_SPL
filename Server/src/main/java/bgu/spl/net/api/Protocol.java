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
        //the user is initialized here for convenience, and to avoid nullptrException. this way we don't need to keep making sure that user!=null
        user = new User("placeTaker", "placeTaker", "placeTaker");
    }

    @Override
    public void process(T message) {
        ((Message)message).runMessage(user, this.connectionId);
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

    public void setShouldTerminate(boolean set){
        shouldTerminate = set;
    }

}
