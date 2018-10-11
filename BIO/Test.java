package BIO;

import java.io.IOException;
import java.util.Random;

public class Test {

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
                try {
                    new Server().start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }).start();

        //避免客户端先启动于服务端执行代码
        Thread.currentThread().sleep(100);

        char[] operators = {'+', '-', '*', '/'};
        Random random = new Random(System.currentTimeMillis());
        Client client = new Client();
        new Thread(() -> {
            while (true){
                String expression = random.nextInt(10) + "" + operators[random.nextInt(4)]+(random.nextInt(10)+1);
                client.send(expression);
                try {
                    Thread.currentThread().sleep(random.nextInt(3000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
