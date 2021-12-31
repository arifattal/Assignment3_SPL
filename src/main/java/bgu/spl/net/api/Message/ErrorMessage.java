package bgu.spl.net.api.Message;

import bgu.spl.net.api.User;

public class ErrorMessage extends Message{
    private short messageOpcode;

    public ErrorMessage(short opCode, short messageOpcode){
        this.opCode = opCode;
        this.messageOpcode = messageOpcode;
    }

    @Override
    public Message runMessage(User user) {
        System.out.println("Error " + messageOpcode);
        return this;
    }
}
