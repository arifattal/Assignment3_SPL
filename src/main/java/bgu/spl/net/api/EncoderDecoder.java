package bgu.spl.net.api;

import bgu.spl.net.api.Message.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class EncoderDecoder<T> implements MessageEncoderDecoder<T>{

    private byte[] bytes = new byte[1 << 10];
    private int len = 0;
//
    @Override
    public T decodeNextByte(byte nextByte) {
        if (nextByte == '\n') {
            Message msgObject = getMessageObj(bytes);
            return (T) msgObject;
        }
        pushByte(nextByte); //this function decodes the string
        return null;
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    private Message getMessageObj(byte[] bytes){
        if (bytes != null) {
            byte[] opCodeByte = {bytes[0], bytes[1]};
            short opCode = bytesToShort(opCodeByte);

            switch (opCode) {
                case (1): {
                    ArrayList<String> stringList = getStringArray(bytes, 2, 3);
                    String username = stringList.get(0);
                    String pass = stringList.get(1);
                    String bDay = stringList.get(2);
                    RegisterMessage register = new RegisterMessage(opCode, username, pass, bDay);
                }
                case (2):{
                    ArrayList<String> stringList = getStringArray(bytes, 2, 1);
                    String username = stringList.get(0);
                    String pass = stringList.get(1);
                    String bDay = stringList.get(2);
                    String captcha = new String(bytes, bytes.length-1, 1, StandardCharsets.UTF_8); //the captcha takes one byte and is at the last position
                    char ch = captcha.charAt(0);
                }
                case (3):{
                    LogOutMessage logOut = new LogOutMessage(opCode);
                }
                case (4):{

                }
                case (5):{
                    ArrayList<String> stringList = getStringArray(bytes, 2, 1);
                    String content = stringList.get(0);
                    PostMessage post = new PostMessage(opCode, content);
                }
                case (6):{
                    ArrayList<String> stringList = getStringArray(bytes, 2, 3);
                    String userName = stringList.get(0);
                    String content = stringList.get(1);
                    String dateAndTime = stringList.get(2);
                    PmMessage pm = new PmMessage(opCode, userName, content, dateAndTime);
                }
                case (7):{
                    LogStatMessage logStat = new LogStatMessage(opCode);
                }
                case (8):{
                    ArrayList<String> stringList = getStringArray(bytes, 2, 1);
                    String userNames = stringList.get(0);
                    StatMessage stat = new StatMessage(opCode, userNames);
                }
                case (9):{

                }
                case (10):{

                }

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
    public byte[] encode(T message) {
        Message msg = (Message)message; //used for ease due to need for casting
        String strForBytes = msg.prepareForString();
        byte[] bytes1;
        byte[] bytes2;
        byte[] bytes3;
        byte[] bytes4;
        byte[] bytes5;
        byte[] bytes6;
        byte[] zeroByte = new byte[]{'\0'};

        switch (msg.getOpCode()){
            case(10):
            {
                switch (msg.getAdditionalBytes()){
                    case(1): case(2): case(4): case(5): case(6): case(12):{
                        bytes1 = shortToBytes(msg.getOpCode());
                        bytes2 = shortToBytes(msg.getAdditionalBytes());
                        bytes3 = (strForBytes).getBytes();
                        bytes4 = mergeArrays(bytes1, bytes2);
                        bytes5 = mergeArrays(bytes3, zeroByte);
                        return mergeArrays(bytes4, bytes5);
                    }
                    case(7): case(8): { //complete this

                    }
                }
            }
            case(11):
            {
                bytes1 = shortToBytes(msg.getOpCode());
                bytes2 = shortToBytes(msg.getAdditionalBytes());
                return mergeArrays(bytes1, bytes2, zeroByte);
            }
        }
//            case(1):
//            case(5):
//            case(6):
//            case(8):
//            case(12):
//                {
//                bytes1 = shortToBytes((msg.getOpCode()));
//                bytes2 = (strForBytes + "\n").getBytes();
//                return mergeArrays(bytes1, bytes2);
//            }
//            case(2): {
//                bytes1 = shortToBytes(msg.getOpCode());
//                bytes2 = (strForBytes).getBytes();
//                bytes3 = shortToBytes(((LoginMessage)msg).getCaptcha());
//                byte[] bytes4 = mergeArrays(bytes3, (";".getBytes())); //the encoded message must end with ";". for that reason we merged these two arrays first
//                return mergeArrays(bytes1, bytes2, bytes4);
//            }
//            case (3):
//            case(7):
//                {
//                bytes1 = shortToBytes(msg.getOpCode());
//                return bytes1;
//            }
//            case  (4):
//            case (9):
//            case (11):
//            case (10):
//            {
//                bytes1 = shortToBytes(msg.getOpCode());
//                bytes2 = shortToBytes(msg.getAdditionalBytes());
//                bytes3 = (strForBytes).getBytes(); //for case 11 strForBytes = ""
//                return mergeArrays(bytes1, bytes2, bytes3);
//            }
//        }
        return null;
    }

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
