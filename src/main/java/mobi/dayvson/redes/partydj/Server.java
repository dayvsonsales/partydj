package mobi.dayvson.redes.partydj;

import mobi.dayvson.redes.partydj.enums.Message;
import mobi.dayvson.redes.partydj.interfaces.IRoom;
import mobi.dayvson.redes.partydj.models.Protocol;
import mobi.dayvson.redes.partydj.models.Room;
import mobi.dayvson.redes.partydj.models.User;
import mobi.dayvson.redes.partydj.models.Video;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Server extends WebSocketServer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private List<IRoom> roomList;

    private List<User> guests;

    public Server(InetSocketAddress address, List<IRoom> roomList, List<User> guests){
        super(address);
        this.roomList = roomList;
        this.guests = guests;
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        guests.add(new User(UUID.randomUUID(), "guest", webSocket));
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        List<IRoom> rooms = roomList.stream().filter(room -> (room.getUserList().stream().filter(p -> p.getWebSocket().equals(webSocket)).collect(Collectors.toList()).size() > 0)).collect(Collectors.toList());
        rooms.forEach(r -> {
            User user = r.getUserList().stream().filter(u -> u.getWebSocket().equals(webSocket)).findFirst().get();
            r.getUserList().removeIf(u -> u.getWebSocket().equals(webSocket));
            sendMessageToAllUserOnRoom(r, user.getName() + " se desconectou", "Sala", "send_message");
        });
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        Message m = Protocol.proccess(s);

        if(m == Message.CREATE_ROOM){
            User user = new User(UUID.randomUUID(), s.split(":")[1], webSocket);
            String token = generateRoomToken();

            while(roomList.contains(new Room(token))){
                token = generateRoomToken();
            }

            Room room = new Room(token);
            room.addUser(user);

            new Thread(room).start();

            roomList.add(room);

            webSocket.send("create_room:0:Sucesso ao criar a sala!:" + token);
        }

        if(m == Message.ENTER_ROOM){
            String[] contents =  s.split(":");
            String token = contents[1];
            String name = contents[2];

            IRoom room = new Room(token);

            if(roomList.contains(room)){
                IRoom _room = roomList.get(roomList.indexOf(room));
                _room.addUser(new User(UUID.randomUUID(), name, webSocket));

                webSocket.send("enter_room:0:Conectado com sucesso!:" + token + ":" + _room.getUserList().get(0).getName());

                sendMessageToAllUserOnRoom(_room, name + " se conectou", "Sala", "enter_room");

                return ;
            }

            webSocket.send("enter_room:1:Sala inexistente!:" + token);
        }

        if(m == Message.ADD_VIDEO){
            String[] contents =  s.split(":");
            String token = contents[1];
            String name = contents[2];
            String videoUrl = contents[3];
            String videoThumb = contents[4];
            String videoDuration = contents[5];
            String videoName = contents[6];

            logger.debug(name + "" + videoDuration + "" + videoUrl + "" + videoThumb);

            Room room = new Room(token);

            if(roomList.contains(room)){
                IRoom _room = roomList.get(roomList.indexOf(room));
                Video video = new Video(videoUrl, videoThumb, videoDuration, videoName);

                _room.addVideo(video);

                webSocket.send("add_video:0:Novo vídeo adicionado com sucesso!:" + token);

                sendMessageToAllUserOnRoom(_room,"Vídeo adicionado por " + name, "Sala", "add_video");
                return ;
            }

            webSocket.send("add_video:1:Sala inexistente!:" + token);
        }

        if(m == Message.LIST_VIDEOS){
            String[] contents =  s.split(":");
            String token = contents[1];

            Room room = new Room(token);

            if(roomList.contains(room)){
                IRoom _room = roomList.get(roomList.indexOf(room));

                webSocket.send("list_videos:0:" + _room.getVideoQueueJson() + ":" + token);
                return ;
            }

            webSocket.send("list_videos:1:Sala inexistente!:" + token);
        }

        if(m == Message.GET_VIDEO){
            String[] contents =  s.split(":");
            String token = contents[1];

            Room room = new Room(token);

            if(roomList.contains(room)){
                IRoom _room = roomList.get(roomList.indexOf(room));

                webSocket.send("get_video:0:" + _room.nextVideo() + ":" + token);
                return ;
            }

            webSocket.send("get_video:1:Sala inexistente!:" + token);
        }

        if(m == Message.SEND_MESSAGE){
            String[] contents =  s.split(":");
            String token = contents[1];
            String message = contents[2];
            String user = contents[3];

            logger.debug(token + message + user);

            Room room = new Room(token);

            if(roomList.contains(room)){
                IRoom _room = roomList.get(roomList.indexOf(room));

                sendMessageToAllUserOnRoom(_room, message, user, "receive_message");
                return ;
            }

            webSocket.send("send_message:1:Sala inexistente!:" + token);
        }

    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        logger.error("Erro ao criar a conexão socket", e.getMessage());
    }

    @Override
    public void onStart() {
        logger.debug("Servidor iniciado com sucesso!");
    }

    private String generateRoomToken(){
        UUID uuid = UUID.randomUUID();
        return String.valueOf(uuid).substring(0, 6);
    }

    private void sendMessageToAllUserOnRoom(IRoom room, String message, String sender, String type){
        room.getUserList().forEach(user -> {
            user.getWebSocket().send("receive_message:0:" + message + ":" + sender + ":" + type + ":" + room.getUserList().size() + ":" + room.queueCount());
        });
    }
}
