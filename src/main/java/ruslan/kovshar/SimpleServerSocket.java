package ruslan.kovshar;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServerSocket {
    public static void main(String[] args) throws IOException {
        try(ServerSocket serverSocket = new ServerSocket(8189);
            Socket socket = serverSocket.accept()) {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);
            writer.println("Hello from Ruslan!");
        }
    }
}
