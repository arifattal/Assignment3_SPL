package bgu.spl.net.srv.Message;

import bgu.spl.net.srv.User;

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
