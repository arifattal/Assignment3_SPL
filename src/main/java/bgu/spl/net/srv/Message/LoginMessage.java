package bgu.spl.net.srv.Message;

import bgu.spl.net.srv.User;

public class LoginMessage extends Message{
    private String userName;
    private String password;
    private short captcha;

    public LoginMessage(short opCode, String userName, String password, short captcha) {
        this.opCode = opCode;
        this.userName = userName;
        this.password = password;
        this.captcha = captcha;
    }

    @Override
    public Message runMessage() {
        User user = data.getUser(userName);
        if (user == null || !user.getPassword().equals(password) || user.getStatus() == User.Status.loggedIn || captcha == 0){
            Message error = new ErrorMessage((short) 11, this.opCode);
            return error;
        }
        else{
            user.setStatus(User.Status.loggedIn);
            Message ack = new ACKmessage<>((short) 10, this.opCode, "");
            return ack;
        }
    }
}
