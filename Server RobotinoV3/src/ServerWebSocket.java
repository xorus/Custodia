/*import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;*/

//public class ServerWebSocket extends WebSocketServer {

   /* private static int TCP_PORT = 8888;

    private List<WebSocket> conns;

    public ServerWebSocket() {
        super(new InetSocketAddress(TCP_PORT));
        this.conns = new ArrayList<WebSocket>();
    }

    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        conns.add(webSocket);
        System.out.println("New connection from " + webSocket.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        conns.remove(webSocket);
        System.out.println("Closed connection to " + webSocket.getRemoteSocketAddress().getAddress().getHostAddress());

    }

    public void onMessage(WebSocket webSocket, String s) {
        System.out.println("Message from client :" + s);

        for (WebSocket sock : this.conns) {
            sock.send(s);
        }
    }

    public void onError(WebSocket webSocket, Exception e) {
        if (webSocket != null) {
            this.conns.remove(webSocket);
        }
        System.out.println("ERROR from " +  webSocket.getRemoteSocketAddress().getAddress().getHostAddress());
    }*/
//}