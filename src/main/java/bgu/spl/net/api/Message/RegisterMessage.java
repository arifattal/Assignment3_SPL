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
    public void runMessage(User user, int connectionId) {
        if (!data.isRegistered(username) && !data.handlerHasUser(connectionId)){ //create new user if not registered, and if the connection handler doesn't have a user, and add him to our data structure
            user.setUserName(username);
            user.setPassword(password);
            user.setBirthday(birthday); //this also sets the correct age
            user.setStatus(User.Status.loggedOut);

            data.RegisterUser(user, connectionId);
            Message ack = new ACKmessage<>((short) 10, this.opCode, "");
            ack.runMessage(user, connectionId);
            connections.send(connectionId, ack);
        }
        else{
            Message error = new ErrorMessage((short) 11, this.opCode);
            connections.send(connectionId, error);
        }
    }

    @Override
    public String prepareForString() {
        String str = username + " " +  password + " " + birthday;
        return str;
    }

    @Override
    public short getAdditionalBytes() {
        return 0;
    }


}
