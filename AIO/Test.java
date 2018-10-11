package AIO;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        new Server().start();

        Thread.sleep(10);

        Client client = new Client();
        client.start();
        System.out.println("input req message: ");
        Scanner scanner = new Scanner(System.in);
        while (client.sendMsg(scanner.nextLine()));
    }

}
