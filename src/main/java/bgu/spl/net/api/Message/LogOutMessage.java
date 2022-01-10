package bgu.spl.net.api.Message;

import bgu.spl.net.api.Data;
import bgu.spl.net.api.User;

public class LogOutMessage extends Message{

    public LogOutMessage(short opCode) {
        this.opCode = opCode;
    }

    @Override
    public void runMessage(User user, int connectionId) {
        if (user.getStatus() != User.Status.loggedIn){
            Message error = new ErrorMessage((short) 11, this.opCode);
            connections.send(connectionId, error);
        }
        else{
            user.setStatus(User.Status.loggedOut);
            data.incDecLoggedInUsers(-1); //decrement num of logged in users by 1
            Message ack = new ACKmessage<>((short) 10, this.opCode, user.getUserName());
            connections.send(connectionId, ack);
            data.logOutUser(user); //this preforms a number of actions, such as disconnecting the handler. look at the function for more information
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
