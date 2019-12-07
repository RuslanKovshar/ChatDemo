package ruslan.kovshar.client;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class UserWindow extends Application {

    private Socket socket;
    private Scanner scanner;
    private PrintWriter printWriter;

    @FXML
    private TextField textField;

    @FXML
    private TextFlow textFlow;

    @FXML
    private Button sendBtn;

    public UserWindow() {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress("localhost", 8189), 2000);
            scanner = new Scanner(socket.getInputStream());
            printWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        sendBtn.setOnAction(actionEvent -> {
            String text = textField.getText().trim();
            printWriter.println(text);
        });
    }

    public void launchApplication(String... args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/user_window.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("s1mpleChat");
        primaryStage.show();
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
}
