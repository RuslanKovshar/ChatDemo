package ruslan.kovshar.client;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class UserWindow extends Application {

    @FXML
    private TextField textField;

    @FXML
    private TextFlow textFlow;

    @FXML
    private void initialize() {
        textFlow.getChildren().add(new Text("Hello there!"));
    }

    public void launchApplication(String... args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/user_window.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("s1mpleChat");
        primaryStage.show();
    }
}
