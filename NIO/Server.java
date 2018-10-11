package NIO;

public class Server {

    private int default_port = 23456;
    private ServerHandler serverHandler;

    public void start(){
        start(default_port);
    }

    public synchronized void start(int port){
        if (serverHandler != null)
            serverHandler.stop();
        serverHandler = new ServerHandler(port);
        new Thread(serverHandler, "Server").start();
    }

}
