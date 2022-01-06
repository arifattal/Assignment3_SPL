package bgu.spl.net.api;

import bgu.spl.net.api.Message.NotificationMessage;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class User {
    public enum Status{
        unRegistered, loggedOut, loggedIn
    }

    private String UserName;
    private String password;
    private int age;
    private String birthday;
    private User.Status status;
    private int numOfPosts;
    private List<String> followList;
    private List<String> followersList;
    private List<String> blockedUsersList;
    private ConcurrentLinkedQueue<NotificationMessage> notificationsQueue; //this queue is used for accumulating notifications while the user is logged out


    public User(String userName, String password, String birthday) {
        UserName = userName;
        this.password = password;
        try {
            this.age = 2021 - Integer.parseInt(birthday.substring(6));
        }catch (NumberFormatException e){}
        this.birthday = birthday;
        status = Status.unRegistered;
        numOfPosts = 0;
        followList = new ArrayList<>();
        followersList = new ArrayList<>();
        blockedUsersList = new ArrayList<>();
        notificationsQueue = new ConcurrentLinkedQueue<>();
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

    public synchronized List<String> getFollowList() {
        return followList;
    }

    public synchronized List<String> getFollowersList() {
        return followersList;
    }

    public synchronized List<String> getBlockedUsersList() {
        return blockedUsersList;
    }

    public int getAge() {
        return age;
    }

    public String getBirthday() {
        return birthday;
    }

    public Queue<NotificationMessage> getNotificationsQueue() {
        return notificationsQueue;
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

    public synchronized void follow(String userName){
        followList.add(userName);
    }

    public synchronized void unFollow(String userName){
        followList.remove(userName);
    }

    public synchronized void addFollower(String userName){
        followersList.add(userName);
    }

    public synchronized void removeFollower(String userName){
        followersList.remove(userName);
    }

    public synchronized int numOfFollowers(){
        return followersList.size();
    }

    public synchronized int numOfUsersFollowing(){
        return followList.size();
    }

    public int getNumOfPosts() {
        return numOfPosts;
    }

    public synchronized void addNotification(NotificationMessage notification){
        notificationsQueue.add(notification);
    }

    public synchronized boolean isBlocking(String userName){
        return blockedUsersList.contains(userName);
    }

    public synchronized void block(String userName){
        blockedUsersList.add(userName);
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
        //set age
        try {
            LocalDate date = LocalDate.now();
            int year = Integer.parseInt(birthday.substring(6));
            int month = Integer.parseInt(birthday.substring(3,5));
            int day = Integer.parseInt(birthday.substring(0,2));
            LocalDate birth = LocalDate.of(year, month, day);
            this.age = Period.between(birth, date).getYears();
        }catch (NumberFormatException e){} //this covers NumberFormatException and exceptions thrown by the date functions. for ex. if month = 13

    }
}
