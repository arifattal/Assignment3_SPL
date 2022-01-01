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

    /**
     * @return gets a relevant string from the message structure
     */
    public abstract String prepareForString();

    /**
     * @return some message structures contain an additional short in addition to the opCode
     * this function returns that short which is used in EncoderDecoder
     */
    public abstract short getAdditionalBytes();

}
