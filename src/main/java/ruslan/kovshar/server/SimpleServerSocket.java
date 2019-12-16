package ruslan.kovshar.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SimpleServerSocket {

    private static List<MySocket> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            System.out.println("Server start...");
            while (true) {
                Socket socket = serverSocket.accept();

                MySocket target = new MySocket(socket);
                clients.add(target);
                new Thread(target).start();
            }
        }
    }

    public static void removeClient(MySocket target) {
        clients.remove(target);
    }

    public static void sendToAll(String message) {
        for (MySocket target :
                clients) {
            target.sendMessage(message);
        }
    }
}

class MySocket implements Runnable {
    private Socket socket;
    private Scanner scanner;
    private PrintWriter writer;

    public MySocket(Socket socket) {
        try {
            this.socket = socket;
            this.scanner = new Scanner(socket.getInputStream());
            this.writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (scanner.hasNextLine()) {
                    String string = scanner.nextLine();
                    if (string.equalsIgnoreCase("#exit#")) {
                        System.err.println(string);
                        SimpleServerSocket.removeClient(this);
                        break;
                    }
                    SimpleServerSocket.sendToAll(string);
                    //writer.println("Your message: " + string);
                    System.out.println(string);
                }
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }
}

