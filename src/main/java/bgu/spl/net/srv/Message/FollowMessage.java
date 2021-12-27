package bgu.spl.net.srv.Message;

import bgu.spl.net.srv.User;


public class FollowMessage extends Message{
    private short follow;
    private String userName;

    public FollowMessage(short opCode, short follow, String userName) {
        this.opCode = opCode;
        this.follow = follow;
        this.userName = userName;
    }

    @Override
    public Message runMessage(User user) {
        User otherUser = data.getUser(userName);
        if (follow == 0) {
            if (otherUser == null || user.getStatus() != User.Status.loggedIn || user.isFollowing(userName)){
                Message error = new ErrorMessage((short) 11, this.opCode);
                return error;
            }
            else{
                user.follow(userName);
                otherUser.addFollower(user.getUserName());
                Message ack = new ACKmessage<>((short) 10, this.opCode, userName);
                return ack;
            }
        }
        else{ //follow == 1
            if (user.getStatus() != User.Status.loggedIn || !user.isFollowing(userName)){
                Message error = new ErrorMessage((short) 11, this.opCode);
                return error;
            }
            else{
                user.unFollow(userName);
                otherUser.removeFollower(user.getUserName());
                Message ack = new ACKmessage<>((short) 10, this.opCode, userName);
                return ack;
            }
        }

    }
}
