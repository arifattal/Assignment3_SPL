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
    private int numOfPosts;
    private List<String> followList;
    private List<String> followersList;
    private List<String> blockedUsersList; //change this to List<tempUser> if user names are not unique

    public User(String userName, String password, String birthday) {
        UserName = userName;
        this.password = password;
        try {
            this.age = Integer.parseInt(birthday.substring(6));
        }catch (NumberFormatException e){}
        this.birthday = birthday;
        status = Status.loggedOut;
        numOfPosts = 0;
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

    public List<String> getFollowList() {
        return followList;
    }

    public List<String> getFollowersList() {
        return followersList;
    }

    public List<String> getBlockedUsersList() {
        return blockedUsersList;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isFollowing(String userName){
        return followList.contains(userName);
    }

    public boolean isFollowedBy(String userName){
        return followersList.contains(userName);
    }

    public void follow(String userName){
        followList.add(userName);
    }

    public void unFollow(String userName){
        followList.remove(userName);
    }

    public void addFollower(String userName){
        followersList.add(userName);
    }

    public void removeFollower(String userName){
        followersList.remove(userName);
    }

    public int numOfFollowers(){
        return followersList.size();
    }

    public int numOfUsersFollowing(){
        return followList.size();
    }

    public int getNumOfPosts() {
        return numOfPosts;
    }

    public int getAge() {
        return age;
    }

    public String getBirthday() {
        return birthday;
    }
}
