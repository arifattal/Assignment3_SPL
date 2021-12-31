package bgu.spl.net.api.Message;

import bgu.spl.net.api.Data;
import bgu.spl.net.api.User;

public abstract class Message {

    protected short opCode;
    protected Data data = Data.getInstance();
    public abstract Message runMessage(User user);
    public short getOpCode(){
        return opCode;
    }
}
