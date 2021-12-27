package bgu.spl.net.srv.Message;

import bgu.spl.net.srv.User;

public class ACKmessage<T> extends Message{
    private short messageOpcode;
    private T optional;

    public ACKmessage(short opCode, short messageOpcode, T optional) {
        this.opCode = opCode;
        this.messageOpcode = messageOpcode;
        this.optional = optional;
    }

    @Override
    public Message runMessage(User user) {
        System.out.println("ACK " + messageOpcode + optional.toString());
        return this;
    }
}
