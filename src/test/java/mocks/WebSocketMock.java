package mocks;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.framing.Framedata;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WebSocketMock implements WebSocket {

    private List<String> buffer = new ArrayList<>();

    @Override
    public void close(int code, String message) {

    }

    @Override
    public void close(int code) {

    }

    @Override
    public void close() {

    }

    @Override
    public void closeConnection(int code, String message) {

    }

    @Override
    public void send(String text) throws NotYetConnectedException {
        buffer = new ArrayList<>();
        buffer.add(text);
    }

    @Override
    public void send(ByteBuffer bytes) throws IllegalArgumentException, NotYetConnectedException {

    }

    @Override
    public void send(byte[] bytes) throws IllegalArgumentException, NotYetConnectedException {

    }

    @Override
    public void sendFrame(Framedata framedata) {

    }

    @Override
    public void sendFrame(Collection<Framedata> frames) {

    }

    @Override
    public void sendPing() throws NotYetConnectedException {

    }

    @Override
    public void sendFragmentedFrame(Framedata.Opcode op, ByteBuffer buffer, boolean fin) {

    }

    @Override
    public boolean hasBufferedData() {
        return !buffer.isEmpty();
    }

    @Override
    public InetSocketAddress getRemoteSocketAddress() {
        return null;
    }

    @Override
    public InetSocketAddress getLocalSocketAddress() {
        return null;
    }

    @Override
    public boolean isConnecting() {
        return false;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public boolean isClosing() {
        return false;
    }

    @Override
    public boolean isFlushAndClose() {
        return false;
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public Draft getDraft() {
        return null;
    }

    @Override
    public READYSTATE getReadyState() {
        return null;
    }

    @Override
    public String getResourceDescriptor() {
        return null;
    }

    @Override
    public <T> void setAttachment(T attachment) {

    }

    @Override
    public <T> T getAttachment() {
        return null;
    }
}
