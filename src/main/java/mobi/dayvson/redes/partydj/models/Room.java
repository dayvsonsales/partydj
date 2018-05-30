package mobi.dayvson.redes.partydj.models;

import com.google.gson.Gson;
import mobi.dayvson.redes.partydj.interfaces.IRoom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;

public class Room implements Runnable, IRoom {

    private Logger logger = LoggerFactory.getLogger(getClass());

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
        if (user != null) {
            this.userList.add(user);
            this.checkVideoRunning(user);
        } else {
            throw new IllegalArgumentException("Usuario não pode ser vazio");
        }
    }

    private void checkVideoRunning(User user) {
        if (isRunningVideo) {
            long startTimeMilis = System.currentTimeMillis() - startVideoTime + SYNC;
            user.getWebSocket().send("get_video:0:" + videoRunning.getUrlId() + ":" + videoRunning.getThumbnail() + ":" + videoRunning.getVideoName() + ":" + startTimeMilis / 1000);
        }
    }

    public void removeUser(User user) {
        if (user != null)
            this.userList.remove(user);
    }

    public void addVideo(Video video) {
        videoQueue.add(video);
    }

    private void stopRoom() {
        this.state = 1;
    }

    public Video nextVideo() {
        if (videoQueue.size() > 0)
            return videoQueue.remove();
        return null;
    }

    public String getVideoQueueJson() {
        return new Gson().toJson(videoQueue);
    }

    public int queueCount() {
        return videoQueue.size();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(this.roomToken);
        sb.append(" ");
        sb.append(userList.size());
        sb.append(" ");
        sb.append(videoQueue.size());

        return sb.toString();
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
        while (state != STOP) {
            if (!videoQueue.isEmpty()) {
                times = 0;

                this.videoRunning = videoQueue.remove();
                this.isRunningVideo = true;
                this.sendVideoToEveryoneOnRoom(videoRunning);
                this.startVideoTime = System.currentTimeMillis();

                try {
                    Thread.sleep(videoRunning.getDurationMilliseconds() + SYNC + 3);
                } catch (InterruptedException e) {
                    this.stopRoom();
                    logger.error("Aconteceu um erro na thread da sala: " + roomToken, e.getMessage());
                }
            } else {
                if (times == 0) {
                    this.sendNoVideoToEveryoneOnRoom();
                    times = 1;
                }
            }
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
}
