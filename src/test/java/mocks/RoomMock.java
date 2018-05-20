package mocks;

import mobi.dayvson.redes.partydj.interfaces.IRoom;
import mobi.dayvson.redes.partydj.models.User;
import mobi.dayvson.redes.partydj.models.Video;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RoomMock implements IRoom {

    private static final int STOP = 1;
    private static final int SYNC = 1;
    private final String roomToken;
    private List<User> userList;
    private Queue<Video> videoQueue;
    private volatile int state;
    private volatile boolean isRunningVideo;
    private Video videoRunning;
    private long startVideoTime;

    public RoomMock(String roomToken) {
        this.roomToken = roomToken;
        this.userList = new ArrayList<>();
        this.videoQueue = new LinkedList<>();
    }

    public List<User> getUserList() {
        return userList;
    }

    public void run() throws InterruptedException {
        if (state == 1) {
            int times = 0;

            if (!videoQueue.isEmpty()) {
                times = 0;

                this.videoRunning = videoQueue.remove();
                this.isRunningVideo = true;
                this.sendVideoToEveryoneOnRoom(videoRunning);
                this.startVideoTime = System.currentTimeMillis();

            } else {
                if (times == 0) {
                    this.sendNoVideoToEveryoneOnRoom();
                    times = 1;
                }
            }
        } else {
            throw new InterruptedException();
        }

    }

    public void addUser(User user) {
        if (user != null) {
            this.userList.add(user);
            this.checkVideoRunning(user);
        } else {
            throw new IllegalArgumentException("Usuario não pode ser vazio");
        }
    }

    @Override
    public void removeUser(User user) {

    }

    @Override
    public String getVideoQueueJson() {
        return null;
    }

    @Override
    public int queueCount() {
        return videoQueue.size();
    }

    private void checkVideoRunning(User user) {
        if (isRunningVideo) {
            long startTimeMilis = System.currentTimeMillis() - startVideoTime + SYNC;
            if(videoRunning == null){ System.out.println("nulo"); }
            user.getWebSocket().send("get_video:0:" + videoRunning.getUrlId() + ":" + videoRunning.getThumbnail() + ":" + videoRunning.getVideoName() + ":" + startTimeMilis / 1000);
        }
    }

    @Override
    public void sendVideoToEveryoneOnRoom(Video video) {
        userList.forEach(user -> {
            user.getWebSocket().send("get_video:0:" + video.getUrlId() + ":" + video.getThumbnail() + ":" + video.getVideoName());
        });
    }

    @Override
    public void sendNoVideoToEveryoneOnRoom() {
        userList.forEach(user -> {
            user.getWebSocket().send("get_video:1");
        });
    }

    @Override
    public void addVideo(Video video) {
        videoQueue.add(video);
    }

    @Override
    public Video nextVideo() {
        return videoQueue.remove();
    }
}
