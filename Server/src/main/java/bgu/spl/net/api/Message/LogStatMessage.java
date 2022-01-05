package bgu.spl.net.api.Message;

import bgu.spl.net.api.Data;
import bgu.spl.net.api.User;

import java.util.concurrent.ConcurrentHashMap;

public class LogStatMessage extends Message{
    Data data = Data.getInstance();

    public LogStatMessage(short opCode){
        this.opCode = opCode;
    }

    @Override
    public void runMessage(User user, int connectionId) {
        if (user.getStatus() != User.Status.loggedIn){ //send error if user is loggedOut or unRegistered
            Message error = new ErrorMessage((short) 11, this.opCode);
            connections.send(connectionId, error);
        }
        else{
            ConcurrentHashMap<String, User> registeredUsersHM = data.getRegisteredUsersHM();
            //short[][] arr = new short[data.getLoggedInUsers()][6]; //create a short array with the requested info regarding logged in users
            //int i = 0;
            for (User user1: registeredUsersHM.values()){
                if (user1.getStatus() == User.Status.loggedIn && !user.isBlocking(user1.getUserName()) && !user1.isBlocking(user.getUserName())){
                    short[] optional = new short[4];
                    optional[0] = (short)user1.getAge();
                    optional[1] = (short)user1.getNumOfPosts();
                    optional[2] = (short)user1.numOfFollowers();
                    optional[3] = (short)user1.numOfUsersFollowing();
                    Message ack = new ACKmessage<>((short) 10, this.opCode, optional);
                    connections.send(connectionId, ack);
                    //insert relevant info

//                    arr[i][0] = (short)10;
//                    arr[i][1] = this.opCode;
//                    arr[i][2] = (short)user1.getAge();
//                    arr[i][3] = (short)user1.getNumOfPosts();
//                    arr[i][4] = (short)user1.numOfFollowers();
//                    arr[i][5] = (short)user1.numOfUsersFollowing();
//                    i++; //increment i for next logged in user
                }
            }
        }
    }

    @Override
    public String prepareForString() {
        return "";
    }

    @Override
    public short getAdditionalBytes() {
        return 0;
    }

}
