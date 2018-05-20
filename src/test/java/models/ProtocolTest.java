package models;

import mobi.dayvson.redes.partydj.enums.Message;
import mobi.dayvson.redes.partydj.models.Protocol;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProtocolTest {

    @Test
    void enterRoom(){
        String msg = "enter_room:<nome>:<token>";

        assertEquals(Message.ENTER_ROOM, Protocol.proccess(msg));
    }

    @Test
    void createRoom(){
        String msg = "create_room:<nome>";

        assertEquals(Message.CREATE_ROOM, Protocol.proccess(msg));
    }

    @Test
    void sendMessage(){
        String msg = "send_message:<token>:<mensagem>:<nome>";

        assertEquals(Message.SEND_MESSAGE, Protocol.proccess(msg));
    }

    @Test
    void receiveMessage(){
        String msg = "receive_message:<erro>:<mensagem>:<quem enviou>:<tipo>:<tamanho de usuarios na sala>:<contagem de videos na fila>";

        assertEquals(Message.RECEIVE_MESSAGE, Protocol.proccess(msg));
    }

    @Test
    void addVideo(){
        String msg = "add_video:<erro>:<mensagem>:<token>";

        assertEquals(Message.ADD_VIDEO, Protocol.proccess(msg));
    }

    @Test
    void listVideos(){
        String msg = "list_videos:<erro>:<json em formato de texto ou mensagem de erro>:<token>";

        assertEquals(Message.LIST_VIDEOS, Protocol.proccess(msg));
    }

    @Test
    void getVideo(){
        String msg = "get_video:<erro>:<videoId>:<videoThumb>:<videoNome>";

        assertEquals(Message.GET_VIDEO, Protocol.proccess(msg));
    }

    @Test
    void unknow(){
        String msg = "blablabla";

        assertEquals(Message.UNKNOW, Protocol.proccess(msg));
    }

}
