package mobi.dayvson.redes.partydj;

import spark.Filter;
import spark.Spark;
import spark.utils.IOUtils;

import java.net.InetSocketAddress;
import static spark.Spark.*;
public class App {

    public static void main(String args[]){
        staticFiles.location("/web");
        port(4000);

        after((Filter) (request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET");
        });

        get("/", (req, res) -> IOUtils.toString(Spark.class.getResourceAsStream("/web/index.html")));

        startServerSocket();
    }

    private static void startServerSocket(){
        String host = "localhost";
        Integer port = 8000;
        Server server = new Server(new InetSocketAddress(host, port));

        server.run();
    }
}
