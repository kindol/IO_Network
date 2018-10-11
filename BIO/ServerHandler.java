package BIO;

import Utils.Calculator;

import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerHandler implements Runnable {

    private Socket socket;

    public ServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            String expression, result;
            while (true){
                if ((expression = in.readLine()) == null)   break;
                System.out.println("Server received message: " + expression);
                try {
                    result = Calculator.cal(expression).toString();
                } catch (ScriptException e) {
                    result = "Calculate error: " + e.toString();
                }
                out.println(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //必要的清理工作
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
