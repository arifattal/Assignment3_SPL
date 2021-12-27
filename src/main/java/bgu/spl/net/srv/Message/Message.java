package bgu.spl.net.srv.Message;

import bgu.spl.net.srv.Data;
import bgu.spl.net.srv.User;

public abstract class Message {

    protected short opCode;
    protected Data data = Data.getInstance();
    public abstract Message runMessage(User user);
}
