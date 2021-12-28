package bgu.spl.net.srv.Message;

import bgu.spl.net.srv.User;

public class BlockMessage extends Message{
    String userName;

    public BlockMessage(short opCode, String userName) {
        this.opCode = opCode;
        this.userName = userName;
    }

    @Override
    public Message runMessage(User user) {
        User otherUser = data.getUser(userName);
        if (otherUser == null){
            Message error = new ErrorMessage((short) 11, this.opCode);
            return error;
        }
        else{
            if (!user.isBlocking(userName)){ //proceed if the user isn't already blocking otherUser
                user.block(userName);
                user.unFollow(userName);
                user.removeFollower(userName);
                otherUser.unFollow(user.getUserName());
                otherUser.removeFollower(user.getUserName());
            }
            Message ack = new ACKmessage<>((short) 10, this.opCode, "");
            return ack;
        }
    }
}
