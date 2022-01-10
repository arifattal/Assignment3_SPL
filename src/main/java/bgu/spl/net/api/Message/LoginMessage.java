package bgu.spl.net.api.Message;

import bgu.spl.net.api.Data;
import bgu.spl.net.api.User;

import java.util.Queue;

public class LoginMessage extends Message{
    private String userName;
    private String password;
    private char captcha;

    public LoginMessage(short opCode, String userName, String password, char captcha) {
        this.opCode = opCode;
        this.userName = userName;
        this.password = password;
        this.captcha = captcha;
    }

    @Override
    public void runMessage(User user, int connectionId) {
        if (user.getStatus() != User.Status.loggedOut|| !user.getPassword().equals(password) || captcha == '0'){
            Message error = new ErrorMessage((short) 11, this.opCode);
            connections.send(connectionId, error);
        }
        else{
            data.connectUserToConId(user, connectionId);//connects user to connection Id if he doesn't have one, this is used for logging in after a logout
            user.setStatus(User.Status.loggedIn);
            Data.getInstance().incDecLoggedInUsers(1); //increment num of logged in users by 1
            Queue<NotificationMessage> notifications = user.getNotificationsQueue(); //send notifications that have been sent to the user while he was logged out
            while (!notifications.isEmpty()){
                notifications.poll().runMessage(user, connectionId);
            }
            Message ack = new ACKmessage<>((short) 10, this.opCode, "");
            ack.runMessage(user, connectionId);
            connections.send(connectionId, ack);
        }
    }

    public String getUserName(){
        return userName;
    }

    @Override
    public String prepareForString() {
        String str = userName + " " + password + " ";
        return str;
    }

    public char getCaptcha(){
        return captcha;
    }

    @Override
    public short getAdditionalBytes() {
        return 0;
    }


}
