package bgu.spl.net.srv;

import bgu.spl.net.api.Data;
import bgu.spl.net.api.EncoderDecoder;
import bgu.spl.net.api.Protocol;

public class TPCMain {
    public static void main(String[] args) {
        Data.getInstance();
        Server.threadPerClient(
                Integer.parseInt(args[0]), //port
                () -> new Protocol(), //protocol factory lambda
                () -> new EncoderDecoder() //encoderDecoder factory lambda
        ).serve();
    }
}
