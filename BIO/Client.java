package BIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private int default_server_port = 23456;
    private String defalut_server_ip = "127.0.0.1";

    public Client(int default_server_port, String defalut_server_ip) {
        this.default_server_port = default_server_port;
        this.defalut_server_ip = defalut_server_ip;
    }

    public Client() {
    }

    public void send(String expression){
        send(defalut_server_ip, default_server_port, expression);
    }

    public void send(String ip, int port, String expression){
        System.out.println("The expression is: " + expression);
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            socket = new Socket(ip, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //注意这里需要设置为true，否则需要自己调用out.flush()，二者均没有的话会使信息一直没有冲刷出去，导致服务端一致处于等待状态
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(expression);
            System.out.println("The result is: " + in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //必须的清理工作
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
            if (out != null){
                out.close();
                out = null;
            }
            if (socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socket = null;
            }
        }
    }

}
