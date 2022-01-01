package bgu.spl.net.api.Message;

import bgu.spl.net.api.User;

public class RegisterMessage extends Message{

    private String username;
    private String password;
    private String birthday;

    public RegisterMessage(short opCode, String username, String password, String birthday) {
        this.opCode = opCode;
        this.username = username;
        this.password = password;
        this.birthday = birthday;
    }

    @Override
    public Message runMessage(User user) {
        if (!data.isRegistered(username)){ //create new user if not registered, and add him to our data structure
            user = new User(username, password, birthday);
            data.RegisterUser(user);
            Message ack = new ACKmessage<>((short) 10, this.opCode, "");
            return ack;
        }
        else{
            Message error = new ErrorMessage((short) 11, this.opCode);
            return error;
        }
    }

    @Override
    public String prepareForString() {
        String str = username + " " +  password + " " + birthday + ';';
        return str;
    }

    @Override
    public short getAdditionalBytes() {
        return 0;
    }


}
