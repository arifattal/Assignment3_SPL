package bgu.spl.net.api.Message;

import bgu.spl.net.api.User;

public class ACKmessage<T> extends Message{

    /**
     * this sub class is used for creating the ack message for LogStat and Stat which is in a different structure
     */
    public static class ShortOptional{
        short[][] shortArray;

        ShortOptional(short[][] shortArray){
            this.shortArray = shortArray;
        }

        @Override
        public String toString(){
            String string = " ";
            for (int i = 0; i<shortArray.length; i++){
                for (int j = 0; j<shortArray[i].length; j++){
                    string = string + shortArray[i][j] + " ";
                }
                string = string + '\n';
            }
            return string;
        }
    }

    private short messageOpcode;
    private T optional;

    public ACKmessage(short opCode, short messageOpcode, T optional) {
        this.opCode = opCode;
        this.messageOpcode = messageOpcode;
        this.optional = optional;
    }

    @Override
    public Message runMessage(User user) {
        //the ACKMessage is printed differently for LOGSTAT and STAT. mainly because of the print structure seen in the else, which adds '"ACK " + messageOpcode'
        if (optional instanceof ShortOptional){
            String string = optional.toString();
            System.out.println(string);
        }
        else{
        System.out.println("ACK " + messageOpcode + optional.toString());
        }
        return this;
    }

    @Override
    public String prepareForString() {
        return (optional.toString());
    }

    @Override
    public short getAdditionalBytes() {
        return messageOpcode;
    }

    public ShortOptional getShortOptional(){
        return (ShortOptional)optional;
    }
}
