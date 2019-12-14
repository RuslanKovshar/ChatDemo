package ruslan.kovshar.client;

import com.sun.javafx.property.adapter.PropertyDescriptor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.adapter.JavaBeanObjectProperty;
import javafx.beans.property.adapter.JavaBeanObjectPropertyBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.w3c.dom.ls.LSOutput;

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

        userWindowController.getSendBtn()
                .setOnAction(actionEvent -> sendMessage(userWindowController));

        //Platform.runLater();
        TextFlow textFlow = userWindowController.getTextFlow();
        textFlow.setLineSpacing(5.0);
        //textFlow.maxWidth(500);
        ObjectProperty<TextFlow> tfp = new SimpleObjectProperty<>(textFlow);

        tfp.addListener((obs, oldVal, newVal) -> {
            System.out.println(obs + " " + oldVal + "->" + newVal);
            obs.getValue().getChildren().forEach(System.out::println);

            oldVal.getChildren().forEach(System.out::println);
            newVal.getChildren().forEach(System.out::println);
            oldVal.getChildren().add(newVal.getChildren().get(0));
            //ObservableList<Node> oldValChildren = oldVal.getChildren();
            //ObservableList<Node> newValChildren = newVal.getChildren();
            //obs.getValue().getChildren().addAll(oldValChildren);
            //obs.getValue().getChildren().addAll(newValChildren);
        });
       /* tfp.addListener((observableValue, textFlow1, t1) -> Platform.runLater(() -> {
            textFlow1.getChildren().add(t1);
            userWindowController.getScroll().setVvalue(1.0);
        }));*/

        MyService service = new MyService(scanner, tfp);
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

    static class ServerListener implements Runnable {

        private Scanner scanner;
        private TextFlow textFlow;

        public ServerListener(Scanner scanner, TextFlow textFlow) {
            this.scanner = scanner;
            this.textFlow = textFlow;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    if (scanner.hasNextLine()) {
                        String s = scanner.nextLine();

                        //textFlow.getChildren().add(new Text(s + "\n"));
                        System.out.println(s);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class MyService extends Service<String> {
        private Scanner scanner;
        ObjectProperty<TextFlow> tfp = new SimpleObjectProperty<>();

        public MyService(Scanner scanner, Property<TextFlow> property) {
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
                                //textFlow.getChildren().add(new Text(s + "\n"));
                                System.out.println(s + "!!!!!");
                                //TextFlow children = tfp.get();
                                //children.getChildren().add(new Text(s));
                                TextFlow textFlow1 = new TextFlow();
                                Text text = new Text(s);
                                text.setFont(new Font(20));
                                text.setWrappingWidth(200);
                                text.setTextAlignment(TextAlignment.JUSTIFY);
                                //text.setText("The quick brown fox jumps over the lazy dog");

                                textFlow1.getChildren().add(text);
                                tfp.set(textFlow1);
                                //return s;
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
