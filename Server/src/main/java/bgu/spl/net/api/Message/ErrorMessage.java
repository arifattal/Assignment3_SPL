package bgu.spl.net.api.Message;

import bgu.spl.net.api.User;

public class ErrorMessage extends Message{
    private short messageOpcode;

    public ErrorMessage(short opCode, short messageOpcode){
        this.opCode = opCode;
        this.messageOpcode = messageOpcode;
    }

    @Override
    public void runMessage(User user, int connectionId) {
        System.out.println("Error " + messageOpcode);
    }

    @Override
    public String prepareForString() {
        return ";";
    }

    @Override
    public short getAdditionalBytes() {
        return messageOpcode;
    }


}
