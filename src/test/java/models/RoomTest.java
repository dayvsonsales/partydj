package models;

import mobi.dayvson.redes.partydj.enums.Message;
import mobi.dayvson.redes.partydj.interfaces.IRoom;
import mobi.dayvson.redes.partydj.models.Protocol;
import mobi.dayvson.redes.partydj.models.Room;
import mobi.dayvson.redes.partydj.models.User;
import mobi.dayvson.redes.partydj.models.Video;
import mocks.RoomMock;
import mocks.WebSocketMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.format.DateTimeParseException;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class RoomTest {

    private RoomMock room;

    @BeforeEach
    void setUp(){
        room = new RoomMock("987");
    }

    @Test
    void addUser(){
        User user2 = new User(null, "Dayvson", null);

        room.addUser(user2);

        assertEquals(1, room.getUserList().size());
    }

    @Test
    void addUserNull(){
        assertThrows(IllegalArgumentException.class, () -> room.addUser(null));
    }

    @Test
    void checkVideoRunningWithRunningVideo() throws NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {

        WebSocketMock webSocketMock = new WebSocketMock();

        User user2 = new User(null, "Dayvson", webSocketMock);

        Field f = RoomMock.class.getDeclaredField("isRunningVideo");
        f.setAccessible(true);

        f.set(room, true);

        Field videoRunning = RoomMock.class.getDeclaredField("videoRunning");
        videoRunning.setAccessible(true);
        videoRunning.set(room, new Video("", "", "PT20M1S", ""));

        Method m = RoomMock.class.getDeclaredMethod("checkVideoRunning", User.class);
        m.setAccessible(true);

        m.invoke(room, user2);


        assertTrue(user2.getWebSocket().hasBufferedData());
    }

    @Test
    void checkVideoRunningWithoutRunningVideo() throws NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {

        WebSocketMock webSocketMock = new WebSocketMock();

        User user2 = new User(null, "Dayvson", webSocketMock);

        Field f = RoomMock.class.getDeclaredField("isRunningVideo");
        f.setAccessible(true);

        f.set(room, false);

        Field videoRunning = RoomMock.class.getDeclaredField("videoRunning");
        videoRunning.setAccessible(true);
        videoRunning.set(room, new Video("", "", "PT20M1S", ""));

        Method m = RoomMock.class.getDeclaredMethod("checkVideoRunning", User.class);
        m.setAccessible(true);

        m.invoke(room, user2);

        assertFalse(user2.getWebSocket().hasBufferedData());
    }

    @Test
    void sendVideoToEveryoneOnRoom(){

        User user = new User(null, "Maria", new WebSocketMock());
        room.addUser(user);

        User user2 = new User(null, "Dayvson",  new WebSocketMock());
        room.addUser(user2);

        User user3NotInRoom = new User(null, "Malbec", new WebSocketMock());

        room.sendVideoToEveryoneOnRoom(new Video("", "", "PT20M1S", ""));

        assertAll(() -> {
            assertTrue(user.getWebSocket().hasBufferedData());
            assertTrue(user2.getWebSocket().hasBufferedData());
            assertFalse(user3NotInRoom.getWebSocket().hasBufferedData());
        });

    }

    @Test
    void sendNoVideoToEveryoneOnRoom(){

        User user = new User(null, "Maria", new WebSocketMock());
        room.addUser(user);

        User user2 = new User(null, "Dayvson",  new WebSocketMock());
        room.addUser(user2);

        User user3NotInRoom = new User(null, "Malbec", new WebSocketMock());

        room.sendNoVideoToEveryoneOnRoom();

        assertAll(() -> {
            assertTrue(user.getWebSocket().hasBufferedData());
            assertTrue(user2.getWebSocket().hasBufferedData());
            assertFalse(user3NotInRoom.getWebSocket().hasBufferedData());
        });

    }

    @Test
    void addVideo(){
        Video video = new Video("", "", "PT20M1S", "");

        room.addVideo(video);

        assertEquals(video, room.nextVideo());
    }

    @Test
    void runStateNormalSendingVideoToUsers() throws InterruptedException, NoSuchFieldException, IllegalAccessException {

        User user = new User(null, "Maria", new WebSocketMock());
        room.addUser(user);

        User user2 = new User(null, "Dayvson",  new WebSocketMock());
        room.addUser(user2);

        User user3NotInRoom = new User(null, "Malbec", new WebSocketMock());


        Video video = new Video("", "", "PT20M1S", "");
        room.addVideo(video);

        Field state = RoomMock.class.getDeclaredField("state");
        state.setAccessible(true);

        state.set(room, 1);

        room.run();

        assertAll(() -> {
            assertTrue(user.getWebSocket().hasBufferedData());
            assertTrue(user2.getWebSocket().hasBufferedData());
            assertFalse(user3NotInRoom.getWebSocket().hasBufferedData());
        });
    }

    @Test
    void runStateInterruptingExecutionThread() throws InterruptedException, NoSuchFieldException, IllegalAccessException {

        User user = new User(null, "Maria", new WebSocketMock());
        room.addUser(user);

        User user2 = new User(null, "Dayvson",  new WebSocketMock());
        room.addUser(user2);

        User user3NotInRoom = new User(null, "Malbec", new WebSocketMock());


        Video video = new Video("", "", "PT20M1S", "");
        room.addVideo(video);

        Field state = RoomMock.class.getDeclaredField("state");
        state.setAccessible(true);

        state.set(room, 0);

        assertThrows(InterruptedException.class, () -> room.run());

    }

}
