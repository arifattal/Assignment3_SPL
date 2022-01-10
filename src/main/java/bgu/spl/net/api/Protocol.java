package bgu.spl.net.api;

import bgu.spl.net.api.Message.LoginMessage;
import bgu.spl.net.bidi.BidiMessagingProtocol;
import bgu.spl.net.bidi.Connections;
import bgu.spl.net.api.Message.Message;
import org.w3c.dom.CDATASection;

public class Protocol<T> implements BidiMessagingProtocol<T> {
    private User user;
    private Integer connectionId = null;
    private Connections connections = null;
    private boolean shouldTerminate = false;
    private Data data = Data.getInstance();

    @Override
    public void start(int connectionId, Connections connections) {
        this.connectionId = connectionId;
        this.connections = connections;
        //the user is initialized here for convenience, and to avoid nullptrException. this way we don't need to keep making sure that user!=null
        user = new User("placeTaker", "placeTaker", "placeTaker");
    }

    @Override
    public void process(T message) {
        //the protocol holds the user's instance. if the user has been logged out and wants to log in again(through a different client) we need to update this instance of the user
        if (message instanceof LoginMessage){
            if (user.getUserName().equals("placeTaker")){ //meaning this protocol hasn't preformed a register, but wants to login
                String requestedUserName = ((LoginMessage)message).getUserName();
                User requestedUser = data.getUser(requestedUserName); //the user instance of which we would like to login into
                this.user = requestedUser;
            }
        }
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
