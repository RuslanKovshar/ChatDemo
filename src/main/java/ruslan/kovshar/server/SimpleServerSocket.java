package ruslan.kovshar.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SimpleServerSocket {
    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            System.out.println("Server start...");
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new MySocket(socket)).start();
            }
        }
    }
}

class MySocket implements Runnable {
    private Socket socket;

    public MySocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (Scanner scanner = new Scanner(socket.getInputStream());
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
            while (true) {
                if (scanner.hasNextLine()) {
                    String string = scanner.nextLine();
                    if (string.equalsIgnoreCase("#exit#")) {
                        System.err.println(string);
                        break;
                    }
                    writer.println("Your message: " + string);
                    System.out.println(string);
                }
                Thread.sleep(100);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

