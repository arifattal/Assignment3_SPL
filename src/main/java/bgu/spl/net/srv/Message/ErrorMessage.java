package bgu.spl.net.srv.Message;

public class ErrorMessage extends Message{
    private short messageOpcode;

    public ErrorMessage(short opCode, short messageOpcode){
        this.opCode = opCode;
        this.messageOpcode = messageOpcode;
    }

    @Override
    public Message runMessage() {
        System.out.println("Error " + messageOpcode);
        return this;
    }
}
