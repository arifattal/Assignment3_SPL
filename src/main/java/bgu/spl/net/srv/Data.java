package bgu.spl.net.srv;

import bgu.spl.net.srv.Message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Data {
    //change to concurrent structures
    List<Message> post_pmList;
    ConcurrentHashMap<String, User> registeredUsersHM;
    List<String> filteredWords;

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
    }

    public boolean isRegistered(String username){
        if (registeredUsersHM.contains(username)){
            return true;
        }
        return false;
    }

    public void RegisterUser(User user){
        registeredUsersHM.put(user.getUserName(), user);
    }

    public User getUser(String userName){
        return registeredUsersHM.get(userName);
    }

}
