package bgu.spl.net.api.Message;

import bgu.spl.net.api.User;

public class BlockMessage extends Message{
    String userName;
    String forString = ""; //this was put here as a work around for encoding. used in the prepareForString function

    public BlockMessage(short opCode, String userName) {
        this.opCode = opCode;
        this.userName = userName;
    }

    @Override
    public void runMessage(User user, int connectionId) {
        User otherUser = data.getUser(userName);
        if (otherUser == null){
            Message error = new ErrorMessage((short) 11, this.opCode);
            connections.send(connectionId, error);
        }
        else{
            if (!user.isBlocking(userName)){ //proceed if the user isn't already blocking otherUser
                user.block(userName);
            }
            //Once user blocking acknowledged, both users (the blocking and the blocked) stop following each other
            //therefore more information is added to the ack message
            String[] optional = new String[2];
            optional[0] = user.getUserName();
            optional[1] = userName;
            //forString = user.getUserName() + " " + userName;
            Message ack = new ACKmessage<>((short) 10, this.opCode, optional);
            connections.send(connectionId, ack);
        }
    }

    @Override
    public String prepareForString() {
        return forString;
    }

    @Override
    public short getAdditionalBytes() {
        return 0;
    }


}
