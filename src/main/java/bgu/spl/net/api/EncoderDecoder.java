package bgu.spl.net.api;

import bgu.spl.net.api.Message.*;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class EncoderDecoder<T> implements MessageEncoderDecoder<T>{

    private byte[] bytes = new byte[1 << 10];
    private int len = 0;

    @Override
    public T decodeNextByte(byte nextByte) {
        if (nextByte == ';') {
            String messageReceived = new String(bytes, 0, len, StandardCharsets.UTF_8);
            String stringArray[] = messageReceived.split(" ");
            if (stringArray[0].charAt(0) == '\n'){
                stringArray[0] = stringArray[0].substring(1);
            }
//            for (String str : stringArray){
//                System.out.println(str);
//            }
            Message msgObject = getMessageObj(stringArray);
            //Message msgObject = getMessageObj(bytes);
            len = 0; //this is our method of clearing the bytes array. If it causes issues for some reason create a new bytes array instead
            return (T) msgObject;
        }
        pushByte(nextByte); //this function adds bytes
        return null;
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }
        bytes[len++] = nextByte;
    }

    private Message getMessageObj(String[] stringArray){
        if (stringArray != null){
            Message message;
            String msgType=stringArray[0];
            //1
            if (msgType.equals("REGISTER")){
                //System.out.println("register message received, nice (:");
                short opCode = 1;
                String username = stringArray[1];
                String pass = stringArray[2];
                String bDay = stringArray[3];
                message = new RegisterMessage(opCode, username, pass, bDay);
                return message;
            }
            //2
            else if(msgType.equals("LOGIN")){
                short opCode = 2;
                String username = stringArray[1];
                String pass = stringArray[2];
                char captcha = stringArray[3].charAt(0);
                message = new LoginMessage(opCode, username, pass, captcha);
                return message;
            }
            //3
            else if(msgType.equals("LOGOUT")){
                short opCode = 3;
                message = new LogOutMessage(opCode);
                return message;
            }
            //4
            else if(msgType.equals("FOLLOW")){
                short opCode = 4;
                char follow = stringArray[1].charAt(0);
                String username = stringArray[2];
                message = new FollowMessage(opCode, follow, username);
                return message;
            }
            //5
            else if(msgType.equals("POST")){
                short opCode = 5;
                String pstMsg = stringChain(stringArray,1);
                message = new PostMessage(opCode,pstMsg);
                return message;
            }
            //6
            else if(msgType.equals("PM")){
                short opCode = 6;
                String username = stringArray[1];
                String content = stringChain(stringArray,2);
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String str = formatter.format(date);
                message = new PmMessage(opCode, username, content, str);
                return message;
            }
            //7
            else if(msgType.equals("LOGSTAT")){
                short opCode = 7;
                message = new LogStatMessage(opCode);
                return message;
            }
            //8
            else if(msgType.equals("STAT")){
                short opCode = 8;
                String usernames = stringArray[1];
                message = new StatMessage(opCode, usernames);
                return message;
            }
            //9
            else if(msgType.equals("NOTIFICATION")){
                short opCode = 9;
                char type = stringArray[1].charAt(0);
                String pstUser = stringArray[2];
                String content = stringArray[3];
                message = new NotificationMessage(opCode, type, pstUser, content);
                return message;
            }
            //12
            else if(msgType.equals("BLOCK")){
                short opCode = 12;
                String usernames = stringArray[1];
                message = new BlockMessage(opCode, usernames);
                return message;
            }
        }
        return null;
    }
    //---------------------------------------------------
    private String stringChain (String [] stringArr,int startIndex){
        if(stringArr!=null) {
            String ans = stringArr[startIndex];
            for (int i = startIndex+1; i < stringArr.length; i++) {
                ans += " " + stringArr[i];
            }
            return ans;
        }
        else return null;
    }


    private Message getMessageObj(byte[] bytes){
        if (bytes != null) {
            byte[] opCodeByte = {bytes[0], bytes[1]};
            short opCode = bytesToShort(opCodeByte);
            Message message;
            switch (opCode) {
                case (1): {
                    System.out.println("register message received, nice (:");
                    ArrayList<String> stringList = getStringArray(bytes, 2, 3);
                    String username = stringList.get(0);
                    String pass = stringList.get(1);
                    String bDay = stringList.get(2);
                    message = new RegisterMessage(opCode, username, pass, bDay);
                }
                case (2):{
                    ArrayList<String> stringList = getStringArray(bytes, 2, 2);
                    String username = stringList.get(0);
                    String pass = stringList.get(1);
                    String captchaString = new String(bytes, bytes.length-1, 1, StandardCharsets.UTF_8); //the captcha takes one byte and is at the last position
                    char captcha = captchaString.charAt(0);
                    message = new LoginMessage(opCode, username, pass, captcha);
                }
                case (3):{
                    message = new LogOutMessage(opCode);
                }
                case (4):{
                    byte[] followUnfollowByte = {bytes[2]}; //get the extra follow/unfollow byte
                    String followUnfollowString = new String(followUnfollowByte, 0, 1, StandardCharsets.UTF_8);
                    char followUnfollow = followUnfollowString.charAt(0);
                    ArrayList<String> stringList = getStringArray(bytes, 3, 1);
                    String userName = stringList.get(0);
                    message = new FollowMessage(opCode, followUnfollow, userName);
                }
                case (5):{
                    ArrayList<String> stringList = getStringArray(bytes, 2, 1);
                    String content = stringList.get(0);
                    message = new PostMessage(opCode, content);
                }
                case (6):{
                    ArrayList<String> stringList = getStringArray(bytes, 2, 3);
                    String userName = stringList.get(0);
                    String content = stringList.get(1);
                    String dateAndTime = stringList.get(2);
                    message = new PmMessage(opCode, userName, content, dateAndTime);
                }
                case (7):{
                    message = new LogStatMessage(opCode);
                }
                case (8):{
                    ArrayList<String> stringList = getStringArray(bytes, 2, 1);
                    String userNames = stringList.get(0);
                    message = new StatMessage(opCode, userNames);
                }
                case (12):{
                    ArrayList<String> stringList = getStringArray(bytes, 2, 1);
                    String userName = stringList.get(0);
                    message = new BlockMessage(opCode, userName);
                }
                return message;
            }
        }
        return null;
    }

    /**
     * this function is used to seperate the relevant strings for a Message object from an array of bytes
     *
     * @param bytes the decoded bytes array
     * @param startParse start parsing bytes from this index for ex. in the follow message the strings start at index 3 and in message at index 2
     * @param endParse end string parsing after collecting endParse strings. for ex. in the login message we would like to collect 2 strings
     * @return a list of relevant strings for creating a message object
     */
    public ArrayList<String> getStringArray(byte[] bytes, int startParse, int endParse){
        ArrayList<Integer> stringSizes = getStringInBytesSizes(bytes, startParse, endParse); //this array let's us initialize the byteArray with a set size. (unable to use A list due to the way a string is created from bytes)
        byte[][] byteArray = new byte[stringSizes.size()][]; //this array will hold the different string representation in bytes. stringSizes.size() gives us the num of strings for our Message objects

        for (int i = 0; i<stringSizes.size(); i++){
            byteArray[i] = new byte[stringSizes.get(i)]; //initialize 2D array
        }
        int j = 0; //outer index
        int t = 0; //inner index
        for (int i = startParse; i < bytes.length; i++) {
            if (bytes[i] != 0) {
                byteArray[j][t] = bytes[i];
                t++;
            }
            if (bytes[i] == 0) {
                j++;
                t = 0;//reset t
            }
        }
        ArrayList<String> stringList = new ArrayList<>(); //this array holds the different strings once converted from bytes
        for (int i = 0; i<byteArray.length; i++){
            String string = new String(byteArray[i], 0, byteArray[i].length, StandardCharsets.UTF_8);
            stringList.add(string);
        }
        return stringList;
    }

    /**
     * this is an auxiliary function for getStringArray()
     * the function is used to get the string representations in bytes.
     * for ex. "hi" in bytes could be 12 13 55, therefore it's size is 3.
     *
     * @param bytes the decoded bytes array
     * @param startParse start parsing bytes from this index for ex. in the follow message the strings start at index 3 and in message at index 2
     * @param endParse end string parsing after collecting endParse strings. for ex. in the login message we would like to collect 2 strings
     * @return a list containing the sizes of the string representations in bytes.
     */
    public ArrayList<Integer> getStringInBytesSizes(byte[] bytes, int startParse, int endParse){
        int strLength = 0;
        ArrayList<Integer> sizesArray = new ArrayList<>();
        for (int i = startParse; i < bytes.length && sizesArray.size()<endParse; i++) { //first two bytes are the opCode, therefore we start at i = 2
            if (bytes[i] != 0) {
                strLength++;
            }
            if (bytes[i] == 0) { //a 0 byte marks the end of the string
                sizesArray.add(strLength); //add length
                strLength = 0; //reset length for next string
            }
        }
        return sizesArray;
    }

    @Override
    public byte[] encode(T message){
        Message msg = (Message)message;
        switch (msg.getOpCode()){
            case(9): //notification
            {
                String str = "NOTIFICATION " + msg.prepareForString()+ '\n';
                return str.getBytes();
            }
            case(10):{ //ack
                String str = "ACK " + msg.getAdditionalBytes() + '\n';
                return str.getBytes();
            }
            case(11):{ //error
                String str = "ERROR " + msg.getAdditionalBytes() + '\n';
                return str.getBytes();
            }
            }
        return null;
    }

