package bgu.spl.net.srv;

import java.io.Closeable;
import java.io.IOException;

public interface ConnectionHandler<T> extends Closeable{
    /**
     * sends msg T to the client. Should be used by send and broadcast in the Connections implementation.
     */
    void send(T msg) ;

}
