package bgu.spl.net.srv;

import bgu.spl.net.srv.Message.Message;

import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.net.srv.bidi.ConnectionHandler;

public class ConnectionsImpl implements bgu.spl.net.bidi.Connections {

    ConcurrentHashMap<Integer, ConnectionHandler> connectionsHM;

    @Override
    public boolean send(int connectionId, Object msg) {
        ConnectionHandler handler = connectionsHM.get(connectionId);
        if (handler == null || !(msg instanceof Message)){
            return false;
        }
        else {
            handler.send((Message) msg);
            return true;
        }
    }

    @Override
    public void broadcast(Object msg) {

    }

    @Override
    public void disconnect(int connectionId) {

    }
}
