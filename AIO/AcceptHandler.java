package AIO;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

//接收客户端发起的新连接
public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncServerHandler> {
    //第一个参数是当前的channel，第二个参数是上一步传递过来的handler
    @Override
    public void completed(AsynchronousSocketChannel result, AsyncServerHandler attachment) {
        Server.clientCount++;
        System.out.println("连接的客户端数为：" + Server.clientCount);

        //继续接收其他客户端的请求，后一个参数是CompletionHandler
        attachment.channel.accept(attachment, this);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //异步读，第3个参数为接收消息回调的业务handler
        result.read(buffer, buffer, new ReadHandler(result));
    }

    @Override
    public void failed(Throwable exc, AsyncServerHandler attachment) {
        exc.printStackTrace();
        attachment.latch.countDown();
    }
}
