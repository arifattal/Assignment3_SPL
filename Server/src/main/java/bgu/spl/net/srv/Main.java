package bgu.spl.net.srv;

import bgu.spl.net.api.EncoderDecoder;
import bgu.spl.net.api.Protocol;

public class Main {
    public static void main(String[] args) {
        if (args[0] == "1"){
            Server.threadPerClient(
                    Integer.parseInt(args[1]), //port
                    () ->  new Protocol(), //protocol factory lambda
                    ()->new EncoderDecoder() //encoderDecoder factory lambda
            ).serve();
            }

        else if(args[0] == "2"){
            Server.reactor(
                    Integer.parseInt(args[1]), //num of threads
                    Integer.parseInt(args[0]), //port
                    () ->  new Protocol(), //protocol factory lambda
                    ()->new EncoderDecoder() //encoderDecoder factory lambda
            ).serve();
        }
        }
    }
