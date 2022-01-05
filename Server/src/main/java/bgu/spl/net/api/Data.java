package bgu.spl.net.api;

import bgu.spl.net.api.Message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Data {
    private final Object post_pmLock = new Object();
    private List<Message> post_pmList; //change to concurrent structure
    private ConcurrentHashMap<String, User> registeredUsersHM;
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
        filteredWords = new ArrayList<>();
        loggedInUsers = 0;
    }


    public boolean isRegistered(String username){
        if (registeredUsersHM.contains(username)){
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
}
