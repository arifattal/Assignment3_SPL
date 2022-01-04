package bgu.spl.net.srv;

import bgu.spl.net.api.Data;
import bgu.spl.net.api.Message.Message;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.net.bidi.Connections;
import bgu.spl.net.srv.bidi.ConnectionHandler;

public class ConnectionsImpl implements Connections {

    private ConcurrentHashMap<Integer, ConnectionHandler> connectionsHM; //maps between a connectionId and a connection handler
    private int provideConId;

    //implement connectionImpl as a singleton
    private static class ConnectionsImplHolder{
        private static ConnectionsImpl instance = new ConnectionsImpl();
    }
    public static ConnectionsImpl getInstance(){
        return ConnectionsImplHolder.instance;
    }

    private ConnectionsImpl(){
        connectionsHM = new ConcurrentHashMap<>();
        provideConId = 0;
    }

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
        for (ConnectionHandler handler : connectionsHM.values()){
            boolean isMessage = (msg instanceof Message);
            if (handler != null && isMessage){
                handler.send((Message) msg);
            }
        }
    }

    @Override
    public void disconnect(int connectionId) {
        connectionsHM.remove(connectionId);
    }

    public void addConnection(int connectionId, ConnectionHandler handler){
        connectionsHM.put(connectionId, handler);
    }

    public int getNewConnectionId(){
        provideConId ++;
        return provideConId;
    }
}
