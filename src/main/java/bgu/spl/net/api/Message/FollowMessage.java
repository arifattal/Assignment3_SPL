package bgu.spl.net.api.Message;

import bgu.spl.net.api.User;


public class FollowMessage extends Message{
    private char follow;
    private String userName;

    public FollowMessage(short opCode, char follow, String userName) {
        this.opCode = opCode;
        this.follow = follow;
        this.userName = userName;
    }

    @Override
    public void runMessage(User user, int connectionId) {
        User otherUser = data.getUser(userName);
        System.out.println("is user following the other user? " + user.isFollowing(userName));
        System.out.println("user's status is: " + user.getStatus());
        System.out.println("other user's address is: " + otherUser);
        if (follow == '0') { //follow
            System.out.println("entered following statement ");
            if (otherUser == null || user.getStatus() != User.Status.loggedIn || user.isFollowing(userName)){
                Message error = new ErrorMessage((short) 11, this.opCode);
                System.out.println("error created");
                connections.send(connectionId, error);
            }
            else{
                if (!otherUser.isBlocking(user.getUserName())){ //follow otherUser if he isn't blocking me
                    user.follow(userName);
                    otherUser.addFollower(user.getUserName());
                }
                Message ack = new ACKmessage<>((short) 10, this.opCode, userName);
                connections.send(connectionId, ack);
            }
        }
        else{ //unfollow
            if (otherUser == null || user.getStatus() != User.Status.loggedIn || !user.isFollowing(userName)){
                Message error = new ErrorMessage((short) 11, this.opCode);
                connections.send(connectionId, error);
            }
            else{
                user.unFollow(userName);
                otherUser.removeFollower(user.getUserName());
                Message ack = new ACKmessage<>((short) 10, this.opCode, userName);
                connections.send(connectionId, ack);
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
        return 0;
    }


}
