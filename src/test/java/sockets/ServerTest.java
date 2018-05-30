package sockets;

import mobi.dayvson.redes.partydj.Server;
import mobi.dayvson.redes.partydj.interfaces.IRoom;
import mobi.dayvson.redes.partydj.models.Room;
import mobi.dayvson.redes.partydj.models.User;
import mobi.dayvson.redes.partydj.models.Video;
import mocks.WebSocketMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ServerTest {

    private WebSocketMock webSocketMock;
    private Room roomMock;
    private Server server;

    @BeforeEach
    void setUp() {
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
    void onClose() {
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
    void onMessageEnterRoom() throws NoSuchFieldException, IllegalAccessException {
        WebSocketMock webSocketMockMaria = new WebSocketMock();

        server.onMessage(webSocketMockMaria, "enter_room:987:Maria");

        Field buffer = WebSocketMock.class.getDeclaredField("buffer");
        buffer.setAccessible(true);

        List<String> bf = (ArrayList<String>) buffer.get(webSocketMock);
        List<String> bfMaria = (ArrayList<String>) buffer.get(webSocketMockMaria);

        assertAll(() -> {
            assertTrue(webSocketMockMaria.hasBufferedData());
            assertTrue(webSocketMock.hasBufferedData());
            assertEquals("receive_message:0:Maria se conectou:Sala:enter_room:2:0", bf.get(0));
            assertEquals("receive_message:0:Maria se conectou:Sala:enter_room:2:0", bfMaria.get(0));
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

        Field buffer = WebSocketMock.class.getDeclaredField("buffer");
        buffer.setAccessible(true);

        List<String> bf = (ArrayList<String>) buffer.get(webSocketMock);

        assertAll(() -> {
            assertTrue(webSocketMock.hasBufferedData());
            assertEquals(video, room.nextVideo());
            assertEquals("receive_message:0:Vídeo adicionado por Dayvson:Sala:add_video:1:1", bf.get(0));
        });
    }

    @Test
    void onMessageAddVideoWithOtherUsersOnRoom() throws NoSuchFieldException, IllegalAccessException {
        WebSocketMock webSocketMockMaria = new WebSocketMock();

        server.onMessage(webSocketMockMaria, "enter_room:987:Maria");

        server.onMessage(webSocketMock, "add_video:987:Dayvson:youtube.com:you.com:PT20M1S:Teste");

        Field roomList = Server.class.getDeclaredField("roomList");
        roomList.setAccessible(true);

        List<IRoom> roomList1 = (List<IRoom>) roomList.get(server);

        IRoom room = roomList1.get(roomList1.indexOf(roomMock));

        Video video = new Video("youtube.com", "you.com", "PT20M1S", "Teste");

        Field buffer = WebSocketMock.class.getDeclaredField("buffer");
        buffer.setAccessible(true);

        List<String> bf = (ArrayList<String>) buffer.get(webSocketMockMaria);

        assertAll(() -> {
            assertTrue(webSocketMock.hasBufferedData());
            assertEquals(video, room.nextVideo());
            assertEquals("receive_message:0:Vídeo adicionado por Dayvson:Sala:add_video:2:1", bf.get(0));
        });
    }

    @Test
    void onMessageListVideos() throws NoSuchFieldException, IllegalAccessException {
        server.onMessage(webSocketMock, "list_videos:987");

        Field buffer = WebSocketMock.class.getDeclaredField("buffer");
        buffer.setAccessible(true);

        List<String> bf = (ArrayList<String>) buffer.get(webSocketMock);

        assertAll(() -> {
            assertTrue(webSocketMock.hasBufferedData());
            assertEquals("list_videos:0:" + "[]" + ":987", bf.get(0));
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

        Field buffer = WebSocketMock.class.getDeclaredField("buffer");
        buffer.setAccessible(true);

        List<String> bf = (ArrayList<String>) buffer.get(webSocketMock);

        assertAll(() -> {
            assertTrue(webSocketMock.hasBufferedData());
            assertEquals("get_video:0:" + video + ":987", bf.get(0));
        });
    }

    @Test
    void onMessageSendMessage() throws NoSuchFieldException, IllegalAccessException {
        server.onMessage(webSocketMock, "send_message:987:Testando:Dayvson");

        Field buffer = WebSocketMock.class.getDeclaredField("buffer");
        buffer.setAccessible(true);

        List<String> bf = (ArrayList<String>) buffer.get(webSocketMock);

        assertAll(() -> {
            assertTrue(webSocketMock.hasBufferedData());
            assertEquals("receive_message:0:Testando:Dayvson:receive_message:1:0", bf.get(0));
        });
    }

    @Test
    void onMessageEnterRoomThatRoomDoesntExist() throws NoSuchFieldException, IllegalAccessException {
        WebSocketMock webSocketMockMaria = new WebSocketMock();

        server.onMessage(webSocketMockMaria, "enter_room:789:Maria");

        Field buffer = WebSocketMock.class.getDeclaredField("buffer");
        buffer.setAccessible(true);

        List<String> bf = (ArrayList<String>) buffer.get(webSocketMockMaria);

        assertAll(() -> {
            assertTrue(webSocketMockMaria.hasBufferedData());
            assertFalse(webSocketMock.hasBufferedData());
            assertEquals("enter_room:1:Sala inexistente!:789", bf.get(0));
        });
    }

    @Test
    void onMessageAddVideoThatRoomDoesntExist() throws NoSuchFieldException, IllegalAccessException {

        server.onMessage(webSocketMock, "add_video:789:Dayvson:youtube.com:you.com:PT20M1S:Teste");

        Field roomList = Server.class.getDeclaredField("roomList");
        roomList.setAccessible(true);

        List<IRoom> roomList1 = (List<IRoom>) roomList.get(server);

        IRoom room = roomList1.get(roomList1.indexOf(roomMock));

        Video video = new Video("youtube.com", "you.com", "PT20M1S", "Teste");

        Field buffer = WebSocketMock.class.getDeclaredField("buffer");
        buffer.setAccessible(true);

        List<String> bf = (ArrayList<String>) buffer.get(webSocketMock);

        assertAll(() -> {
            assertTrue(webSocketMock.hasBufferedData());
            assertEquals(null, room.nextVideo());
            assertEquals("add_video:1:Sala inexistente!:789", bf.get(0));
        });
    }

    @Test
    void onMessageListVideosThatRoomDoesntExist() throws NoSuchFieldException, IllegalAccessException {
        server.onMessage(webSocketMock, "list_videos:789");

        Field buffer = WebSocketMock.class.getDeclaredField("buffer");
        buffer.setAccessible(true);

        List<String> bf = (ArrayList<String>) buffer.get(webSocketMock);

        assertAll(() -> {
            assertTrue(webSocketMock.hasBufferedData());
            assertEquals("list_videos:1:Sala inexistente!:789", bf.get(0));
        });
    }

    @Test
    void onMessageGetVideoThatRoomDoesntExist() throws NoSuchFieldException, IllegalAccessException {
        server.onMessage(webSocketMock, "add_video:789:Dayvson:youtube.com:you.com:PT20M1S:Teste");

        Field roomList = Server.class.getDeclaredField("roomList");
        roomList.setAccessible(true);

        List<IRoom> roomList1 = (List<IRoom>) roomList.get(server);

        IRoom room = roomList1.get(roomList1.indexOf(roomMock));

        Video video = new Video("youtube.com", "you.com", "PT20M1S", "Teste");

        server.onMessage(webSocketMock, "get_video:787");

        Field buffer = WebSocketMock.class.getDeclaredField("buffer");
        buffer.setAccessible(true);

        List<String> bf = (ArrayList<String>) buffer.get(webSocketMock);

        assertAll(() -> {
            assertTrue(webSocketMock.hasBufferedData());
            assertEquals("get_video:1:Sala inexistente!:787", bf.get(0));
        });
    }

    @Test
    void onMessageSendMessageThatRoomDoesntExist() throws NoSuchFieldException, IllegalAccessException {
        server.onMessage(webSocketMock, "send_message:897:Testando:Dayvson");

        Field buffer = WebSocketMock.class.getDeclaredField("buffer");
        buffer.setAccessible(true);

        List<String> bf = (ArrayList<String>) buffer.get(webSocketMock);

        assertAll(() -> {
            assertTrue(webSocketMock.hasBufferedData());
            assertEquals("send_message:1:Sala inexistente!:897", bf.get(0));
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
