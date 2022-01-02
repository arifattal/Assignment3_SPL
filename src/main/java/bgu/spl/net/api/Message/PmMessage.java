package bgu.spl.net.api.Message;

import bgu.spl.net.api.User;

import java.util.List;

public class PmMessage extends Message {
    String userName;
    String content;
    String sendTime;

    public PmMessage(short opCode, String userName, String content, String sendTime) {
        this.opCode = opCode;
        this.userName = userName;
        this.content = content;
        this.sendTime = sendTime;
    }

    public static String filterWords(String str, List<String> words){ //an auxiliary function used to filter out words from PM's
        for (int i = 0; i< words.size(); i++){
            str = str.replace(words.get(i), "<filtered>");
        }
        return str;
    }

    @Override
    public Message runMessage(User user) {
        if (user.getStatus() != User.Status.loggedIn){
            Message error = new ErrorMessage((short) 11, this.opCode);
            return error;
        }
        User otherUser = data.getUser(userName);
        if (otherUser == null || otherUser.getStatus() == User.Status.unRegistered || otherUser.isBlocking(user.getUserName())){
            Message error = new ErrorMessage((short) 11, this.opCode);
            return error;
        }
        else{
            List<String> filteredWords = data.getFilteredWords();
            content = filterWords(content, filteredWords); //filter out banned words
            data.addPost_pm(this); //add post to the post_pm list
            NotificationMessage notification = new NotificationMessage((short)9, (short)0, user.getUserName(), content);
            notification.runMessage(otherUser); //running a notification message is different to other messages. here the user sent is the "other user"
        }
        Message ack = new ACKmessage<>((short) 10, this.opCode, ""); //the pdf doesn't state that an ack needs to be sent but it appears in the example on page 17
        return ack;
    }

    @Override
    public String prepareForString() {
        String str = userName + " " +  content + " " + sendTime;
        return str;
    }

    @Override
    public short getAdditionalBytes() {
        return 0;
    }


}
