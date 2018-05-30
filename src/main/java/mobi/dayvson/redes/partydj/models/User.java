package mobi.dayvson.redes.partydj.models;

import org.java_websocket.WebSocket;

import java.util.Objects;
import java.util.UUID;

public class User {

    private final UUID uuid;
    private final String name;
    private final WebSocket webSocket;

    public User(UUID uuid, String name, WebSocket webSocket) {
        this.uuid = uuid;
        this.name = name;
        this.webSocket = webSocket;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) &&
                Objects.equals(webSocket, user.webSocket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, webSocket);
    }

    public String toString() {
        return this.name;
    }
}

