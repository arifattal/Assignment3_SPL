package bgu.spl.net.srv.Message;

import bgu.spl.net.srv.User;

public class registerMessage extends Message{

    private String username;
    private String password;
    private String birthday;

    @Override
    public Message runMessage() {
        if (!data.isRegistered(username)){ //create new user if not registered, and add him to our data structure
            User user = new User(username, password, birthday);
            data.RegisterUser(user);
            Message ack = new ACKmessage<>((short) 10, this.opCode, "");
            return ack;
        }
        else{
            Message error = new ErrorMessage((short) 11, this.opCode);
            return error;
        }
    }

    public registerMessage(short opCode, String username, String password, String birthday) {
        this.opCode = opCode;
        this.username = username;
        this.password = password;
        this.birthday = birthday;
    }
}
