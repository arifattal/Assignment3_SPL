package bgu.spl.net.srv;

import bgu.spl.net.api.EncoderDecoder;
import bgu.spl.net.api.Protocol;

public class ReactorMain {
    public static void main(String[] args) {
        Server.reactor(
                Integer.parseInt(args[0]), //num of threads
                Integer.parseInt(args[1]), //port
                () ->  new Protocol(), //protocol factory lambda
                ()->new EncoderDecoder() //encoderDecoder factory lambda
        ).serve();
    }
}
