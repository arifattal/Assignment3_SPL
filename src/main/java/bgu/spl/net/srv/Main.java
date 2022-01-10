package bgu.spl.net.srv;

import bgu.spl.net.api.Data;
import bgu.spl.net.api.EncoderDecoder;
import bgu.spl.net.api.Protocol;

public class Main {
    public static void main(String[] args) {
        Data.getInstance();
        if (args[0].equals("1")){
            Server.threadPerClient(
                    Integer.parseInt(args[1]), //port
                    () ->  new Protocol(), //protocol factory lambda
                    ()->new EncoderDecoder() //encoderDecoder factory lambda
            ).serve();
            }
        else if(args[0].equals("2")){
            Server.reactor(
                    Integer.parseInt(args[1]), //num of threads
                    Integer.parseInt(args[2]), //port
                    () ->  new Protocol(), //protocol factory lambda
                    ()->new EncoderDecoder() //encoderDecoder factory lambda
            ).serve();
        }
        }
    }
