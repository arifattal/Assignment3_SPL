package bgu.spl.net.api.Message;

import bgu.spl.net.api.User;

public class ACKmessage<T> extends Message{

    /**
     * this sub class is used for creating the ack message for LogStat and Stat which is in a different structure
     */
//    public static class ShortOptional{
//        short[] shortArray;
//
//        ShortOptional(short[] shortArray){
//            this.shortArray = shortArray;
//        }
//
//        @Override
//        public String toString(){
//            String string = " ";
//            for (int i = 0; i<shortArray.length; i++){
//                for (int j = 0; j<shortArray[i].length; j++){
//                    string = string + shortArray[i][j] + " ";
//                }
//                string = string + '\n';
//            }
//            return string;
//        }
//    }

    private short messageOpcode;
    private T optional;

    public ACKmessage(short opCode, short messageOpcode, T optional) {
        this.opCode = opCode;
        this.messageOpcode = messageOpcode;
        this.optional = optional;
    }

    @Override
    public void runMessage(User user, int connectionId) {
        //the ACKMessage is printed differently for LOGSTAT and STAT. mainly because of the print structure seen in the else, which adds '"ACK " + messageOpcode'
        if (optional instanceof short[]){
            String string = optional.toString();
            System.out.println(string);
        }
        else{
        System.out.println("ACK " + messageOpcode + optional.toString());
        }
    }

    @Override
    public String prepareForString() {
        switch(messageOpcode){
            case(1): case(2): case(3): case(5): case(6):{
                return "";}
            case(4):{
                return " " + (String)optional;
            }
            case(7): case(8):{
                short[] optionalArray = (short[])optional;
                String str = "";
                if (optionalArray.length>3){
                    str = " " + optionalArray[0] + " " + optionalArray[1] + " " + optionalArray[2] + " " +  optionalArray[3];
                }
                return str;
            }
            case(12):{
                String[] optionalArray = (String[])optional;
                String str = " ";
                if (optionalArray.length > 1) {
                    str = str + optionalArray[0] + " " + optionalArray[1];
                }
                return str;
            }
        }
        return "";
    }

    @Override
    public short getAdditionalBytes() {
        return messageOpcode;
    }

//    public ShortOptional getShortOptional(){
//        return (ShortOptional)optional;
//    }

    public T getOptional() {
        return optional;
    }
}
