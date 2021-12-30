package bgu.spl.net.srv.Message;

import bgu.spl.net.srv.Data;
import bgu.spl.net.srv.User;

import java.util.Queue;

public class LoginMessage extends Message{
    private String userName;
    private String password;
    private short captcha;

    public LoginMessage(short opCode, String userName, String password, short captcha) {
        this.opCode = opCode;
        this.userName = userName;
        this.password = password;
        this.captcha = captcha;
    }

    @Override
    public Message runMessage(User user) {
        if (user.getStatus() != User.Status.loggedOut|| !user.getPassword().equals(password) || captcha == 0){
            Message error = new ErrorMessage((short) 11, this.opCode);
            return error;
        }
        else{
            user.setStatus(User.Status.loggedIn);
            Data.getInstance().incDecLoggedInUsers(1); //increment num of logged in users by 1
            Queue<NotificationMessage> notifications = user.getNotificationsQueue(); //send notifications that have been sent to the user while he was logged out
            while (!notifications.isEmpty()){
                notifications.poll().runMessage(user);
            }
            Message ack = new ACKmessage<>((short) 10, this.opCode, "");
            return ack;
        }
    }
}
