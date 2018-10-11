#### NIO与BIO的区别
NIO提供了与传统BIO模型中的Socket和ServerSocket相对应的SocketChannel和ServerSocketChannel两种不同的套接字通道实现。

新增的两种通道支持阻塞和非阻塞两种模式。

对于低负载、低并发的应用程序，可以使用同步阻塞I/O来提升开发速率和更好的维护性；

对于高负载、高并发的（网络）应用，应使用NIO的非阻塞模式来开发。

---

#### 基础知识
1.缓冲区Buffer

这里的Buffer是一个对象，包含读入或者写出的数据。

NIO库中，所有数据都是用缓冲区处理的，读取数据读到缓冲区，写入数据写到缓冲区，本质上，缓冲区是一个数组，其提供了对数据的结构化访问以及维护读写位置等信息

这些缓冲区有：ByteBuffe、CharBuffer、 ShortBuffer、IntBuffer、LongBuffer、FloatBuffer、DoubleBuffer。他们实现了相同的接口：Buffer     

2.通道Channel

前面的Buffer是数据的载体，而我们是通过Channel对数据进行读取与写入的，其允许同时写读，通道是双向的，这点与流不同，

Channel分为两大类：
- SelectableChannel：用户网络读写(ServerSocketChannel和SocketChannel都是SelectableChannel的子类)
    
    所有的SelectableChannel都需要register到selector上，并且有相应的注册事件用于启动对应的channel，比如ServerSocketChannel就一般是注册SelectionKey.OP_ACCEPT事件，
    所有的都需要绑定端口，客户端还需要绑定ip
    
    ServerSocketChannel位于服务端，用于接受请求并且创建与客户端对应的SocketChannel，并将SocketChannel注册到selector上，

- FileChannel：用于文件操作

3.多路复用器Selector

java nio的基础，提供选择已就绪任务的能力：**Selector会不断轮询注册在其上的Channel**，如果某个Channel上面发生读或者写事件，这个Channel就处于就绪状态，会被Selector轮询出来，然后**通过SelectionKey可以获取就绪Channel的集合**，进行后续的I/O操作。

selector共可以监听4种事件，分别为SelectionKey.OP_READ, OP_CONNECT（客户端用于发起连接建立请求）, OP_WRITE, OP_ACCEPT（服务端用与接收连接的建立）

一个Selector可以同时轮询多个Channel，因为JDK使用了epoll()代替传统的select实现，所以没有最大连接句柄1024/2048的限制。所以，只需要一个线程负责Selector的轮询，就可以接入成千上万的客户端。

#### 总体流程
1. 打开ServerSocketChannel，监听客户端连接
2. 绑定监听端口，设置连接为非阻塞模式
3. 创建Reactor线程，启动多路复用器（selector）并启动线程
4. 将ServerSocketChannel注册到Reactor线程中的Selector上，监听ACCEPT事件（连接请求）
5. Selector轮询准备就绪的key
6. Selector监听到新的客户端接入，处理新的接入请求，完成TCP三次握手，建立物理链路
7. 设置客户端链路为非阻塞模式
8. 将新接入的客户端连接注册到Reactor线程的Selector上，监听读操作，读取客户端发送的网络消息
9. 异步读取客户端消息到缓冲区
10. 对Buffer编解码，处理半包消息，将解码成功的消息封装成Task
11. 将应答消息编码为Buffer，调用SocketChannel的write将消息异步发送给客户端