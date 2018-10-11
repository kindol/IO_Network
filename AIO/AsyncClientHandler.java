package AIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class AsyncClientHandler implements CompletionHandler<Void, AsyncClientHandler>, Runnable {

    private String ip;
    private int port;
    private AsynchronousSocketChannel clientChannel;
    private CountDownLatch latch;

    public AsyncClientHandler() {}

    public AsyncClientHandler(String ip, int port) {
        this.ip = ip;
        this.port = port;
        try {
            clientChannel = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg){
        byte[] req = msg.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
        writeBuffer.put(req);
        writeBuffer.flip();

        //异步写
        clientChannel.write(writeBuffer, writeBuffer, new WriteHandler_Client(clientChannel, latch));
    }

    //连接服务器成功
    @Override
    public void completed(Void result, AsyncClientHandler attachment) {
        System.out.println("Client connect successfully");
    }

    @Override
    public void failed(Throwable exc, AsyncClientHandler attachment) {
        System.out.println("Client connect fail");
        exc.printStackTrace();
        try {
            clientChannel.close();
            latch.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        //发起异步连接，回调参数是这个类本身，如果连接成功会回调complete方法
        clientChannel.connect(new InetSocketAddress(ip, port), this, this);

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            clientChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
