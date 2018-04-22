package mobi.dayvson.redes.partydj.models;

import mobi.dayvson.redes.partydj.enums.Message;

public class Protocol {

    private Protocol(){

    }

    public static Message proccess(String message){

        Message m = Message.UNKNOW;
        message = message.split(":")[0];

        switch (message){
            case "enter_room":
                m = Message.ENTER_ROOM;
                break;
            case "create_room":
                m = Message.CREATE_ROOM;
                break;
            case "send_message":
                m = Message.SEND_MESSAGE;
                break;
            case "receive_message":
                m = Message.RECEIVE_MESSAGE;
                break;
            case "add_video":
                m = Message.ADD_VIDEO;
                break;
            case "list_videos":
                m = Message.LIST_VIDEOS;
                break;
            case "get_video":
                m = Message.GET_VIDEO;
                break;
        }

        return m;
    }

    public static String convert(Message message){
        return message.toString().toLowerCase();
    }

}