//    @Override
//    public byte[] encode(T message) {
//        Message msg = (Message)message; //used for ease due to need for casting
//        //String strForBytes = msg.prepareForString();
//        byte[] bytes1;
//        byte[] bytes2;
//        byte[] bytes3;
//        byte[] bytes4;
//        byte[] bytes5;
//        byte[] bytes6;
//        byte[] zeroByte = new byte[]{'\0'};
//
//        switch (msg.getOpCode()){
//            case(9):
//            {
//                bytes1 = shortToBytes(msg.getOpCode());
//                char notificationType = ((NotificationMessage)msg).getNotificationType();
//                bytes2 = new byte[]{(byte) notificationType};
//                bytes3 = mergeArrays(bytes1, bytes2);
//                String postingUser = ((NotificationMessage)msg).getPostingUser();
//                bytes4 = mergeArrays(postingUser.getBytes(), zeroByte);
//                String content = ((NotificationMessage)msg).getContent();
//                bytes5 = mergeArrays(content.getBytes(), zeroByte);
//                return mergeArrays(bytes3, bytes4, bytes5);
//            }
//
//            case(10):
//            {
//                switch (msg.getAdditionalBytes()){
//                    case(1): case(2): case(4): case(5): case(6): {
//                        //return "ack 1".getBytes();
//                        String strForBytes = (String) ((ACKmessage)msg).getOptional();
//                        bytes1 = shortToBytes(msg.getOpCode());
//                        bytes2 = shortToBytes(msg.getAdditionalBytes());
//                        bytes3 = (strForBytes).getBytes();
//                        bytes4 = mergeArrays(bytes1, bytes2);
//                        bytes5 = mergeArrays(bytes3, zeroByte);
//                        return mergeArrays(bytes4, bytes5);
//                    }
//                    case(7): case(8): { //stat messages
//                        bytes1 = shortToBytes(msg.getOpCode());
//                        bytes2 = shortToBytes(msg.getAdditionalBytes());
//                        short[] additionalShorts = (short[]) ((ACKmessage)msg).getOptional();
//                        bytes3 = new byte[8]; //there are 4 additional shorts, therefore they will be represented by 8 bytes
//                        int j = 0;
//                        for (int i=0; i<additionalShorts.length; i++){ //create an byte array from the 4 additional shorts
//                            byte[] shortsToBytes = shortToBytes(additionalShorts[i]);
//                            bytes3[j] = shortsToBytes[0];
//                            bytes3[j+1] = shortsToBytes[1];
//                            j = j+2;
//                        }
//                        return mergeArrays(bytes1, bytes2, bytes3);
//                    }
//                    case(12):{
//                        bytes1 = shortToBytes(msg.getOpCode());
//                        bytes2 = shortToBytes(msg.getAdditionalBytes());
//                        String[] additionalString = (String[]) ((ACKmessage)msg).getOptional(); //at index 0 - the user blocking, at index 1 - the user being blocked
//                        bytes3 = mergeArrays(additionalString[0].getBytes(), zeroByte);
//                        bytes4 = mergeArrays(additionalString[1].getBytes(), zeroByte);
//                        bytes5 = mergeArrays(bytes3, bytes4);
//                        return mergeArrays(bytes1, bytes2, bytes5);
//                    }
//                }
//            }
//            case(11):
//            {
//                bytes1 = shortToBytes(msg.getOpCode());
//                bytes2 = shortToBytes(msg.getAdditionalBytes());
//                return mergeArrays(bytes1, bytes2, zeroByte);
//            }
//        }
//        return null;
//    }

    /**
     * an auxiliary function that merges between two byte arrays
     * @param arr1 first array to merge
     * @param arr2 second array to merge
     * @return
     */
    public byte[] mergeArrays(byte[] arr1, byte[] arr2){
        byte[] mergedArray = new byte[arr1.length+arr2.length];
        for (int i = 0; i< mergedArray.length; i++){
            if (i<arr1.length)
                mergedArray[i] = arr1[i];
            else
                mergedArray[i] = arr2[i- arr1.length];
        }
        return mergedArray;
    }
    /**
     * an auxiliary function that merges between three byte arrays
     * @param arr1 first array to merge
     * @param arr2 second array to merge
     * @param arr3 third array to merge
     * @return
     */
    public byte[] mergeArrays(byte[] arr1, byte[] arr2, byte[] arr3){
        byte[] mergedArray = new byte[arr1.length+arr2.length+ arr3.length];
        for (int i = 0; i< mergedArray.length; i++){
            if (i<arr1.length)
                mergedArray[i] = arr1[i];
            else if (i<(arr2.length + arr1.length))
                mergedArray[i] = arr2[i - arr1.length];
            else
                mergedArray[i] = arr3[i - arr1.length - arr2.length];
        }
        return mergedArray;
    }


    /**
     * an auxiliary function given to us that converts short to bytes
     * @param num short we would like to convert
     * @return
     */
    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    /**
     * an auxiliary function given to us that converts bytes to short
     * @param byteArr bytes array we would like to convert
     * @return
     */
    public short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }



}
