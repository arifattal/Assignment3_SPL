package bgu.spl.net.bidi;

import java.io.IOException;

public interface Connections<T> {
    /**
     *sends a message T to client represented by the given connId
     */
    boolean send(int connectionId, T msg);

    /**
     *  sends a message T to all active clients. This includes clients that has not yet completed log-in by the BGS protocol.
     *  Remember, Connections belongs to the server pattern implementation, not the protocol!
     */
    void broadcast(T msg);

    /**
     * removes active client connId from map
     */
    void disconnect(int connectionId);
}
