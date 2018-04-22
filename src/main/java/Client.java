import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

public class Client extends WebSocketClient {

    public Client(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public Client(URI serverURI) {
        super(serverURI);
    }

    public static void main(String args[]) throws URISyntaxException {
        Client client = new Client(new URI("ws://localhost:8000"));
        client.connect();


    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("Se conectou");
        send("create_room:");
    }

    @Override
    public void onMessage(String s) {
        System.out.println(s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println(s);
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onMessage(ByteBuffer bytes) {

    }
}
