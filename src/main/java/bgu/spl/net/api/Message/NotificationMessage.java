package bgu.spl.net.api.Message;

import bgu.spl.net.api.User;

//note that a notification doesn't send an ACK
public class NotificationMessage extends Message{
    private char NotificationType;
    String PostingUser;
    String Content;

    public NotificationMessage(short opCode, char notificationType, String postingUser, String content) {
        this.opCode = opCode;
        NotificationType = notificationType;
        PostingUser = postingUser;
        Content = content;
    }

    @Override
    public void runMessage(User user, int connectionId) {
        if (user.getStatus() != User.Status.loggedIn){ //if the user we would like to send a notification to is logged out save the notification to his notification queue
            user.addNotification(this);
        }
        else {
            connections.send(connectionId, this); //sending is done inside the run function, since we would like to send it only if the user is logged in
//            if (NotificationType == 0)
//                System.out.println("NOTIFICATION " + "PM " + PostingUser + " " + Content);
//            else
//                System.out.println("NOTIFICATION " + "Public " + PostingUser + " " + Content);
        }
    }

    @Override
    public String prepareForString() {
        String str = NotificationType + " " + PostingUser + " " + Content;
        return str;
    }

    public char getNotificationType() {
        return NotificationType;
    }

    public String getPostingUser() {
        return PostingUser;
    }

    public String getContent() {
        return Content;
    }

    @Override
    public short getAdditionalBytes() {
        return 0;
    }


}
