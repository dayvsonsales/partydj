package mobi.dayvson.redes.partydj;

import java.net.InetSocketAddress;

public class App {

    public static void main(String args[]){
        String host = "localhost";
        Integer port = 8000;
        Server server = new Server(new InetSocketAddress(host, port));

        server.run();
    }

}
