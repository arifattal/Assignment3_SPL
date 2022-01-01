package bgu.spl.net.api.Message;

import bgu.spl.net.api.User;


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
        if (follow == 0) { //follow
            if (otherUser == null || user.getStatus() != User.Status.loggedIn || user.isFollowing(userName)){
                Message error = new ErrorMessage((short) 11, this.opCode);
                return error;
            }
            else{
                if (!otherUser.isBlocking(user.getUserName())){ //follow otherUser if he isn't blocking me
                    user.follow(userName);
                    otherUser.addFollower(user.getUserName());
                }
                Message ack = new ACKmessage<>((short) 10, this.opCode, userName);
                return ack;
            }
        }
        else{ //unfollow
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

    @Override
    public String prepareForString() {
        String str = userName + ';';
        return str;
    }

    @Override
    public short getAdditionalBytes() {
        return follow;
    }


}
