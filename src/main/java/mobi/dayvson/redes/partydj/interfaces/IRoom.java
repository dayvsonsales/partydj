package mobi.dayvson.redes.partydj.interfaces;

import mobi.dayvson.redes.partydj.models.User;
import mobi.dayvson.redes.partydj.models.Video;

import java.util.List;

public interface IRoom {

    void sendVideoToEveryoneOnRoom(Video video);

    void sendNoVideoToEveryoneOnRoom();

    void addVideo(Video video);

    Video nextVideo();

    List<User> getUserList();

    void addUser(User user);

    void removeUser(User user);

    String getVideoQueueJson();

    int queueCount();



}
