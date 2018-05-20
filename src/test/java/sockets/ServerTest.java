package sockets;

import mobi.dayvson.redes.partydj.Server;
import mobi.dayvson.redes.partydj.interfaces.IRoom;
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
import java.net.InetSocketAddress;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class ServerTest {

    private WebSocketMock webSocketMock;
    private Room roomMock;
    private Server server;

    @BeforeEach
    void setUp(){
        this.webSocketMock = new WebSocketMock();
        this.roomMock = new Room("987");
        roomMock.addUser(new User(null, "Dayvson", webSocketMock));
        List<IRoom> roomMockList = new ArrayList<>();
        roomMockList.add(roomMock);
        this.server = new Server(new InetSocketAddress(8000), roomMockList, new ArrayList<>());
    }

    @Test
    void onOpen() throws NoSuchFieldException, IllegalAccessException {

        server.onOpen(webSocketMock, null);

        Field guests = Server.class.getDeclaredField("guests");
        guests.setAccessible(true);

        List<User> guestList = (List<User>) guests.get(server);

        assertEquals(1, guestList.size());
    }

    @Test
    void onClose(){
        WebSocketMock webSocketMockDaMaria = new WebSocketMock();
        roomMock.addUser(new User(null, "Maria", webSocketMockDaMaria));
        server.onClose(webSocketMock, 0, "", true);

        assertTrue(webSocketMockDaMaria.hasBufferedData());
    }

    @Test
    void onMessageCreateRoom() throws NoSuchFieldException, IllegalAccessException {
        server.onMessage(webSocketMock, "create_room:Dayvson");

        Field roomList = Server.class.getDeclaredField("roomList");
        roomList.setAccessible(true);

        List<IRoom> roomList1 = (List<IRoom>) roomList.get(server);

        assertAll(() -> {
            assertTrue(webSocketMock.hasBufferedData());
            assertEquals(2, roomList1.size());
        });
    }

    @Test
    void onMessageEnterRoom(){
        WebSocketMock webSocketMockMaria = new WebSocketMock();

        server.onMessage(webSocketMockMaria, "enter_room:987:Maria");

        assertAll(() -> {
            assertTrue(webSocketMockMaria.hasBufferedData());
            assertTrue(webSocketMock.hasBufferedData());
        });
    }

    @Test
    void onMessageAddVideo() throws NoSuchFieldException, IllegalAccessException {

        server.onMessage(webSocketMock, "add_video:987:Dayvson:youtube.com:you.com:PT20M1S:Teste");

        Field roomList = Server.class.getDeclaredField("roomList");
        roomList.setAccessible(true);

        List<IRoom> roomList1 = (List<IRoom>) roomList.get(server);

        IRoom room = roomList1.get(roomList1.indexOf(roomMock));

        Video video = new Video("youtube.com", "you.com", "PT20M1S", "Teste");

        assertAll(() -> {
            assertTrue(webSocketMock.hasBufferedData());
            assertEquals(video, room.nextVideo());
        });
    }

    @Test
    void onMessageListVideos(){
        server.onMessage(webSocketMock, "list_videos:987");

        assertAll(() -> {
            assertTrue(webSocketMock.hasBufferedData());
        });
    }

    @Test
    void onMessageGetVideo() throws NoSuchFieldException, IllegalAccessException {
        server.onMessage(webSocketMock, "add_video:987:Dayvson:youtube.com:you.com:PT20M1S:Teste");

        Field roomList = Server.class.getDeclaredField("roomList");
        roomList.setAccessible(true);

        List<IRoom> roomList1 = (List<IRoom>) roomList.get(server);

        IRoom room = roomList1.get(roomList1.indexOf(roomMock));

        Video video = new Video("youtube.com", "you.com", "PT20M1S", "Teste");


        server.onMessage(webSocketMock, "get_video:987");

        assertAll(() -> {
            assertTrue(webSocketMock.hasBufferedData());
        });
    }

    @Test
    void onMessageSendMessage(){
        server.onMessage(webSocketMock, "send_message:987:Testando:Dayvson");

        assertAll(() -> {
            assertTrue(webSocketMock.hasBufferedData());
        });
    }

    @Test
    void generateRoomToken() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method generate = Server.class.getDeclaredMethod("generateRoomToken");
        generate.setAccessible(true);

        String token = (String) generate.invoke(server);

        assertAll(() -> {
            assertTrue(!token.isEmpty());
            assertTrue(token.length() == 6);
        });
    }

    @Test
    void sendMessageToAllUserOnRoom() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method generate = Server.class.getDeclaredMethod("sendMessageToAllUserOnRoom", IRoom.class, String.class, String.class, String.class);
        generate.setAccessible(true);

        generate.invoke(server, roomMock, "Teste", "Classe de Teste", "Teste");

        assertAll(() -> {
            assertTrue(webSocketMock.hasBufferedData());
        });
    }

}
