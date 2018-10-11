package BIO;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private int default_port = 23456;
    private ServerSocket server;

    public Server() {}

    public Server(int port){
        default_port = port;
    }

    //此方法不会被并发访问，因而不考虑效率
    public synchronized void start(int port) throws IOException {
        if (server != null)
            return;

        try {
            server = new ServerSocket(port);
            System.out.println("Server has been start up on port: " + port);
            while (true){
                Socket socket = server.accept();
                new Thread(new ServerHandler(socket)).start();
            }
        }finally {
            //必要的清理工作
            if (server != null){
                System.out.println("Server has been shut down!");
                server.close();
                server = null;
            }
        }
    }

    public void start() throws IOException {
        start(default_port);
    }

}
