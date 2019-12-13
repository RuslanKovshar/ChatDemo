package ruslan.kovshar.client;

import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class UserWindow extends Application {

    private Socket socket;
    private Scanner scanner;
    private PrintWriter printWriter;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    public void setPrintWriter(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }

    public UserWindow() {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress("localhost", 8189), 2000);
            scanner = new Scanner(socket.getInputStream());
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            System.out.println(scanner.hashCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Stoop");
        super.stop();
    }

    private void sendMessage(UserWindowController userWindowController) {
        String text = userWindowController.getTextField().getText().trim();
        System.err.println(scanner.hashCode());
        System.err.println(printWriter);
        printWriter.println(text);
        userWindowController.getTextField().setText("");
    }

    public void launchApplication(String... args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        System.out.println("Start init");

        System.out.println("End init");
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Start start");
        URL location = getClass().getResource("/user_window.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Pane root = loader.load();
        UserWindowController userWindowController = loader.getController();
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("s1mpleChat");
        primaryStage.show();

        userWindowController.getSendBtn().setOnAction(actionEvent -> sendMessage(userWindowController));

        MyService service = new MyService(scanner);
        //service.setOnSucceeded(event -> System.out.println(event.getSource().getValue()));
        service.start();


        System.out.println("End start");
    }

    public void finishWork() {
        printWriter.println("#exit#");
        try {
            socket.close();
            printWriter.close();
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static class MyService extends Service<String> {

        private Scanner scanner;

        public MyService(Scanner scanner) {
            System.err.println("scanner: " + scanner.hashCode());
            this.scanner = scanner;
        }

        @Override
        protected Task<String> createTask() {
            return new Task<String>() {
                @Override
                protected String call() throws Exception {
                    try {
                        while (true) {
                            if (scanner.hasNextLine()) {
                                String s = scanner.nextLine();
                                System.out.println(s);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return "";
                }
            };
        }
    }
}
