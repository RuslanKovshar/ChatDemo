package ruslan.kovshar.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class UserWindow extends Application {

    private Socket socket;
    private Scanner scanner;
    private PrintWriter printWriter;
    private VBox vBox;
    private ScrollPane scroll;
    private TextField textField;

    public VBox getvBox() {
        return vBox;
    }

    public void setvBox(VBox vBox) {
        this.vBox = vBox;
    }

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
            socket.connect(new InetSocketAddress("77.47.221.76", 8189), 2000);
            scanner = new Scanner(socket.getInputStream());
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            System.out.println(scanner.hashCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        printWriter.println("#exit#");
        try {
            socket.close();
            printWriter.close();
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(UserWindowController userWindowController) {
        String text = textField.getText().trim();
        System.err.println(scanner.hashCode());
        System.err.println(printWriter);
        printWriter.println(text);
        userWindowController.getTextField().setText("");
    }

    public void launchApplication(String... args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL location = getClass().getResource("/user_window.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Pane root = loader.load();
        UserWindowController userWindowController = loader.getController();

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("s1mpleChat");
        primaryStage.show();
        textField = userWindowController.getTextField();
        textField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().getName().equals("Enter")) {
                sendMessage(userWindowController);
            }
        });

        userWindowController.getSendBtn()
                .setOnAction(actionEvent -> {
                    sendMessage(userWindowController);
                    textField.requestFocus();
                });

        scroll = userWindowController.getScroll();

        vBox = userWindowController.getvBox();
        scroll.vvalueProperty().bind(vBox.heightProperty());
        ObjectProperty<VBox> tfp = new SimpleObjectProperty<>(vBox);

        tfp.addListener((observableValue, vBoxx, t1) -> Platform.runLater(() -> getvBox().getChildren().addAll(t1.getChildren())));
        MyService service = new MyService(scanner, tfp);
        service.start();
    }

    public void update(Text text) {
        Platform.runLater(() -> vBox.getChildren().add(text));
    }

    class MyService extends Service<String> {
        private Scanner scanner;
        ObjectProperty<VBox> tfp = new SimpleObjectProperty<>();

        public MyService(Scanner scanner, Property<VBox> property) {
            this.scanner = scanner;
            this.tfp.bindBidirectional(property);
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
                                byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
                                String utf8String = new String(bytes);
                                Text text = new Text(utf8String);
                                text.setFont(new Font(14));
                                text.setWrappingWidth(vBox.widthProperty().intValue());
                                text.setTextAlignment(TextAlignment.JUSTIFY);
                                update(text);
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
