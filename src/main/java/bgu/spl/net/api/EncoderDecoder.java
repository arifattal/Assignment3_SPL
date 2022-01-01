package bgu.spl.net.api;

import bgu.spl.net.api.Message.ACKmessage;
import bgu.spl.net.api.Message.BlockMessage;
import bgu.spl.net.api.Message.LoginMessage;
import bgu.spl.net.api.Message.Message;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class EncoderDecoder<T> implements MessageEncoderDecoder<T>{

    private byte[] bytes = new byte[1 << 10];
    private int len = 0;

    @Override
    public T decodeNextByte(byte nextByte) {
        if (nextByte == '\n') {
            String StringMessage =  popString();
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

    private String popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }

    private T getMessageObj(String StringMessage){


        return null;
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
        short zero = '\0';

        switch (msg.getOpCode()){
            case(10):
            {
                switch (msg.getAdditionalBytes()){
                    case(1): case(2): case(4): case(5): case(6): case(12):{
                        bytes1 = shortToBytes(msg.getOpCode());
                        bytes2 = shortToBytes(msg.getAdditionalBytes());
                        bytes3 = (strForBytes).getBytes();
                        bytes4 = shortToBytes(zero); //adds that \0 byte
                        bytes5 = mergeArrays(bytes1, bytes2);
                        bytes6 = mergeArrays(bytes3, bytes4);
                        return mergeArrays(bytes5, bytes6);
                    }
                    case(7): case(8): {

                    }
                }
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
