package bgu.spl.net.api.Message;

import bgu.spl.net.api.Data;
import bgu.spl.net.api.User;
import bgu.spl.net.srv.ConnectionsImpl;

public abstract class Message {

    protected short opCode;
    protected Data data = Data.getInstance();
    protected ConnectionsImpl connections = ConnectionsImpl.getInstance();

    public abstract void runMessage(User user, int connectionId);
    public short getOpCode(){
        return opCode;
    }

    /**
     * @return gets a relevant string from the message structure
     */
    public abstract String prepareForString(); //as for now we decided not to use this function, can be deleted if we see no use for it

    /**
     * @return some message structures contain an additional short in addition to the opCode
     * this function returns that short which is used in EncoderDecoder
     */
    public abstract short getAdditionalBytes();

}
