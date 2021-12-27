package bgu.spl.net.srv;

import java.util.ArrayList;
import java.util.List;

public class User {
    public enum Status{
        unRegistered, loggedOut, loggedIn
    }

    private String UserName;
    private String password;
    private int age;
    private String birthday; //this is received during registration and might not be a necessary field
    private User.Status status;
    private List<User> followList;
    private List<User> followersList;
    private List<String> blockedUsersList; //change this to List<tempUser> if user names are not unique

    public User(String userName, String password, String birthday) {
        UserName = userName;
        this.password = password;
        try {
            this.age = Integer.parseInt(birthday.substring(6));
        }catch (NumberFormatException e){}
        this.birthday = birthday;
        status = Status.loggedOut;
        followList = new ArrayList<>();
        followersList = new ArrayList<>();
        blockedUsersList = new ArrayList<>();
    }

    public String getUserName() {
        return UserName;
    }

    public String getPassword() {
        return password;
    }

    public Status getStatus() {
        return status;
    }

    public List<User> getFollowList() {
        return followList;
    }

    public List<User> getFollowersList() {
        return followersList;
    }

    public List<String> getBlockedUsersList() {
        return blockedUsersList;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
