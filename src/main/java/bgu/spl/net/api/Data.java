package bgu.spl.net.api;

import bgu.spl.net.api.Message.Message;
import bgu.spl.net.bidi.BidiMessagingProtocol;
import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.ConnectionsImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Data {
    private final Object post_pmLock = new Object();

    private List<Message> post_pmList; //change to concurrent structure
    private ConcurrentHashMap<String, User> registeredUsersHM; //maps between userNames and user objects
    private ConcurrentHashMap<User, Integer> usersClientIdsHM; //this HM maps between users and their client ids
    private List<String> filteredWords;
    private int loggedInUsers; //used for LogStatMessage

    //implement data as a singleton
    private static class DataHolder{
        private static Data instance = new Data();
    }
    public static Data getInstance(){
        return DataHolder.instance;
    }

    private Data(){
        post_pmList = new ArrayList<>();
        registeredUsersHM = new ConcurrentHashMap<>();
        usersClientIdsHM = new ConcurrentHashMap<>();
        filteredWords = new ArrayList<>();
        filteredWords.add("shalom");
        filteredWords.add("maniac");
        loggedInUsers = 0;
    }

    public boolean isRegistered(String username){
        if (registeredUsersHM.containsKey(username)){
            return true;
        }
        return false;
    }

    public void RegisterUser(User user, int connectionId){
        usersClientIdsHM.put(user, connectionId);
        registeredUsersHM.put(user.getUserName(), user);
    }

    public Integer getUserClientId(User user){
        return usersClientIdsHM.get(user);
    }

    public User getUser(String userName){
        return registeredUsersHM.get(userName);
    }

    public ConcurrentHashMap<String, User> getRegisteredUsersHM() {
        return registeredUsersHM;
    }

    public int getLoggedInUsers() {
        return loggedInUsers;
    }

    public List<String> getFilteredWords() {
        return filteredWords;
    }

    public void incDecLoggedInUsers(int i){
        loggedInUsers = loggedInUsers + i;
    }

    public void addPost_pm(Message message){
        synchronized (post_pmLock) {
            post_pmList.add(message);
        }
    }

    public boolean handlerHasUser(int connectionId){ //this returns true if the connection handler has a user in the system
        return usersClientIdsHM.contains(connectionId);
    }

    public void logOutUser(User user){
        //change the protocol's shouldTerminate value to true
        int connectionId = usersClientIdsHM.get(user);
        ConnectionsImpl connections = ConnectionsImpl.getInstance();
        ConnectionHandler handler = connections.getConnectionHandler(connectionId);
        BidiMessagingProtocol protocol = handler.getProtocol();
        protocol.setShouldTerminate(true);
        //
        usersClientIdsHM.remove(user); //remove the mapping between the user and the connection id. this will allow a connectionHandler to take access of this user in the future
    }

    //connects user to connection Id if he doesn't have one, this is used for logging in after a logout
    public void connectUserToConId(User user, int connectionId){
        if (!usersClientIdsHM.containsKey(user)){
            usersClientIdsHM.put(user, connectionId);
        }
    }

    public void setFilteredWords(ArrayList<String> wordsArray){
        filteredWords = wordsArray;
    }
}
