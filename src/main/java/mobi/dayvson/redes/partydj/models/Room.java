package mobi.dayvson.redes.partydj.models;

import com.google.gson.Gson;

import java.util.*;

public class Room {

    private List<User> userList;
    private final String roomToken;
    private Queue<Video> videoQueue;

    public Room(String roomToken) {
        this.roomToken = roomToken;
        this.userList = new ArrayList<>();
        this.videoQueue = new LinkedList<>();
    }

    public List<User> getUserList() {
        return userList;
    }

    public void addUser(User user) {
        if(user != null)
            this.userList.add(user);
    }

    public void removeUser(User user){
        if(user != null)
            this.userList.remove(user);
    }

    public void addVideo(Video video){
        videoQueue.add(video);
    }

    public Video nextVideo(){
        return videoQueue.remove();
    }

    public String getVideoQueueJson(){
        return new Gson().toJson(videoQueue);
    }

    public String toString(){
        return this.roomToken + " " + userList.size() + " " + videoQueue.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(roomToken, room.roomToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomToken);
    }
}
