package bgu.spl.net.srv.Message;

import bgu.spl.net.srv.Data;
import bgu.spl.net.srv.User;

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
            Message ack = new ACKmessage<>((short) 10, this.opCode, "");
            return ack;
        }
    }
}
