package bgu.spl.net;


/**
 * This class was added by us and meant for testing portions of the code.
 */
public class TestingClass {

    public static byte[] shortToBytes(short num){
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    public static short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    public static void main(String[] args) {

        short[] additionalShorts = new short[]{2,6,4,5};
        byte[] bytes3 = new byte[8];
        int j= 0;
        for (int i=0; i<additionalShorts.length; i++){
            byte[] shortsToBytes = shortToBytes(additionalShorts[i]);
            bytes3[j] = shortsToBytes[0];
            bytes3[j+1] = shortsToBytes[1];
            j = j+2;
        }
        for (byte element:bytes3){
            System.out.println(element);
        }


    }
}
