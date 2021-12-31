package bgu.spl.net.api.Message;

import bgu.spl.net.api.User;

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
            }
            //Once user blocking acknowledged, both users (the blocking and the blocked) stop following each other
            //therefore more information is added to the ack message
            String[] optional = new String[2];
            optional[0] = user.getUserName();
            optional[1] = userName;
            Message ack = new ACKmessage<>((short) 10, this.opCode, optional);
            return ack;
        }
    }
}
