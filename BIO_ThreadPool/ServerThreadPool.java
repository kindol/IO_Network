package BIO_ThreadPool;

import BIO.ServerHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerThreadPool {
    private int default_port = 23456;
    private ServerSocket server;
    private ExecutorService executorService = Executors.newFixedThreadPool(60);

    public ServerThreadPool(int default_port) {
        this.default_port = default_port;
    }

    public ServerThreadPool() {}

    public void start() throws IOException {
        start(default_port);
    }

    public synchronized void start(int port) throws IOException {
        if (server != null)
            return;

        try {
            server = new ServerSocket(port);
            System.out.println("Server has been start up on port: " + port);

            while (true){
                Socket socket = server.accept();
                //调用线程池执行
                executorService.execute(new ServerHandler(socket));
            }
        }finally {
            if (server != null){
                System.out.println("Server has been shut down!");
                server.close();
                server = null;
            }
        }

    }
}
