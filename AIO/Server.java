package AIO;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

public class Server {

    private int default_port = 23456;
    private AsyncServerHandler serverHandler;
    public static volatile long clientCount = 0;

    public Server() {}

    public Server(int default_port) {
        this.default_port = default_port;
    }

    public void start(){
        start(default_port);
    }

    //Server仅仅作为一个门面，传递port，其他工作做转交给AsyncServerHandler，
    //AsyncServerHandler负责开启通道，绑定端口，并且配置其他handler
    public synchronized void start(int port){
        if (serverHandler != null)
            return;
        serverHandler = new AsyncServerHandler(port);
        new Thread(serverHandler, "Server").start();
    }
}
