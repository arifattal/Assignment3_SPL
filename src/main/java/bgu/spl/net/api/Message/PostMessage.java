package bgu.spl.net.api.Message;

import bgu.spl.net.api.User;

import java.util.ArrayList;
import java.util.List;

public class PostMessage extends Message{
    private String content;

    public PostMessage(short opCode, String content) {
        this.opCode = opCode;
        this.content = content;
    }

    public static List<String> createTaggedList(String str){ //an auxiliary function used to separate the names tagged in the post
        List<String> taggedUsers = new ArrayList<>();
        String accumulator = ""; //accumulates names once a '@' is encountered
        boolean accumulate = false;
        for (int i = 0; i< str.length(); i++){
            char character = str.charAt(i);
            if (accumulate && (character != ' ')){ //accumulate while accumulate is true and the char is not " "
                accumulator = accumulator + character;
            }
            if (character == '@'){ //start accumulating
                accumulate = true;
            }
            if (character == ' ' && accumulate){ //stop accumulating, the second condition stops the function from adding garbage to the list
                accumulate = false;
                taggedUsers.add(accumulator);
                accumulator = ""; //reset accumulator
            }
        }
        if (accumulate){ //this is used to deal with cases where the last "word" is a tag, for example "@moshe and @alina". since in this case we don't enter the third if statement which searches for spaces
            taggedUsers.add(accumulator);
        }
        return taggedUsers;
    }

    @Override
    public void runMessage(User user, int connectionId) {
        List<User> sendNotificationList = new ArrayList<>(); //an array containing the userNames we want to send a notification to
        List<String> taggedList = new ArrayList<>(); //an array containing userNames tagged in the post
        if (user.getStatus() != User.Status.loggedIn){
            Message error = new ErrorMessage((short) 11, this.opCode);
            connections.send(connectionId, error);
        }
        else{
            for (String element: user.getFollowersList()){//add the user's followers to sendNotificationList
                User user1 = data.getUser(element);
                if (user1 != null && user1.getStatus() != User.Status.unRegistered){ //checking for blocking isn't necessary here since user1 won't be following user
                    sendNotificationList.add(user1);
                }
            }
            taggedList = createTaggedList(content); //uses the auxiliary function to receive a list of names tagged in the post
            for (String element: taggedList){//add tagged users to sendNotificationList
                User user1 = data.getUser(element);
                if (user1 != null && user1.getStatus() != User.Status.unRegistered && !user1.isBlocking(user.getUserName())){
                    if (!sendNotificationList.contains(user1)){ //add the user tagged if he isn't already in sendNotificationList
                        sendNotificationList.add(user1);
                    }
                }
            }
            data.addPost_pm(this); //add post to the post_pm list
            for (User user1: sendNotificationList){ //user1 is the user we want to send a notification to
                NotificationMessage notification = new NotificationMessage((short)9, (char)1, user.getUserName(), content);
                if (user1.getStatus() == User.Status.loggedOut){
                    user1.addNotification(notification);
                }
                else{
                    int user1ClientId = data.getUserClientId(user1); //this gets the client id that represents user1
                    notification.runMessage(user1, user1ClientId); //running a notification message is different to other messages. here the user sent is the "other user"
                }
            }
        }
        Message ack = new ACKmessage<>((short) 10, this.opCode, ""); //the pdf doesn't state that an ack needs to be sent but it appears in the example on page 17
        connections.send(connectionId, ack);
    }

    @Override
    public String prepareForString() {
        String str = content;
        return str;
    }

    @Override
    public short getAdditionalBytes() {
        return 0;
    }


}
