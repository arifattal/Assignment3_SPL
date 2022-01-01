package bgu.spl.net.api.Message;

import bgu.spl.net.api.Data;
import bgu.spl.net.api.User;

public class LogOutMessage extends Message{

    public LogOutMessage(short opCode) {
        this.opCode = opCode;
    }

    @Override
    public Message runMessage(User user) {
        if (user.getStatus() != User.Status.loggedIn){
            Message error = new ErrorMessage((short) 11, this.opCode);
            return error;
        }
        else{
            user.setStatus(User.Status.loggedOut);
            Data.getInstance().incDecLoggedInUsers(-1); //decrement num of logged in users by 1
            Message ack = new ACKmessage<>((short) 10, this.opCode, user.getUserName());
            return ack;
        }
    }

    @Override
    public String prepareForString() {
        return "";
    }

    @Override
    public short getAdditionalBytes() {
        return 0;
    }


}
