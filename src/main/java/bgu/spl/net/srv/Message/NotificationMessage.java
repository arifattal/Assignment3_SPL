package bgu.spl.net.srv.Message;

import bgu.spl.net.srv.User;

public class NotificationMessage extends Message{
    private int NotificationType;
    String PostingUser;
    String Content;


    @Override
    public Message runMessage(User user) {
        return null;
    }
}
