package mobi.dayvson.redes.partydj.models;

import com.google.gson.Gson;

import java.time.Duration;
import java.util.*;

public class Room implements Runnable {

    private List<User> userList;
    private final String roomToken;
    private Queue<Video> videoQueue;

    private volatile int state;

    private volatile boolean isRunningVideo;
    private Video videoRunning;

    private long startVideoTime;

    private static final int STOP = 1;
    private static final int SYNC = 1;

    public Room(String roomToken) {
        this.isRunningVideo = false;
        this.roomToken = roomToken;
        this.userList = new ArrayList<>();
        this.videoQueue = new LinkedList<>();
    }

    public List<User> getUserList() {
        return userList;
    }

    public void addUser(User user) {
        if(user != null) {
            this.userList.add(user);
            this.checkVideoRunning(user);
        }
    }

    private void checkVideoRunning(User user){
        if(isRunningVideo){
            long startTimeMilis = System.currentTimeMillis() - startVideoTime + SYNC;
            user.getWebSocket().send("get_video:0:" + videoRunning.getUrlId() + ":" + videoRunning.getThumbnail() + ":" + videoRunning.getVideoName() + ":" + startTimeMilis/1000);
        }
    }

    public void removeUser(User user){
        if(user != null)
            this.userList.remove(user);
    }

    public void addVideo(Video video){
        videoQueue.add(video);
    }

    public void stopRoom(){
        this.state = 1;
    }

    public Video nextVideo(){
        return videoQueue.remove();
    }

    public String getVideoQueueJson(){
        return new Gson().toJson(videoQueue);
    }

    public int queueCount(){
        return videoQueue.size();
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

    @Override
    public void run() {
        int times = 0;
        while(state != STOP){
            if(!videoQueue.isEmpty()){
                times = 0;
                videoRunning = videoQueue.remove();
                this.isRunningVideo = true;
                this.sendVideoToEveryoneOnRoom(videoRunning);
                this.startVideoTime = System.currentTimeMillis();
                try {
                    Thread.sleep(videoRunning.getDurationMilliseconds() + SYNC + 3 );
                } catch (InterruptedException e) {
                    this.stopRoom();
                }
            }else  {
                if(times == 0){
                    this.sendNoVideoToEveryoneOnRoom();
                    times = 1;
                }
            }
        }
    }

    private void sendVideoToEveryoneOnRoom(Video video){
        userList.forEach(user -> {
            user.getWebSocket().send("get_video:0:" + video.getUrlId() + ":" + video.getThumbnail() + ":" + video.getVideoName());
        });
    }

    private void sendNoVideoToEveryoneOnRoom(){
        userList.forEach(user -> {
            user.getWebSocket().send("get_video:1");
        });
    }
}
