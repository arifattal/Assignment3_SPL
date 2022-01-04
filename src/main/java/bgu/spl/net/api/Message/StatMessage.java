package bgu.spl.net.api.Message;

import bgu.spl.net.api.Data;
import bgu.spl.net.api.User;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class StatMessage extends Message{
    Data data = Data.getInstance();
    String[] userNamesList;
    String userNamesString;

    public StatMessage(short opCode, String userNamesList) {
        this.data = data;
        this.userNamesList = userNamesList.split("\\|"); //create an array of usernames out of the string provided
        this.opCode = opCode;
        this.userNamesString = userNamesList;
    }

    @Override
    public void runMessage(User user, int connectionId) {
        if (user.getStatus() != User.Status.loggedIn){ //send error if user is loggedOut or unRegistered
            Message error = new ErrorMessage((short) 11, this.opCode);
            connections.send(connectionId, error);
        }
        else{
            boolean sendError = false; //this flag is used to stop the action if one of the users requested is not registered
            ConcurrentHashMap<String, User> registeredUsersHM = data.getRegisteredUsersHM();
            HashMap<String, User> requestedUsersHM = new HashMap<>(); //this HM will hold all user objects requested
            for (int i = 0; i<userNamesList.length && !sendError; i++){ //create the requestedUsersHM
                User user1 = registeredUsersHM.get(userNamesList[i]);
                if (user1 == null || user1.getStatus() == User.Status.unRegistered){ //we will want to send an error if any of the users requested isn't registered
                    sendError = true;
                }
                else {
                    requestedUsersHM.put(userNamesList[i], user1); //add user
                }
            }
            if (sendError){ //if an unregistered user was found in the list provided send an error
                Message error = new ErrorMessage((short) 11, this.opCode);
                connections.send(connectionId, error);
            }
            else {
                //short[][] arr = new short[requestedUsersHM.size()][6]; //create a short array with the requested info regarding the users provided
                //int i = 0;
                for (User user1: requestedUsersHM.values()){
                    short[] optional = new short[4];
                    optional[0] = (short)user1.getAge();
                    optional[1] = (short)user1.getNumOfPosts();
                    optional[2] = (short)user1.numOfFollowers();
                    optional[3] = (short)user1.numOfUsersFollowing();
                    Message ack = new ACKmessage<>((short) 10, this.opCode, optional);
                    connections.send(connectionId, ack);

//                    //insert relevant info
//                    arr[i][0] = (short)10;
//                    arr[i][1] = this.opCode;
//                    arr[i][2] = (short)user1.getAge();
//                    arr[i][3] = (short)user1.getNumOfPosts();
//                    arr[i][4] = (short)user1.numOfFollowers();
//                    arr[i][5] = (short)user1.numOfUsersFollowing();
//                    i++; //increment i for next user in list
                }
                //ACKmessage.ShortOptional optional = new ACKmessage.ShortOptional(arr); //create an optional of type ShortOptional - more info in ACKMessage

            }
        }
    }

    @Override
    public String prepareForString() {
        String str = userNamesString + ';';
        return str;
    }

    @Override
    public short getAdditionalBytes() {
        return 0;
    }


}
