package bgu.spl.net.srv.Message;

import bgu.spl.net.srv.Data;

public abstract class Message {

    protected short opCode;
    protected Data data = Data.getInstance();
    public abstract Message runMessage();
}
