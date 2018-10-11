package AIO;

public class Client {

    private String default_ip = "127.0.0.1";
    private int default_port = 23456;
    private AsyncClientHandler clientHandler;

    public Client() {}

    public Client(String default_ip, int default_port) {
        this.default_ip = default_ip;
        this.default_port = default_port;
    }

    public synchronized void start(String ip, int port){
        if (clientHandler != null)
            return;
        clientHandler = new AsyncClientHandler(ip, port);
        new Thread(clientHandler, "Client").start();
    }

    public void start(){
        start(default_ip, default_port);
    }

    public boolean sendMsg(String msg){
        if (msg.equals("q"))
            return false;
        clientHandler.sendMsg(msg);
        return true;
    }

}
